package com.ferosburn.databencana.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DisasterEntity::class], version = 1, exportSchema = false)
abstract class DisasterDatabase : RoomDatabase() {
    abstract fun disasterDao(): DisasterDao
}