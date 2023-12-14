package com.example.taskn17.authentfication

import com.squareup.moshi.Json

data class ErrorResponse(
    @Json(name = "error") val error: String
)
