package com.minor_project.flaamandroid.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.minor_project.flaamandroid.data.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val USER_PREFERENCE_NAME = "user_preferences"
class UserPreferences(private val context: Context) {


    private val Context.dataStore by preferencesDataStore(
        USER_PREFERENCE_NAME
    )

    suspend fun updateTokens(tokens: LoginResponse){
        context.dataStore.edit {
            it[ACCESS_TOKEN] = tokens.access.toString()
            it[REFRESH_TOKEN] = tokens.access.toString()
        }
    }

    suspend fun getToken(): Flow<String?> = context.dataStore.data.map{ it[ACCESS_TOKEN] }


    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("auth_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val MAIL = stringPreferencesKey("mail")
        val USER_NAME = stringPreferencesKey("user_name")

    }


}