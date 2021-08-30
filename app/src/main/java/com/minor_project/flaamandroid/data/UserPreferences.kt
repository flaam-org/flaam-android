package com.minor_project.flaamandroid.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.minor_project.flaamandroid.data.request.LoginRequest
import com.minor_project.flaamandroid.data.response.LoginResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val USER_PREFERENCE_NAME = "user_preferences"
class UserPreferences(private val context: Context) {


    private val Context.dataStore by preferencesDataStore(
        USER_PREFERENCE_NAME
    )

    suspend fun updateTokens(tokens: LoginResponse){
        context.dataStore.edit {
            tokens.apply {
                access?.let { str ->
                    it[ACCESS_TOKEN] = str
                }
                refresh?.let { str ->
                    it[REFRESH_TOKEN] = str
                }
            }
        }
    }

    suspend fun registerUser(registerUser: ViewProfileResponse){
        context.dataStore.edit {
        registerUser.apply {
            it[FIRST_NAME] = firstName.toString()
            it[LAST_NAME] = lastName.toString()
            it[MAIL] = email.toString()
            it[USER_NAME] = username.toString()
        }
        }
    }

    val accessToken: Flow<String?> = context.dataStore.data.map{ it[ACCESS_TOKEN] }

    val refreshToken: Flow<String?> = context.dataStore.data.map{ it[REFRESH_TOKEN]}

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("auth_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val MAIL = stringPreferencesKey("mail")
        val USER_NAME = stringPreferencesKey("user_name")

    }


}