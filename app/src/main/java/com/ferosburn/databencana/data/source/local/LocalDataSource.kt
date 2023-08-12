package com.ferosburn.databencana.data.source.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val disasterDao: DisasterDao) {

    fun getAllDisaster(): Flow<List<DisasterEntity>> = disasterDao.getAllDisaster()

    suspend fun insertDisaster(disasterList: List<DisasterEntity>) = disasterDao.insertDisaster(disasterList)

}