package com.example.taskn17.authentfication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskn17.authentfication.Resource
import com.example.taskn17.authentfication.RetrofitClient
import com.example.taskn17.authentfication.login.api.LoginRequest
import com.example.taskn17.authentfication.login.api.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class LoginVIewModel : ViewModel() {
    private val _resourceFlow = MutableStateFlow<Resource<LoginResponse>>(
        Resource.Loading(false)
    )
    val resourceFlow: StateFlow<Resource<LoginResponse>> get() = _resourceFlow

    suspend fun login(email: String, password: String) {
        viewModelScope.launch {
            _resourceFlow.value = Resource.Loading(true)
            try {
                val response = RetrofitClient.loginService().login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _resourceFlow.value = response.body()?.let {
                        Resource.Success(LoginResponse(it.token))
                    }!!
                } else if (response.code() in 400..499) {
                    _resourceFlow.value = Resource.Error.ClientError(
                        "${response.code()} response.errorBody()?.string() "
                    )
                } else {
                    _resourceFlow.value =
                        Resource.Error.ServerError("Server Error: ${response.code()}")
                }
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException -> _resourceFlow.value =
                        Resource.Error.HostResolutionError("${e.message}")

                    is IOException -> _resourceFlow.value =
                        Resource.Error.NetworkError("Network Error")

                    is TimeoutException -> _resourceFlow.value =
                        Resource.Error.TimeoutError("${e.message}")

                    is HttpException -> _resourceFlow.value =
                        Resource.Error.HttpError("${e.response()}")
                }
            } finally {
                _resourceFlow.value = Resource.Loading(false)
            }
        }
    }
}