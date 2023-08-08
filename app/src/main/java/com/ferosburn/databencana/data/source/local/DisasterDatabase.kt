package com.ferosburn.databencana.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DisasterEntity::class], version = 1, exportSchema = false)
abstract class DisasterDatabase : RoomDatabase() {
    abstract val disasterDao: DisasterDao

    companion object {
        @Volatile
        private var INSTANCE: DisasterDatabase? = null
        fun getDatabase(context: Context): DisasterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DisasterDatabase::class.java,
                    "Disaster.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}