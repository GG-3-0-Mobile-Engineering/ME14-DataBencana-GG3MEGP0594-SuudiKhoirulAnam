package com.ferosburn.databencana.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DisasterDao {
    @Query("SELECT * FROM disaster")
    fun getAllDisaster(): Flow<List<DisasterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDisaster(disasterList: List<DisasterEntity>)
}

