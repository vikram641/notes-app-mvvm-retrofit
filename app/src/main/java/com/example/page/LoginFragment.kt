package com.example.page

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.page.Utils.Constants.TAG
import com.example.page.Utils.NetworkResult
import com.example.page.Utils.TokenManager
import com.example.page.databinding.FragmentLoginBinding
import com.example.page.databinding.FragmentRegisterBinding
import com.example.page.models.UserRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    @Inject
    lateinit var tokenManager: TokenManager




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        if(tokenManager.getToken()!= null){
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener {
            val validateResult = validateUserInput()
            if(validateResult.first){
                authViewModel.loginUser(getUserRequest())

            }
            else{
                binding.txtError1.text  = validateResult.second
            }

            Log.d(TAG, "API call triggered")
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }

        bindObserver()

    }
    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(userRequest.username,userRequest.password,userRequest.email,true)
    }


    private fun getUserRequest(): UserRequest{
//        val username = "dgd"
        val email = binding.txtEmail1.text.toString().trim()
        val password = binding.txtPassword1.text.toString().trim()
        return  UserRequest(email,password,"")
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveDate.observe(viewLifecycleOwner, Observer {
            binding.progressBar1.isVisible = false
            when (it){
                is NetworkResult.Success -> {
                    // token
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)

                }
                is NetworkResult.Error -> {
                    binding.txtError1.text = it.massage
                    Log.d(TAG,it.massage.toString())
                }
                is NetworkResult.Loading -> {
                    binding.progressBar1.isVisible = true

                }

            }
        })
    }


}