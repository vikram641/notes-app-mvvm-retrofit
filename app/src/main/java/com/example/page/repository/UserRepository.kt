package com.example.page.repository

import android.nfc.Tag
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.page.Utils.Constants.TAG
import com.example.page.Utils.NetworkResult
import com.example.page.api.API
import com.example.page.models.UpdateUserdeatilsRequest
import com.example.page.models.UpdateUserdetailsResponse
//import com.example.page.api.UserAPI
import com.example.page.models.UserRequest
import com.example.page.models.UserResponce
import com.example.page.models.UserdetailResponse
import com.example.page.models.updateProfileResponse
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Multipart
import javax.inject.Inject

class UserRepository @Inject constructor(private val api: API) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponce>>()
    val userResponseLiveData : LiveData<NetworkResult<UserResponce>>
    get() = _userResponseLiveData

    private val _userProfileLiveData = MutableLiveData<NetworkResult<updateProfileResponse>>()
    val userProfileLiveData : LiveData<NetworkResult<updateProfileResponse>>
    get() = _userProfileLiveData

    private val _updateUserProfileLiveData = MutableLiveData<NetworkResult<UpdateUserdetailsResponse>>()
    val updateUserProfileLiveData : LiveData<NetworkResult<UpdateUserdetailsResponse>>
        get() = _updateUserProfileLiveData



    suspend fun registerUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = api.signup(userRequest)
//        Log.d(TAG, response.body().toString())

       handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = api.signin(userRequest)
//        Log.d(TAG, response.body().toString())
//        _userResponseLiveData.postValue(NetworkResult.Loading())
        handleResponse(response)
    }

    suspend fun userdetail(){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = api.getUserdetails()
        Log.d(TAG, api.getUserdetails().toString())

        handleResponse(response)

    }

    suspend fun profileUpdate(multipart: MultipartBody.Part) {
        _userProfileLiveData.postValue(NetworkResult.Loading())

        try {
            val response = api.updateProfile(multipart)
            Log.d(TAG, response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                _userProfileLiveData.postValue(NetworkResult.Success(response.body()!!))


            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _userProfileLiveData.postValue(
                    NetworkResult.Error(errorObj.optString("message", "Something went wrong"))
                )
            } else {
                _userProfileLiveData.postValue(NetworkResult.Error("Unknown error"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            _userProfileLiveData.postValue(NetworkResult.Error(e.localizedMessage ?: "Exception occurred"))
        }
    }

    suspend fun UserdeatilsUpdate(updateUserdeatilsRequest: UpdateUserdeatilsRequest){
        val response = api.updateUserdetails(updateUserdeatilsRequest)
        Log.d(TAG, response.body().toString()+"kkkkkk")
        try {
            if (response.isSuccessful && response.body() != null) {
                _updateUserProfileLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                Log.d(TAG, errorObj.toString())
                _updateUserProfileLiveData.postValue(NetworkResult.Error(errorObj.getString("massage")))
                //            Log.d(TAG,_userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("massage"))).toString()
            } else {
                _updateUserProfileLiveData.postValue(NetworkResult.Loading())
            }

        }
        catch (e:Exception){
            e.printStackTrace()
            _updateUserProfileLiveData.postValue(NetworkResult.Error("Network call failed: ${e.message}"))
        }

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
