package com.ferosburn.databencana.data.source.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val reportService: ReportService) {
    suspend fun getReports (startTime: String, endTime: String, provinceCode: String? = null): Flow<ReportResponse<List<GeometryReport>>> {
        return flow {
            try {
                val response = reportService.getReports(startTime, endTime, provinceCode)
                val dataArray = response.result?.objectReport?.output?.geometries
                if (!dataArray.isNullOrEmpty()) {
                    emit(ReportResponse.Success(dataArray))
                } else {
                    emit(ReportResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ReportResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getRecentReports (timePeriod: Int, provinceCode: String? = null, disasterValue: String? = null): Flow<ReportResponse<List<GeometryReport>>> {
        return flow {
            try {
                val response = reportService.getRecentReports(timePeriod, provinceCode, disasterValue)
                val dataArray = response.result?.objectReport?.output?.geometries
                if (!dataArray.isNullOrEmpty()) {
                    emit(ReportResponse.Success(dataArray))
                } else {
                    emit(ReportResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ReportResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}