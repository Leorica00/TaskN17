package com.example.taskn17.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

sealed class AppError(open val message: String) {
    data class NetworkError(override val message: String) : AppError(message)
    data class HttpError(override val message: String) : AppError(message)
    data class TimeoutError(override val message: String) : AppError(message)
    data class ServerError(override val message: String) : AppError(message)
    data class ClientError(override val message: String) : AppError(message)
    data class UnknownError(override val message: String) : AppError(message)

    companion object {
        fun fromException(exception: Exception): AppError {
            return when (exception) {
                is IOException -> NetworkError("Network error occurred: No Internet")
                is HttpException -> {
                    when (exception.code()) {
                        in 400..499 -> {
                            val adapter: JsonAdapter<ErrorResponseDto> = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(
                                ErrorResponseDto::class.java)
                            val errorModel = exception.response()?.errorBody()?.string()?.let { adapter.fromJson(it) }
                            ClientError(errorModel?.error.toString())
                        }
                        in 500..599 -> ServerError("Server error occurred")
                        else -> HttpError("Http error occurred")
                    }
                }
                is TimeoutException -> TimeoutError("Can not process task")
                else -> UnknownError("An unexpected error occurred")
            }
        }
    }
}