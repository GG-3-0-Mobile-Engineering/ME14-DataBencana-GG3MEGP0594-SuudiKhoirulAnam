package com.ferosburn.databencana.domain.usecase

import com.ferosburn.databencana.data.Resource
import com.ferosburn.databencana.domain.model.DisasterModel
import com.ferosburn.databencana.domain.repository.IDisasterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DisasterInteractor @Inject constructor(private val disasterRepository: IDisasterRepository): DisasterUseCase {
    override fun getAllDisaster(
        timePeriod: Int,
        provinceCode: String?,
        disasterValue: String?
    ): Flow<Resource<List<DisasterModel>>> = disasterRepository.getAllDisaster(timePeriod, provinceCode, disasterValue)

}