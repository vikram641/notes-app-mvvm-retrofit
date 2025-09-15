package com.example.page

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.page.Utils.Constants.TAG
import com.example.page.Utils.NetworkResult
import com.example.page.databinding.FragmentMainBinding
import com.example.page.databinding.FragmentNoteBinding
import com.example.page.models.Note
import com.example.page.models.NoteRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val note = arguments?.getParcelable<Note>("note")
        Log.d("hhhhh", arguments?.getParcelable<Note>("note").toString())

        note?.let {
            var title = binding.txtTitle.setText(it.title)
            var content = binding.txtDescription.setText(it.content)

        }



        binding.submit.setOnClickListener {
            noteViewModel.updateNote(noteId = note?._id.toString(), getUserRequest())
            findNavController().navigate(R.id.action_noteFragment_to_mainFragment)
        }

        binding.btnDelete.setOnClickListener {
            noteViewModel.deleteNote(noteId = note?._id.toString())
            findNavController().navigate(R.id.action_noteFragment_to_mainFragment)
        }
        bindObservers()

    }
    private fun getUserRequest(): NoteRequest {
        val title = binding.txtTitle.text.toString().trim()
        val content = binding.txtDescription.text.toString().trim()



        return NoteRequest(content,title)
    }

    private fun bindObservers(){
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {


            when(it){
                is NetworkResult.Success-> {
                    findNavController().navigate(R.id.action_createNoteFragment_to_mainFragment)

                }
                is NetworkResult.Error-> {



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