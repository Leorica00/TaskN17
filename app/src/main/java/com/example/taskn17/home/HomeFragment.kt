package com.example.taskn17.home

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskn17.BaseFragment
import com.example.taskn17.authentfication.DataStoreManager
import com.example.taskn17.authentfication.login.User
import com.example.taskn17.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val args: HomeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (args.token.isNotEmpty()) lifecycleScope.launch {
            val user = User(args.token, args.email, args.password)
            DataStoreManager.saveUserData(user)
        }
    }

    override fun setUp() {
        viewLifecycleOwner.lifecycleScope.launch {
            DataStoreManager.isSessionActive().collect { isActive ->
                if (isActive) {
                    DataStoreManager.getUserData().collect {
                        binding.tvEmailHome.text = it?.email
                    }
                } else {
                    binding.tvEmailHome.text = args.email
                }
            }
        }
    }

    override fun setUpListeners() {
        binding.btnLogout.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                with(DataStoreManager) {
                    clearUserData()
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                    )
                }
            }
        }
    }
}