package com.example.taskn17.domain.register

import com.squareup.moshi.Json

data class RegisterRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)
