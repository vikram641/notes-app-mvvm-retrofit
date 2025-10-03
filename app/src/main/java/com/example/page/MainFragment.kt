package com.example.page

import NotesAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.page.Utils.Constants.TAG
import com.example.page.Utils.NetworkResult
//import com.example.page.api.NotesAPI
import com.example.page.databinding.FragmentMainBinding
import com.example.page.databinding.FragmentRegisterBinding
import com.example.page.models.Note
import com.example.page.models.NoteRequest
import com.example.page.models.NoteResponce
import com.example.page.models.User
import com.example.page.models.UserX
import com.example.page.models.UserdetailResponse
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

    lateinit var user: User


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

//        binding.logOut.setOnClickListener {


//            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
//        }

        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_createNoteFragment)

        }
        binding.menuIcon.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.RIGHT)
        }
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_profile -> { onNoteClick(user = user)}
                R.id.nav_settings -> { /* open settings */ }
                R.id.nav_logout -> {
                    authViewModel.logout()

                    findNavController().navigate(
                        R.id.action_mainFragment_to_registerFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.mainFragment, true) // mainFragment tak backstack clear
                            .build()
                    )
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }



        binding.noteList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = notesAdapter

//


        noteViewModel.getNotes()
        authViewModel.userdeatil()


        bindObservers()



    }



    private fun bindObservers(){
        authViewModel.userResponseLiveDate.observe(viewLifecycleOwner,{
            when(it){
                is NetworkResult.Success->{
                    it.data?.let { userdetail ->
                        user = userdetail.user
                        val headerView = binding.navigationView.getHeaderView(0)
                        val profileImage = headerView.findViewById<ImageView>(R.id.profileImage)
                        val profileName = headerView.findViewById<TextView>(R.id.profileName)
                        val profileEmail = headerView.findViewById<TextView>(R.id.profileEmail)


                        // Bind API data
                        profileName.text = userdetail.user.username
                        profileEmail.text = userdetail.user.email


                        // Load image with Glide/Coil
                        profileImage.load(userdetail.user.img_url) {
                            crossfade(true)
                            placeholder(R.drawable.ic_profile) // default icon
                            error(R.drawable.ic_profile)       // fallback agar url galat hai
                            transformations(CircleCropTransformation())
                        }


                    }
                }
                is NetworkResult.Error -> {
                }

                is NetworkResult.Loading -> {
                    // Optionally show progress bar in header if needed
                }
            }
        })
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

    private fun onNoteClick(note: Note? = null, user: User? = null) {

        // If note is provided -> go to NoteFragment
        note?.let {
            val bundle = Bundle().apply {
                putParcelable("note", it)
            }
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
        }

        // If userX is provided -> go to UserProfileFragment
        user?.let {
            val bundle = Bundle().apply {
                putParcelable("user", it)
            }
            findNavController().navigate(R.id.action_mainFragment_to_userProfileFragment, bundle)
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}