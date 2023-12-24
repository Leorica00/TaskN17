package com.example.taskn17.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskn17.data.Resource
import com.example.taskn17.domain.register.RegisterRepository
import com.example.taskn17.domain.register.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository) :
    ViewModel() {
    private val emailRegex = Regex("^\\S+@\\S+\\.\\S+$")
    private val _resourceFlow = MutableStateFlow<Resource<RegisterResponse>>(
        Resource.Loading(false)
    )
    val resourceFlow: StateFlow<Resource<RegisterResponse>> get() = _resourceFlow

    suspend fun onEvent(event: RegisterEvent): Boolean {
        when(event) {
            is RegisterEvent.Register -> register(email = event.email, password = event.password)
            is RegisterEvent.CheckValidation -> checkIfValid(email = event.email, password = event.password, repeatPassword = event.repeatPassword)
        }
        return false
    }

    private fun checkIfValid(email: String, password: String, repeatPassword: String) {
        _resourceFlow.value =  Resource.Valid(emailRegex.matches(email) && password.trim().isNotEmpty() && (password == repeatPassword))
    }

    suspend fun register(email: String, password: String) {
        viewModelScope.launch {
            registerRepository.register(email, password).collect {
                _resourceFlow.value = when (it) {
                    is Resource.Loading -> Resource.Loading(loading = it.loading)
                    is Resource.Success -> Resource.Success(response = it.response)
                    is Resource.Error -> Resource.Error(message = it.errorMessage)
                    is Resource.Valid -> Resource.Valid(false)
                }
            }
        }
    }
}

sealed class RegisterEvent {
    data class CheckValidation(val email: String, val password: String, val repeatPassword: String): RegisterEvent()
    data class Register(val email: String, val password: String): RegisterEvent()
}