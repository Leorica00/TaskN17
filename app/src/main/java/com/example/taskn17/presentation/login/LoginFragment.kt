package com.example.taskn17.presentation.login

import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.taskn17.BaseFragment
import com.example.taskn17.R
import com.example.taskn17.data.Resource
import com.example.taskn17.databinding.FragmentLoginBinding
import com.example.taskn17.domain.login.LoginResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val loginViewModel: LoginVIewModel by viewModels()

    override fun setUp() {
        setFragmentResultListener("credentialsRequest") { _, result ->
            val email = result.getString("email") ?: ""
            val password = result.getString("password") ?: ""
            binding.etLoginEmail.setText(email)
            binding.etLoginPassword.setText(password)
        }
        with(binding) {
            btnLogin.isEnabled = false
            btnLogin.setBackgroundResource(R.drawable.costume_btn_disabled_background)
        }
    }

    override fun setUpListeners() {
        onLoginClick()
        binding.btnGoToRegister.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                loginViewModel.resourceFlow.collect { resource ->
                    handleResource(resource)
                }
            }
        }
    }

    private fun inputsWatchersListeners() {
        with(binding) {
            etLoginEmail.doOnTextChanged { _, _, _, _ -> validationToEnableButton() }
            etLoginPassword.doOnTextChanged { _, _, _, _ -> validationToEnableButton() }
        }
    }

    private fun validationToEnableButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(binding) {
                    loginViewModel.onEvent(
                        LoginEvent.CheckValidation(
                            etLoginEmail.text.toString(),
                            etLoginPassword.text.toString()
                        )
                    )
                }
            }
        }
    }

    private fun onLoginClick() {
        inputsWatchersListeners()
        binding.btnLogin.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                loginViewModel.onEvent(
                    with(binding) {
                        LoginEvent.Login(
                            etLoginEmail.text.toString().trim(),
                            etLoginPassword.text.toString().trim()
                        )
                    }
                )
            }
        }
    }

    private fun handleResource(resource: Resource<LoginResponse>) {
        when (resource) {
            is Resource.Loading -> {
                if (resource.isLoading) {
                    binding.progressBarLogin.visibility = View.VISIBLE
                } else {
                    binding.progressBarLogin.visibility = View.INVISIBLE
                }
            }

            is Resource.Success -> {
                if (binding.checkboxRememberMe.isChecked) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        with(binding) {
                            loginViewModel.onEvent(
                                LoginEvent.SaveSession(etLoginEmail.text.toString().trim())
                            )
                            goToHomePage(
                                etLoginEmail.text.toString().trim(),
                                resource.response.token,
                                etLoginPassword.text.toString().trim()
                            )
                        }
                       
                    }
                } else {
                    goToHomePage(binding.etLoginEmail.text.toString().trim(), "", "")
                }
            }

            is Resource.Error -> {
                Toast.makeText(requireContext(), resource.errorMessage, Toast.LENGTH_SHORT).show()
            }

            is Resource.Valid -> {
                with(binding.btnLogin) {
                    isEnabled = resource.isValid
                    if (isEnabled) setBackgroundResource(R.drawable.costume_btn_background)
                    else setBackgroundResource(R.drawable.costume_btn_disabled_background)
                }
            }
        }
    }

    private fun goToHomePage(email: String, token: String, password: String) {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                email = email,
                token = token,
                password = password
            )
        )
    }
}