package com.example.taskn17.domain.data_store

import kotlinx.coroutines.flow.Flow

interface UserDataPreferencesRepository {
    suspend fun saveUserData(email: String)
    suspend fun getUserData(): Flow<String?>
    suspend fun clearUserData()
    suspend fun isSessionActive(): Flow<Boolean>
}