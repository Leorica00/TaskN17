package com.example.taskn17.data.login

import com.example.taskn17.domain.login.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginDto>
}