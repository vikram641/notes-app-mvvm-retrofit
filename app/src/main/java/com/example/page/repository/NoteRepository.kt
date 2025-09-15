package com.example.page.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.page.Utils.Constants.TAG
import com.example.page.Utils.NetworkResult
import com.example.page.api.NotesAPI
import com.example.page.models.Note
import com.example.page.models.NoteRequest
import com.example.page.models.NoteResponce
import com.example.page.models.NotesResponceGet
import com.example.page.models.UserResponce
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesAPI: NotesAPI) {
    private val _notesLiveData = MutableLiveData<NetworkResult<List<Note>>>()
    val notesLiveData : LiveData<NetworkResult<List<Note>>>
    get() = _notesLiveData


   private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData:  LiveData<NetworkResult<String>>
        get()  = _statusLiveData



//    suspend fun getNotes(){
////        Log.d(TAG, response.body().toString())
//        _notesLiveData.postValue(NetworkResult.Loading())
//        val response = notesAPI.getNotes()
////        Log.d(TAG, notesAPI.getNotes().toString())
//        if(response.isSuccessful){
//            val notesList = response.body()?.notes?: emptyList()
//            _notesLiveData.postValue(NetworkResult.Success(notesList))
//        }
//        else if(response.errorBody()!= null){
//            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
//            _notesLiveData.postValue(NetworkResult.Error(errorObj.getString("massage")))
//        }
//        else{
//            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
//        }
//
//
//    }
    suspend fun getNotes(){
    _statusLiveData.postValue((NetworkResult.Loading()))
    try {
        val response = notesAPI.getNotes()

        if (response.isSuccessful && response.body() != null) {
            val notesList = response.body()?.notes?: emptyList()
            _notesLiveData.postValue(NetworkResult.Success(notesList))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _statusLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    } catch (e: Exception) {
        e.printStackTrace()
        _statusLiveData.postValue(NetworkResult.Error("Network call failed: ${e.message}"))
    }
    }

    suspend fun createNote(noteRequest: NoteRequest){
        _notesLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.createNote(noteRequest)
        if(response.isSuccessful && response.body() != null){
            _statusLiveData.postValue(NetworkResult.Success(response.body()!!.message))
        }
        else if(response.errorBody()!= null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetworkResult.Error(errorObj.getString("massage")))
        }
        else{
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }



    }

    suspend fun updateNote(noteId: String, noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.updateNote(noteId , noteRequest)
        if(response.isSuccessful && response.body() != null){
            _statusLiveData.postValue(NetworkResult.Success(response.body()!!.message))
        }
        else if(response.errorBody()!= null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetworkResult.Error(errorObj.getString("massage")))
        }
        else{
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }


    }

//    suspend fun deleteNote(noteId: String){
//        _statusLiveData.postValue(NetworkResult.Loading())
//        val response = notesAPI.deleteNote(noteId)
//        if(response.isSuccessful && response.body() != null){
//            _statusLiveData.postValue(NetworkResult.Success(response.body()!!.message))
//        }
//        else if(response.errorBody()!= null){
//            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
//            _notesLiveData.postValue(NetworkResult.Error(errorObj.getString("massage")))
//        }
//        else{
//            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
//        }
//
//    }

    suspend fun deleteNote(noteId: String){
        _statusLiveData.postValue((NetworkResult.Loading()))
        try {
            val response = notesAPI.deleteNote(noteId)

            if (response.isSuccessful && response.body() != null) {
                _statusLiveData.postValue(NetworkResult.Success(response.body()!!.message))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _statusLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _statusLiveData.postValue(NetworkResult.Error("Network call failed: ${e.message}"))
        }
    }



}