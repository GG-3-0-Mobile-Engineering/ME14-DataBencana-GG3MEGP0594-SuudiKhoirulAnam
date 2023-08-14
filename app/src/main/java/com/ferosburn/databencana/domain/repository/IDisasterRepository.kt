package com.ferosburn.databencana.domain.repository

import com.ferosburn.databencana.data.Resource
import com.ferosburn.databencana.domain.model.DisasterModel
import kotlinx.coroutines.flow.Flow

interface IDisasterRepository {
    fun getRecentReports(
        timePeriod: Int,
        provinceCode: String?,
        disasterValue: String?
    ) : Flow<Resource<List<DisasterModel>>>

    fun getReports(
        startTime: String,
        endTime: String,
        provinceCode: String?
    ) : Flow<Resource<List<DisasterModel>>>
}