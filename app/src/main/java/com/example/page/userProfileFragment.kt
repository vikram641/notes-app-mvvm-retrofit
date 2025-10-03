package com.example.page

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.page.Utils.Constants.TAG
import com.example.page.Utils.NetworkResult
import com.example.page.databinding.FragmentRegisterBinding
import com.example.page.databinding.FragmentUserProfileBinding
import com.example.page.models.Note
import com.example.page.models.UpdateUserdeatilsRequest
import com.example.page.models.UpdateUserdetailsResponse
import com.example.page.models.User
import com.example.page.models.UserRequest
import com.example.page.models.updateProfileResponse
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@AndroidEntryPoint
class userProfileFragment : Fragment() {


    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private val profileViewModel by viewModels<UserProfileViewModel>()
    lateinit var  massage:String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getParcelable<User>("user")
        Log.d("hhhhh", arguments?.getParcelable<User>("user").toString())

        user?.let {
            var name = binding.txtUsername11.setText(it.username)
            var email = binding.txtEmail.setText(it.email)
            var userProfile = binding.profileImage.load(it.img_url) {
                crossfade(true)
                placeholder(R.drawable.ic_profile) // default icon
                error(R.drawable.ic_profile)       // fallback agar url galat hai
                transformations(CircleCropTransformation())
            }

        }
        binding.editProfileIcon.setOnClickListener {
            showImagePickerOptions()

        }
        binding.btnSignUp.setOnClickListener {
//            val validateResult = validateUserInput()
            profileViewModel.saveProfileChanges(getUserRequest())
            findNavController().navigate(R.id.action_userProfileFragment_to_mainFragment)

//            if(validateResult.first){
//                profileViewModel.saveProfileChanges(getUserRequest())
//                findNavController().navigate(R.id.action_userProfileFragment_to_mainFragment)
//
//            }
//            else{
//                binding.txtError.text  = validateResult.second
//            }

            Log.d(TAG, "API call triggered")

        }

    }
    private fun showImagePickerOptions() {
        val options = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(requireContext())
            .setTitle("Select Image")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> cameraLauncher.launch(null)  // Camera
                    1 -> galleryLauncher.launch("image/*") // Gallery
                }
            }
            .show()
    }
    // Gallery picker
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { uploadProfileImage(it) }
        }

    // Camera picker
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                val file = bitmapToFile(it)
                uploadProfileImage(Uri.fromFile(file))
            }
        }

    private fun bitmapToFile(bitmap: Bitmap): File {
        val file = File(requireContext().cacheDir, "camera_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        return file
    }

    private fun uploadProfileImage(uri: Uri) {
        val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(uri, "r", null) ?: return
        val file = File(requireContext().cacheDir, "profile_${System.currentTimeMillis()}.jpg")
        FileInputStream(parcelFileDescriptor.fileDescriptor).use { input ->
            FileOutputStream(file).use { output -> input.copyTo(output) }
        }

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("profile", file.name, requestFile)

        profileViewModel.uploadProfileImage(multipartBody)
        bindObservers()

    }


//    private fun validateUserInput(): Pair<Boolean, String> {
//        val updateUserdeatilsRequest = getUserRequest()
//        return profileViewModel.validateCredentials(updateUserdeatilsRequest.email,updateUserdeatilsRequest.username,updateUserdeatilsRequest.img_url, true)
//    }


    private fun getUserRequest(): UpdateUserdeatilsRequest {
//        val username = "dgd"
        val email = binding.txtEmail.text.toString().trim()
        val username = binding.txtUsername11.text.toString().trim()
        val image = profileViewModel.imgUrl.value
        Log.d(TAG, image.toString()+"gggggggg")


        return  UpdateUserdeatilsRequest(email,image.toString(),username)
    }


    private fun bindObservers() {
        profileViewModel.updateUserProfileLiveData.observe(viewLifecycleOwner,{
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success->{
                    it.data?.let { response->
                        massage = response.message


                    }

                }
                is NetworkResult.Error->{

                }
                is NetworkResult.Loading->{

                }

            }
        })
        profileViewModel.userProfileLiveData.observe(viewLifecycleOwner, {
            binding.imageProgressBar.isVisible = false
            when(it){
                is NetworkResult.Success-> {
                    it.data?.let { response ->
                        val imageUrl = response.img_url



                        val placeholderDrawable = binding.profileImage.drawable ?: ContextCompat.getDrawable(requireContext(), R.drawable.ic_profile)

                        if (!imageUrl.isNullOrEmpty()) {
                            binding.profileImage.load(imageUrl) {
                                crossfade(true)
                                placeholder(placeholderDrawable)
                                error(R.drawable.ic_profile)
                                transformations(CircleCropTransformation())
                            }
                        } else {
                            binding.profileImage.setImageResource(R.drawable.ic_profile)
                        }

                    }



                }

                is NetworkResult.Error -> {
                }

                is NetworkResult.Loading -> {
                    binding.imageProgressBar.isVisible = true


                }
            }


        })

    }







}

