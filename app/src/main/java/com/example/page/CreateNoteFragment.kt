package com.example.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.page.Utils.NetworkResult
import com.example.page.databinding.FragmentCreateNoteBinding
import com.example.page.databinding.FragmentMainBinding
import com.example.page.models.NoteRequest
import com.example.page.models.UserRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNoteFragment : Fragment() {
    private var _binding : FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel by viewModels<NoteViewModel>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveNote.setOnClickListener {
            val validateResult = validateUserInput()
            if(validateResult.first){
                noteViewModel.createNote(getUserRequest())

            }
            else{
                binding.errorMassage.text = validateResult.second
            }

        }


        bindObservers()




    }
    private fun getUserRequest(): NoteRequest {
        val title = binding.addTitle.text.toString().trim()
        val content = binding.addContent.text.toString().trim()

        return NoteRequest(title,content)
    }
    private fun validateUserInput(): Pair<Boolean, String>{
        val noteRequest = getUserRequest()
        return noteViewModel.validateCredentials(noteRequest.title,noteRequest.content)

    }


    private fun bindObservers(){
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {


            when(it){
                is NetworkResult.Success-> {
                    findNavController().navigate(R.id.action_createNoteFragment_to_mainFragment)

                }
                is NetworkResult.Error-> {
                    binding.errorMassage.text = it.massage

                }
                is NetworkResult.Loading-> {

                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}