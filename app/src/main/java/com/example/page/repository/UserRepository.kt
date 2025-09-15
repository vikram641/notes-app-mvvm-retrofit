package com.example.page.repository

import android.nfc.Tag
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.page.Utils.Constants.TAG
import com.example.page.Utils.NetworkResult
import com.example.page.api.UserAPI
import com.example.page.models.UserRequest
import com.example.page.models.UserResponce
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {
    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponce>>()
    val userResponseLiveData : LiveData<NetworkResult<UserResponce>>
    get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signup(userRequest)
//        Log.d(TAG, response.body().toString())
        _userResponseLiveData.postValue(NetworkResult.Loading())
       handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signin(userRequest)
//        Log.d(TAG, response.body().toString())
//        _userResponseLiveData.postValue(NetworkResult.Loading())
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponce>) {
        try {
            if (response.isSuccessful && response.body() != null) {
                _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                Log.d(TAG, errorObj.toString())
                _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("massage")))
                //            Log.d(TAG,_userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("massage"))).toString()
            } else {
                _userResponseLiveData.postValue(NetworkResult.Loading())
            }

        }
        catch (e:Exception){
            e.printStackTrace()
            _userResponseLiveData.postValue(NetworkResult.Error("Network call failed: ${e.message}"))
        }


    }


}
