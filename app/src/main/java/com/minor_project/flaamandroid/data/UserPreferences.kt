package com.minor_project.flaamandroid.data

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private const val USER_PREFERENCE_NAME = "user_preferences"
class UserPreferences {


    private val Context.dataStore by preferencesDataStore(
        USER_PREFERENCE_NAME
    )

    suspend fun updateTokens(){}




    companion object {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val MAIL = stringPreferencesKey("mail")
        val USER_NAME = stringPreferencesKey("user_name")

    }


}