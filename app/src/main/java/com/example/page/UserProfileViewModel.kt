package com.example.page

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.page.Utils.Constants.TAG
import com.example.page.Utils.NetworkResult
import com.example.page.models.UpdateUserdeatilsRequest
import com.example.page.models.UserResponce
import com.example.page.models.updateProfileResponse
import com.example.page.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {


    val userProfileLiveData get() = userRepository.userProfileLiveData
    val updateUserProfileLiveData get() = userRepository.updateUserProfileLiveData
    private val _imgUrl = MutableLiveData<String?>()
    val imgUrl: LiveData<String?> get() = _imgUrl

    init {
        // ✅ automatically observe hoga jab ViewModel banega
        observeProfileUpdateResponse()
    }



    fun uploadProfileImage(profile: MultipartBody.Part) {
        viewModelScope.launch {
            userRepository.profileUpdate(profile)
        }
    }

    fun saveProfileChanges(updateUserdeatilsRequest: UpdateUserdeatilsRequest){
        viewModelScope.launch {
            userRepository.UserdeatilsUpdate(updateUserdeatilsRequest)
        }
    }


    fun validateCredentials (email : String, username: String,img_url : String , isLogin:Boolean): Pair<Boolean,String> {
        var result = Pair(true,"")
        if((!isLogin && TextUtils.isEmpty(email)) || TextUtils.isEmpty(username) || TextUtils.isEmpty(img_url)){
            result = Pair(false, "please provide Credentials")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false, "Please provide valid email")

        }

        return result


    }
    fun observeProfileUpdateResponse() {
        userProfileLiveData.observeForever { response ->
            response?.data?.img_url?.let { url ->

                _imgUrl.postValue(url)
            }
        }
    }







}