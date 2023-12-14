package com.example.taskn17.presentation.register

import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.taskn17.BaseFragment
import com.example.taskn17.R
import com.example.taskn17.data.Resource
import com.example.taskn17.databinding.FragmentRegisterBinding
import com.example.taskn17.domain.register.RegisterResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun setUpListeners() {
        onRegisterClick()
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerViewModel.resourceFlow.collect {
                    handleResource(it)
                }
            }
        }
    }

    private fun inputsWatchersListeners() {
        with(binding) {
            etRegisterEmail.doOnTextChanged { _, _, _, _ -> validationToEnableButton() }
            etRegisterPassword.doOnTextChanged { _, _, _, _ -> validationToEnableButton() }
            etRegisterRepeatPassword.doOnTextChanged { _, _, _, _ -> validationToEnableButton() }
        }
    }

    private fun validationToEnableButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(binding.btnLogin) {
                    isEnabled = registerViewModel.onEvent(
                        RegisterEvent.CheckValidation(
                            binding.etRegisterEmail.text.toString(),
                            binding.etRegisterPassword.text.toString(),
                            binding.etRegisterRepeatPassword.text.toString()
                        )
                    )
                }
            }
        }
    }

    private fun onRegisterClick() {
        inputsWatchersListeners()
        binding.btnLogin.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                registerViewModel.register(
                    binding.etRegisterEmail.text.toString().trim(),
                    binding.etRegisterPassword.text.toString().trim()
                )
            }
        }
    }

    private fun handleResource(resource: Resource<RegisterResponse>) {
        when (resource) {
            is Resource.Loading -> {
                if (resource.isLoading) {
                    binding.progressBarLogin.visibility = View.VISIBLE
                } else {
                    binding.progressBarLogin.visibility = View.INVISIBLE
                }
            }

            is Resource.Success -> {
                setFragmentResult(
                    "credentialsRequest", bundleOf(
                        "email" to (binding.etRegisterEmail.text.toString().trim()),
                        "password" to (binding.etRegisterPassword.text.toString().trim())
                    )
                )
                findNavController().navigateUp()
            }

            is Resource.Error -> {
                Toast.makeText(requireContext(), resource.errorMessage, Toast.LENGTH_SHORT)
                    .show()
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
}