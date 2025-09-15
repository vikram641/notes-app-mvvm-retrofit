package com.example.page

import android.text.TextUtils
import android.util.Patterns
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.page.Utils.NetworkResult
import com.example.page.Utils.TokenManager
import com.example.page.models.UserRequest
import com.example.page.models.UserResponce
import com.example.page.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository, private val tokenManager: TokenManager): ViewModel() {

     val userResponseLiveDate : LiveData<NetworkResult<UserResponce>>
    get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)

        }

    }
    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)

        }
    }
    fun logout() {
        tokenManager.clearSession()
    }


    fun validateCredentials (username : String, password: String,email : String , isLogin:Boolean): Pair<Boolean,String> {
        var result = Pair(true,"")
        if((!isLogin && TextUtils.isEmpty(username)) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)){
            result = Pair(false, "please provide Credentials")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false, "Please provide valid email")

        }
        else if(password.length <= 5 ){
            result = Pair(false, "Please length should be greatar then 5")
        }
        return result


    }



}