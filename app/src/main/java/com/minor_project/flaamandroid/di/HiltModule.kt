package com.minor_project.flaamandroid.di

import com.minor_project.flaamandroid.network.ApiBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class HiltModule {


    @Provides
    @Singleton
    fun provideApi() = ApiBuilder().getFlaamApi()


}