package com.example.page

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.page.Utils.Constants.TAG
import com.example.page.Utils.NetworkResult
import com.example.page.Utils.TokenManager
import com.example.page.databinding.FragmentRegisterBinding
import com.example.page.models.UserRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    @Inject
    lateinit var tokenManager : TokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        if(tokenManager.getToken()!= null){
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {

            val validateResult = validateUserInput()
            if(validateResult.first){
                authViewModel.registerUser(getUserRequest())

            }
            else{
                binding.txtError.text  = validateResult.second
            }

            Log.d(TAG, "API call triggered")

        }
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        bindObservers()

    }
    private fun getUserRequest():UserRequest{
        val username = binding.txtUsername.text.toString().trim()
        val email = binding.txtEmail.text.toString().trim()
        val password = binding.txtPassword.text.toString().trim()
        return UserRequest(email,password,username)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(userRequest.username,userRequest.password,userRequest.email,false)

    }

    private fun bindObservers() {
        authViewModel.userResponseLiveDate.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it){
                is NetworkResult.Success -> {
                    // token
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)

                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.massage
                    Log.d(TAG,it.massage.toString())
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true

                }

            }
        })
    }


}