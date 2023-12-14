package com.example.taskn17.presentation.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskn17.BaseFragment
import com.example.taskn17.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val args: HomeFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by viewModels()
    override fun setUp() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.onEvent(HomeFragmentEvents.SetEmailText(args.email))
        }
    }

    override fun setUpListeners() {
        binding.btnLogout.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                homeViewModel.onEvent(HomeFragmentEvents.ClearUserData)
            }
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            )
        }
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.homeState.collect {
                    handleState(it)
                }
            }
        }
    }

    private fun handleState(state: HomeFragmentState) {
        when(state) {
            is HomeFragmentState.Pending -> {}
            is HomeFragmentState.Data -> binding.tvEmailHome.text = state.data
        }

    }
}