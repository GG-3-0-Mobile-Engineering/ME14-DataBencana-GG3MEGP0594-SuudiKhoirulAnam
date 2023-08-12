package com.ferosburn.databencana.domain.repository

import com.ferosburn.databencana.data.Resource
import com.ferosburn.databencana.domain.model.DisasterModel
import kotlinx.coroutines.flow.Flow

interface IDisasterRepository {
    fun getAllDisaster(
        timePeriod: Int,
        provinceCode: String?,
        disasterValue: String?
    ) : Flow<Resource<List<DisasterModel>>>
}