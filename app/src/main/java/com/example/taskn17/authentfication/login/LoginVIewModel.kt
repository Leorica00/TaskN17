package com.example.taskn17.authentfication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskn17.AppError
import com.example.taskn17.authentfication.Resource
import com.example.taskn17.authentfication.RetrofitClient
import com.example.taskn17.authentfication.login.api.LoginRequest
import com.example.taskn17.authentfication.login.api.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginVIewModel : ViewModel() {
    private val emailRegex = Regex("^\\S+@\\S+\\.\\S+$")
    private val _resourceFlow = MutableStateFlow<Resource<LoginResponse>>(
        Resource.Loading(false)
    )
    val resourceFlow: StateFlow<Resource<LoginResponse>> get() = _resourceFlow

    fun checkIfValid(email: String, password: String): Boolean {
        return emailRegex.matches(email) && password.trim().isNotEmpty()
    }

    suspend fun login(email: String, password: String) {
        viewModelScope.launch {
            _resourceFlow.value = Resource.Loading(true)
            try {
                val response = RetrofitClient.loginService().login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _resourceFlow.value = response.body()?.let {
                        Resource.Success(LoginResponse(it.token))
                    }!!
                } else {
                    throw HttpException(response)
                }
            } catch (e: Exception) {
                val error: AppError = AppError.fromException(e)
                _resourceFlow.value = Resource.Error(message = error.message)
            } finally {
                _resourceFlow.value = Resource.Loading(false)
            }
        }
    }
}