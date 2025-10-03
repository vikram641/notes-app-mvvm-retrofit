//package com.example.page.repository
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.example.page.Utils.NetworkResult
//import com.example.page.api.API
//import dagger.hilt.android.lifecycle.HiltViewModel
//import org.json.JSONObject
//import retrofit2.Response
//import retrofit2.http.Multipart
//import javax.inject.Inject
//
//@HiltViewModel
//class BaseRepository<T> @Inject constructor(private val api: API) {
//    private val _responseLiveData = MutableLiveData<NetworkResult<Any>>()
//    val responseLiveData: LiveData<NetworkResult<Any>> get() = _responseLiveData
//
//
//    suspend fun <T> makeApiCall(apiCall: suspend () -> Response<T>) {
//        _responseLiveData.postValue(NetworkResult.Loading())
//        try {
//            val response = apiCall()
//            if (response.isSuccessful && response.body() != null) {
//                _responseLiveData.postValue(NetworkResult.Success(response.body()!!))
//            } else if (response.errorBody() != null) {
//                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
//                _responseLiveData.postValue(NetworkResult.Error(errorObj.getString("massage")))
//            } else {
//                _responseLiveData.postValue(NetworkResult.Error("Unknown error"))
//            }
//        } catch (e: Exception) {
//            _responseLiveData.postValue(NetworkResult.Error("Network call failed: ${e.message}"))
//        }
//    }
//
//
//    suspend fun updatedetail(multipart: Multipart){
//        makeApiCall { api.updateProfile(multipart) }
//
//    }
//
//
//
//
//}
