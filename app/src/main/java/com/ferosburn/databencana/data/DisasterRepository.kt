package com.ferosburn.databencana.data

import com.ferosburn.databencana.data.source.local.LocalDataSource
import com.ferosburn.databencana.data.source.remote.GeometryReport
import com.ferosburn.databencana.data.source.remote.RemoteDataSource
import com.ferosburn.databencana.data.source.remote.ReportResponse
import com.ferosburn.databencana.domain.model.DisasterModel
import com.ferosburn.databencana.domain.repository.IDisasterRepository
import com.ferosburn.databencana.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DisasterRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IDisasterRepository {

    override fun getAllDisaster(
        timePeriod: Int,
        provinceCode: String?,
        disasterValue: String?
    ): Flow<Resource<List<DisasterModel>>> =
        object : NetworkBoundResource<List<DisasterModel>, List<GeometryReport>>() {
            override fun loadFromDB(): Flow<List<DisasterModel>> =
                localDataSource.getAllDisaster().map {
                    DataMapper.mapEntitiesToDomain(it)
                }

            override suspend fun createCall(): Flow<ReportResponse<List<GeometryReport>>> =
                remoteDataSource.getRecentReports(timePeriod, provinceCode, disasterValue)

            override suspend fun saveCallResult(data: List<GeometryReport>) =
                localDataSource.insertDisaster(DataMapper.mapResponseToEntities(data))

            override fun shouldFetch(data: List<DisasterModel>?): Boolean = true

        }.asFlow()
}