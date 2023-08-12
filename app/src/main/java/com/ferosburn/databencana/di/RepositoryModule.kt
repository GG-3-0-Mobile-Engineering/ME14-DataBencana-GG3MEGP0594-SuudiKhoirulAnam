package com.ferosburn.databencana.di

import com.ferosburn.databencana.data.DisasterRepository
import com.ferosburn.databencana.domain.repository.IDisasterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepository(disasterRepository: DisasterRepository): IDisasterRepository
}