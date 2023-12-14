package com.example.taskn17.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskn17.data.Resource
import com.example.taskn17.data.data_store.UserDataPreferencesRepositoryImpl
import com.example.taskn17.domain.login.LoginRepository
import com.example.taskn17.domain.login.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVIewModel @Inject constructor(private val loginRepository: LoginRepository, private val userDataPreferencesRepositoryImpl: UserDataPreferencesRepositoryImpl) : ViewModel() {
    private val emailRegex = Regex("^\\S+@\\S+\\.\\S+$")
    private val _resourceFlow = MutableStateFlow<Resource<LoginResponse>>(
        Resource.Loading(false)
    )
    val resourceFlow: StateFlow<Resource<LoginResponse>> get() = _resourceFlow

    suspend fun onEvent(event: LoginEvent): Boolean {
        when(event) {
            is LoginEvent.CheckValidation -> checkIfValid(email = event.email, password = event.password)
            is LoginEvent.Login -> login(email = event.email, password = event.password)
            is LoginEvent.SaveSession -> saveSessionInDataStore(email = event.email)
        }
        return false
    }

    private fun checkIfValid(email: String, password: String) {
        _resourceFlow.value = Resource.Valid(emailRegex.matches(email) && password.trim().isNotEmpty())
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            loginRepository.login(email, password).collect {
                _resourceFlow.value = when (it) {
                    is Resource.Loading -> Resource.Loading(it.loading)
                    is Resource.Success -> Resource.Success(it.successData!!)
                    is Resource.Error -> Resource.Error(it.errorMessage)
                    else -> Resource.Valid(false)
                }
            }
        }
    }

    private suspend fun saveSessionInDataStore(email: String) {
        userDataPreferencesRepositoryImpl.saveUserData(email)
    }

}

sealed class LoginEvent {
    data class Login(val email: String, val password: String): LoginEvent()
    data class SaveSession(val email: String): LoginEvent()
    data class CheckValidation(val email: String, val password: String) : LoginEvent()
}