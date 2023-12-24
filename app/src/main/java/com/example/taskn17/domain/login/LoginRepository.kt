package com.example.taskn17.domain.login

import com.example.taskn17.data.Resource
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(email: String, password: String): Flow<Resource<LoginResponse>>
}