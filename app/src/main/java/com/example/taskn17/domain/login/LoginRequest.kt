package com.example.taskn17.domain.login

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)