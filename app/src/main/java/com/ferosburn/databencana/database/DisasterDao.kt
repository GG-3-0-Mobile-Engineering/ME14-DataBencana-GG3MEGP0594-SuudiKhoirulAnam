package com.ferosburn.databencana.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DisasterDao {
    @Query("SELECT * FROM disaster")
    fun getAllDisaster(): LiveData<List<DisasterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDisaster(disaster: List<DisasterEntity>)
}

