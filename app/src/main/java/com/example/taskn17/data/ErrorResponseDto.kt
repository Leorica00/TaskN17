package com.example.taskn17.data

import com.squareup.moshi.Json

data class ErrorResponseDto(
    @Json(name = "error") val error: String
)