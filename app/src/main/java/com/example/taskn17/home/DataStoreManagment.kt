package com.example.taskn17.home

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.taskn17.authentfication.login.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


object DataStoreManager {
    private val USER_DATA = stringPreferencesKey("user_data")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val userAdapter = moshi.adapter(User::class.java)

    fun getUserData(context: Context): Flow<User?> {
        return context.dataStore.data
            .map { preferences ->
                val userDataString = preferences[USER_DATA]
                if (userDataString != null) {
                    userAdapter.fromJson(userDataString)
                } else {
                    null
                }
            }
    }

    suspend fun saveUserData(context: Context, user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_DATA] = userAdapter.toJson(user)
        }
    }

    suspend fun clearUserData(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_DATA)
        }
    }

    fun isSessionActive(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences.contains(USER_DATA)
        }
    }
}

