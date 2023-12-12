package com.example.taskn17.authentfication.register

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
import com.example.taskn17.authentfication.Resource
import com.example.taskn17.databinding.FragmentRegisterBinding
import com.example.taskn17.authentfication.register.api.RegisterResponse
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    private val emailRegex = Regex("^\\S+@\\S+\\.\\S+$")
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun setUp() {
        buttonEnableValidation()
    }

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
            etRegisterEmail.doOnTextChanged { _, _, _, _ -> buttonEnableValidation() }
            etRegisterPassword.doOnTextChanged { _, _, _, _ ->  buttonEnableValidation()}
        }
    }

    private fun buttonEnableValidation() {
        binding.btnLogin.apply {
            isEnabled = emailRegex.matches(binding.etRegisterEmail.text.toString().trim()) && binding.etRegisterPassword.text.toString().trim().isNotEmpty() && (binding.etRegisterPassword.text.toString() == binding.etRegisterRepeatPassword.text.toString())
            if (isEnabled) setBackgroundResource(R.drawable.costume_btn_background)
            else setBackgroundResource(R.drawable.costume_btn_disabled_background)
        }
    }


    private fun onRegisterClick() {
        inputsWatchersListeners()
        binding.btnLogin.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                registerViewModel.register(binding.etRegisterEmail.text.toString().trim(), binding.etRegisterPassword.text.toString().trim())
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
                Toast.makeText(requireContext(), resource.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}