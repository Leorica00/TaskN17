package com.example.taskn17.data.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.taskn17.domain.data_store.UserDataPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataPreferencesRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: DataStore<Preferences>
) : UserDataPreferencesRepository {

    companion object {
        val USER_DATA = stringPreferencesKey("user_data")
    }


    override suspend fun saveUserData(email: String) {
        userDataStorePreferences.edit { preferences ->
            preferences[USER_DATA] = email
        }
    }

    override suspend fun getUserData(): Flow<String?> {
        return userDataStorePreferences.data.map { preferences ->
            preferences[USER_DATA]
        }
    }

    override suspend fun clearUserData() {
        userDataStorePreferences.edit { preferences ->
            preferences.remove(USER_DATA)
        }
    }

    override suspend fun isSessionActive(): Flow<Boolean> {
        return userDataStorePreferences.data.map { preferences ->
            preferences.contains(USER_DATA)
        }
    }


}

