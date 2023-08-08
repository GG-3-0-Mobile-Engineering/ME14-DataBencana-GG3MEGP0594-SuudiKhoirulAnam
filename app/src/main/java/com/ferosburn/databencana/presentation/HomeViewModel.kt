package com.ferosburn.databencana.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferosburn.databencana.data.source.remote.DataReport
import com.ferosburn.databencana.network.DisasterModel
import com.ferosburn.databencana.data.source.remote.Report
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

    // max number of day = 7
    private val numberOfDay = 2

    fun fetchDisasterData() {
        viewModelScope.launch {
            _status.value = DataStatus.LOADING
            try {
                _disasterData.value =
                    Report.retrofitService.getRecentReports(DAY_TIME_PERIOD * numberOfDay, null, null)
                _listDisaster.value = listOf()
                _listDisaster.value =
                    disasterData.value?.result?.objectReport?.output?.geometries?.map {
                        DisasterModel(
                            pkey = it.properties.pkey,
                            createdAt = it.properties.createdAt,
                            imageUrl = it.properties.imageUrl,
                            disasterType = it.properties.disasterType,
                            instanceRegionCode = it.properties.propertyTags.instanceRegionCode
                                ?: "",
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

    fun fetchFilteredList(
        disasterValue: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        provinceCode: String? = null
    ) {
        viewModelScope.launch {
            _status.value = DataStatus.LOADING
            try {
                if (startDate.isNullOrBlank() || endDate.isNullOrBlank()) {
                    _disasterData.value =
                        Report.retrofitService.getRecentReports(
                            DAY_TIME_PERIOD * numberOfDay,
                            provinceCode,
                            disasterValue
                        )
                } else {
                    _disasterData.value =
                        Report.retrofitService.getReports(startDate, endDate, provinceCode)
                }
                _listDisaster.value = listOf()
                _listDisaster.value =
                    disasterData.value?.result?.objectReport?.output?.geometries?.map {
                        DisasterModel(
                            pkey = it.properties.pkey,
                            createdAt = it.properties.createdAt,
                            imageUrl = it.properties.imageUrl,
                            disasterType = it.properties.disasterType,
                            instanceRegionCode = it.properties.propertyTags.instanceRegionCode
                                ?: "",
                            coordinates = it.coordinates
                        )
                    }
                if ((!startDate.isNullOrBlank() || !endDate.isNullOrBlank()) && !disasterValue.isNullOrBlank()) {
                    filterByDisasterType(disasterValue)
                }
                _bbox.value = disasterData.value?.result?.bbox
                _status.value = DataStatus.DONE
            } catch (e: Exception) {
                _listDisaster.value = listOf()
                _status.value = DataStatus.ERROR
            }
        }
    }

    fun isListEmpty(): Boolean {
        return _listDisaster.value.isNullOrEmpty()
    }

    private fun filterByDisasterType(disasterType: String) {
        _listDisaster.value = _listDisaster.value?.filter { item -> item.disasterType == disasterType }
    }

    companion object {
        const val WEEK_TIME_PERIOD = 604800
        const val DAY_TIME_PERIOD = 86400
    }

}