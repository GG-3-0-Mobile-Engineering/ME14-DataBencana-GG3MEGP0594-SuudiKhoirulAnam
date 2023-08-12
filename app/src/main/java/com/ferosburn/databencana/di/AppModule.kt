package com.ferosburn.databencana.di

import com.ferosburn.databencana.domain.usecase.DisasterInteractor
import com.ferosburn.databencana.domain.usecase.DisasterUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideDisasterUseCase(disasterInteractor: DisasterInteractor): DisasterUseCase
}