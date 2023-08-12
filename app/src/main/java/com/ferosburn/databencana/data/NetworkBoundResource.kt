package com.ferosburn.databencana.data

import com.ferosburn.databencana.data.source.remote.ReportResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

abstract class NetworkBoundResource<ResultType, RequestType> {
    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        val dbSource = loadFromDB().first()
        if (shouldFetch(dbSource)) {
            emit(Resource.Loading())
            when (val reportResponse = createCall().first()) {
                is ReportResponse.Success -> {
                    saveCallResult(reportResponse.data)
                    emitAll(loadFromDB().map { Resource.Success(it) })
                }
                is ReportResponse.Empty -> {
                    emitAll(loadFromDB().map { Resource.Success(it) })
                }
                is ReportResponse.Error -> {
                    onFetchFailed()
                    emit(Resource.Error<ResultType>(reportResponse.errorMessage))
                }
            }
        } else {
            emitAll(loadFromDB().map { Resource.Success(it) })
        }
    }
    protected open fun onFetchFailed() {}

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Flow<ReportResponse<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow() : Flow<Resource<ResultType>> = result
}