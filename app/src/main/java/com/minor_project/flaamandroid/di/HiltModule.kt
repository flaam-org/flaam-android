package com.minor_project.flaamandroid.di

import android.content.Context
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.network.*
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
    fun provideAuthApi(
        @ApplicationContext context: Context
    ): AuthApi {
        return ApiBuilder(context).getFlaamApiForAuthRepo()
    }



    @Provides
    @Singleton
    fun providesAuthRepo(authApi: AuthApi) = AuthRepository(authApi)

    @Provides
    @Singleton
    fun provideFlaamApi(
        @ApplicationContext context: Context,
        userPreferences: UserPreferences,
        authRepo: AuthRepository
    ): FlaamApi = ApiBuilder(context).getFlaamApi(userPreferences, authRepo)







    @Provides
    @Singleton
    fun provideRepo(flaamApi: FlaamApi): FlaamRepository = FlaamRepository(flaamApi)


}