package com.ferosburn.databencana.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferosburn.databencana.data.DataReport
import com.ferosburn.databencana.data.DisasterModel
import com.ferosburn.databencana.data.Report
import kotlinx.coroutines.launch

enum class DataStatus { LOADING, DONE, ERROR }

class HomeViewModel : ViewModel() {
    private val _disasterData = MutableLiveData<DataReport>()
    private val _listDisaster = MutableLiveData<List<DisasterModel>>()
    private val _bbox = MutableLiveData<List<Double>>()
    private val _status = MutableLiveData<DataStatus>()

    val listDisaster: LiveData<List<DisasterModel>> = _listDisaster
    val disasterData: LiveData<DataReport> = _disasterData
    val bbox: LiveData<List<Double>> = _bbox
    val status: LiveData<DataStatus> = _status

    init {
        fetchDisasterData()
    }

    private fun fetchDisasterData() {
        viewModelScope.launch {
            _status.value = DataStatus.LOADING
            try {
                _disasterData.value = Report.retrofitService.getRecentReports(604800)
                _listDisaster.value = disasterData.value?.result?.objectReport?.output?.geometries?.map {
                    DisasterModel(
                        pkey = it.properties.pkey,
                        createdAt = it.properties.createdAt,
                        imageUrl = it.properties.imageUrl,
                        disasterType = it.properties.disasterType,
                        instanceRegionCode = it.properties.propertyTags.instanceRegionCode ?: "",
                        coordinates = it.coordinates
                    )
                }
                _bbox.value = disasterData.value?.result?.bbox
                _status.value = DataStatus.DONE
            } catch (e: Exception) {
                _listDisaster.value = listOf()
                _status.value = DataStatus.ERROR
            }
        }
    }

}