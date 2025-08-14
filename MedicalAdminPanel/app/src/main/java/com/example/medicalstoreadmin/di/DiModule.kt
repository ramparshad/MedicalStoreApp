package com.example.medicalstoreadmin.di

import com.example.medicalstoreadmin.repo.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideRepository() = Repo()
}

