package com.example.taskn17.authentfication.login

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
import com.example.taskn17.authentfication.Resource
import com.example.taskn17.authentfication.login.api.LoginResponse
import com.example.taskn17.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val loginViewModel: LoginVIewModel by viewModels()
    var email: String = ""
    var password: String = ""

    override fun setUp() {
        setFragmentResultListener("credentialsRequest") { _, result ->
            email = result.getString("email").toString()
            password = result.getString("password").toString()
            binding.etLoginUsername.setText(email)
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.resourceFlow.collect { resource ->
                    handleResource(resource)
                }
            }
        }
    }

    private fun inputsWatchersListeners() {
        with(binding) {
            etLoginUsername.doOnTextChanged { _, _, _, _ -> validationToEnableButton() }
            etLoginPassword.doOnTextChanged { _, _, _, _ -> validationToEnableButton() }
        }
    }

    private fun validationToEnableButton() {
        with(binding.btnLogin) {
            isEnabled = loginViewModel.checkIfValid(
                binding.etLoginUsername.text.toString(),
                binding.etLoginPassword.text.toString()
            )
            if (isEnabled) setBackgroundResource(R.drawable.costume_btn_background)
            else setBackgroundResource(R.drawable.costume_btn_disabled_background)
        }
    }

    private fun onLoginClick() {
        inputsWatchersListeners()
        binding.btnLogin.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                loginViewModel.login(
                    binding.etLoginUsername.text.toString().trim(),
                    binding.etLoginPassword.text.toString().trim()
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
                    goToHomePage(
                        binding.etLoginUsername.text.toString().trim(),
                        resource.response.token,
                        binding.etLoginPassword.text.toString().trim()
                    )
                } else {
                    goToHomePage(binding.etLoginUsername.text.toString().trim(), "", "")
                }
            }

            is Resource.Error -> {
                Toast.makeText(requireContext(), resource.errorMessage, Toast.LENGTH_SHORT).show()
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