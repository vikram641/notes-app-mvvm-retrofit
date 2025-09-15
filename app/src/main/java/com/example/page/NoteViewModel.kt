package com.example.page

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.page.models.NoteRequest
import com.example.page.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository):ViewModel() {


    val notesLiveData get() = noteRepository.notesLiveData
    val statusLiveData get() = noteRepository.statusLiveData


    fun getNotes(){
        viewModelScope.launch {
            noteRepository.getNotes()
        }
    }
    fun createNote(noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.createNote(noteRequest)
        }
    }

    fun updateNote(noteId: String,noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.updateNote(noteId,noteRequest)
        }
    }

    fun deleteNote(noteId: String){
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }

    fun validateCredentials (title : String, content: String): Pair<Boolean,String> {
        var result = Pair(true,"")

        if(TextUtils.isEmpty(content)){
            result = Pair(false, "Please write title...")

        }
        else if (TextUtils.isEmpty(title)){
            result = Pair(false, "Please write note...")

        }
        else if(TextUtils.isEmpty(title) && TextUtils.isEmpty(content)){
            result = Pair(false , "please write title/note...")
        }
        return result


    }



}