package com.example.taskn17.data.register

import com.example.taskn17.domain.register.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterDto>
}