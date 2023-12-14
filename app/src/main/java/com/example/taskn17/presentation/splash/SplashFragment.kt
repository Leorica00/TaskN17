package com.example.taskn17.presentation.splash

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.taskn17.BaseFragment
import com.example.taskn17.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun setUp() {
        splashViewModel.readSession()
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.sessionFlow.collect {
                    handleResource(it)
                }
            }
        }
    }

    private fun handleResource(resource: DataStoreState) {
        when (resource) {
            is DataStoreState.Pending -> {}
            is DataStoreState.State -> goToSpecifiedFragment(resource.boolean)
        }
    }

    private fun goToSpecifiedFragment(session: Boolean) {
        if (session)
            findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToHomeFragment()
            )
        else
            findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            )
    }

}