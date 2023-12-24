package com.example.taskn17.domain.register

import com.example.taskn17.data.Resource
import kotlinx.coroutines.flow.Flow

interface RegisterRepository {
    suspend fun register(email: String, password: String): Flow<Resource<RegisterResponse>>
}