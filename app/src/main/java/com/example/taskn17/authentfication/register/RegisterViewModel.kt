package com.example.taskn17.authentfication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskn17.AppError
import com.example.taskn17.authentfication.Resource
import com.example.taskn17.authentfication.RetrofitClient
import com.example.taskn17.authentfication.register.api.RegisterRequest
import com.example.taskn17.authentfication.register.api.RegisterResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {
    private val emailRegex = Regex("^\\S+@\\S+\\.\\S+$")
    private val _resourceFlow = MutableStateFlow<Resource<RegisterResponse>>(
        Resource.Loading(false)
    )
    val resourceFlow: StateFlow<Resource<RegisterResponse>> get() = _resourceFlow

    fun checkIfValid(email: String, password: String, repeatPassword: String): Boolean {
        return emailRegex.matches(email) && password.trim().isNotEmpty() && (password == repeatPassword)
    }

    suspend fun register(email: String, password: String) {
        viewModelScope.launch {
            _resourceFlow.value = Resource.Loading(true)
            try {
                val response =
                    RetrofitClient.registerService().register(RegisterRequest(email, password))
                if (response.isSuccessful) {
                    _resourceFlow.value = response.body()?.let {
                        Resource.Success(
                            RegisterResponse(it.id, it.token)
                        )
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