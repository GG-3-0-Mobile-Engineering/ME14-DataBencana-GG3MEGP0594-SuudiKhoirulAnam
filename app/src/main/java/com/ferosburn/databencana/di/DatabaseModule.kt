package com.ferosburn.databencana.di

import android.content.Context
import androidx.room.Room
import com.ferosburn.databencana.data.source.local.DisasterDao
import com.ferosburn.databencana.data.source.local.DisasterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): DisasterDatabase =
        Room.databaseBuilder(
            context,
            DisasterDatabase::class.java,
            "Disaster.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideDisasterDao(database: DisasterDatabase): DisasterDao = database.disasterDao()
}