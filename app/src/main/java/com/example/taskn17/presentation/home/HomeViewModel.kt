package com.example.taskn17.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskn17.data.data_store.UserDataPreferencesRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDataPreferencesRepositoryImpl: UserDataPreferencesRepositoryImpl
): ViewModel() {
    private val _homeState = MutableStateFlow<HomeFragmentState>(HomeFragmentState.Pending)
    val homeState get() = _homeState.asStateFlow()

    fun onEvent(event: HomeFragmentEvents){
        when(event) {
            is HomeFragmentEvents.SetEmailText -> setEmailText(event.email)
            is HomeFragmentEvents.ClearUserData -> clearData()
        }
    }

    private fun setEmailText(email: String) {
        viewModelScope.launch {
            userDataPreferencesRepositoryImpl.isSessionActive().collect { isActive ->
                if (isActive) {
                    userDataPreferencesRepositoryImpl.getUserData().collect {
                        _homeState.value = HomeFragmentState.Data(it!!)
                    }
                } else {
                    _homeState.value = HomeFragmentState.Data(email)
                }
            }
        }
    }

    private fun clearData() {
        viewModelScope.launch {
            userDataPreferencesRepositoryImpl.clearUserData()
        }
    }
}

sealed class HomeFragmentState {
    data object Pending: HomeFragmentState()
    data class Data(val data: String): HomeFragmentState()
}

sealed class HomeFragmentEvents {
    data class SetEmailText(val email: String): HomeFragmentEvents()
    data object ClearUserData: HomeFragmentEvents()
}