package com.minor_project.flaamandroid.di

import android.content.Context
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.network.ApiBuilder
import com.minor_project.flaamandroid.network.FlaamApi
import com.minor_project.flaamandroid.network.FlaamRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class HiltModule {



    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context) = UserPreferences(context)


    @Provides
    @Singleton
    fun provideApi(
        @ApplicationContext context: Context,
        userPreferences: UserPreferences
    ): FlaamApi {
        return runBlocking {
            ApiBuilder(context).getFlaamApi(userPreferences)
        }
    }

    @Provides
    @Singleton
    fun provideRepo(flaamApi: FlaamApi): FlaamRepository = FlaamRepository(flaamApi)


}