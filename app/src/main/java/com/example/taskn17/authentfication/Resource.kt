package com.example.taskn17.authentfication

sealed class Resource<T>(
    val isLoading: Boolean = false,
    val successData: T? = null,
    val errorMessage: String = ""
) {
    data class Loading<T>(val loading: Boolean) : Resource<T>(isLoading = loading)
    data class Success<T>(val response: T) :
        Resource<T>(successData = response)
    sealed class Error<T>(eMessage: String): Resource<T>(errorMessage = eMessage) {
        data class NetworkError<T>(val message: String): Error<T>(eMessage = message)
        data class HttpError<T>(val message: String): Error<T>(eMessage = message)
        data class TimeoutError<T>(val message: String): Error<T>(eMessage = message)
        data class ServerError<T>(val message: String): Error<T>(eMessage = message)
        data class HostResolutionError<T>(val message: String): Error<T>(eMessage = message)
        data class ClientError<T>(val message: String): Error<T>(eMessage = message)
    }
}