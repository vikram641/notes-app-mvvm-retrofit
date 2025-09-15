package com.example.page

import NotesAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.page.Utils.Constants.TAG
import com.example.page.Utils.NetworkResult
import com.example.page.api.NotesAPI
import com.example.page.databinding.FragmentMainBinding
import com.example.page.databinding.FragmentRegisterBinding
import com.example.page.models.Note
import com.example.page.models.NoteRequest
import com.example.page.models.NoteResponce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel by viewModels<NoteViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var notesAdapter: NotesAdapter

//    @Inject
//    lateinit var notesAPI: NotesAPI



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        notesAdapter = NotesAdapter(::onNoteClick)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logOut.setOnClickListener {
            authViewModel.logout()

            findNavController().navigate(
                R.id.action_mainFragment_to_registerFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.mainFragment, true) // mainFragment tak backstack clear
                    .build()
            )
//            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }

        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_createNoteFragment)

        }


        binding.noteList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = notesAdapter

        noteViewModel.getNotes()


        bindObservers()



    }


    private fun bindObservers(){
        noteViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {


            when(it){
                is NetworkResult.Success-> {

//                    it.data?.let { response ->
//                        notesAdapter.submitList(it.data)
//                    }

                    it.data?.let { notes ->
                        if (notes.isEmpty()) {
                            binding.noteList.visibility = View.GONE
                            binding.emptyView.visibility = View.VISIBLE
                        } else {
                            binding.noteList.visibility = View.VISIBLE
                            binding.emptyView.visibility = View.GONE
                            notesAdapter.submitList(notes)
                        }
                    }

                }
                is NetworkResult.Error-> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Error")
                        .setMessage(it.massage?: "Something went wrong!")
                        .setPositiveButton("OK", null)
                        .show()


                }
                is NetworkResult.Loading-> {

                }
            }
        })
    }

    private fun onNoteClick(note: Note){
//        Toast.makeText(requireContext(), "note - ${note.title}", Toast.LENGTH_SHORT).show()
        val bundle = Bundle().apply {
            putParcelable("note", note)
        }
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)





    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}