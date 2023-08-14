package com.ferosburn.databencana.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ferosburn.databencana.domain.usecase.DisasterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val disasterUseCase: DisasterUseCase): ViewModel() {

    fun getRecentReports(
        timePeriod: Int,
        provinceCode: String?,
        disasterValue: String?
    ) = disasterUseCase.getRecentReports(timePeriod, provinceCode, disasterValue).asLiveData()

    fun getReports(
        startTime: String,
        endTime: String,
        provinceCode: String?
    ) = disasterUseCase.getReports(startTime, endTime, provinceCode).asLiveData()
}