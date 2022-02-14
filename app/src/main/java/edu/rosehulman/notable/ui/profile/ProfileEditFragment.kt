package edu.rosehulman.notable.ui.profile

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import edu.rosehulman.notable.BuildConfig
import edu.rosehulman.notable.Constants
import edu.rosehulman.notable.R
import edu.rosehulman.notable.databinding.FragmentNotesListBinding
import edu.rosehulman.notable.databinding.FragmentProfileEditBinding
import edu.rosehulman.notable.models.ProfileViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class ProfileEditFragment : Fragment() {

    private lateinit var binding: FragmentProfileEditBinding
    private lateinit var model: ProfileViewModel


    //image
    private var storageUriStringInFragment: String = ""
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    binding.profileImage.setImageURI(uri)
                    addPhotoFromUri(uri)
                }
            }
        }
    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.profileImage.setImageURI(uri)
                addPhotoFromUri(uri)
            }
        }
    private val storageImagesRef = Firebase.storage
        .reference
        .child("pfpImages")
    private var latestTmpUri: Uri? = null


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        model = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        setupButtons()
        //updateView()

        model.getOrMakeProfile {
            with(model.profile!!){
                Log.d(Constants.TAG, "$this")
                binding.editName.setText(name)
                binding.profileImage.load(storageURIString)
                storageUriStringInFragment = storageURIString
            }
        }

        return binding.root
    }

    private fun setupButtons() {
        binding.changeImageButton.setOnClickListener {
            // do stuff to change the image
            showPictureDialog()
            model.getOrMakeProfile {
                with(model.profile!!){
                    Log.d(Constants.TAG, "$this")
                    binding.editName.setText(name)
                    binding.profileImage.load(storageURIString)
                    storageUriStringInFragment = storageURIString
                }
            }
        }
        binding.editProfileSubmitButton.setOnClickListener {
            // eventually this will make a call to the model
            // for updating firebase
            val newNameFromView = binding.editName.text.toString()
            model.update(
                newName=newNameFromView,
                newStorageURIString=this.storageUriStringInFragment,
                setup=true)
            findNavController().navigate(R.id.nav_profile)
        }
    }

    private fun updateView(){
        if(model.profile != null){
            with(model.profile!!){
                binding.profileImage.load(storageURIString)
                binding.editName.setText(name)
            }
        }

    }

    //image methods
    private fun showPictureDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose a photo source")
        builder.setMessage("Would you like to take a new picture?\nOr choose an existing one?")
        builder.setPositiveButton("Take Picture") { _, _ ->
            binding.editProfileSubmitButton.isEnabled = false
            binding.editProfileSubmitButton.text = "Loading image"
            takeImage()
        }

        builder.setNegativeButton("Choose Picture") { _, _ ->
            binding.editProfileSubmitButton.isEnabled = false
            binding.editProfileSubmitButton.text = "Loading image"
            selectImageFromGallery()
        }
        builder.create().show()
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val storageDir: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val tmpFile = File.createTempFile("JPEG_${timeStamp}_", ".png", storageDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun addPhotoFromUri(uri: Uri?) {
        if (uri == null) {
            Log.e(Constants.TAG, "Uri is null. Not saving to storage")
            return
        }
// https://stackoverflow.com/a/5657557
        val stream = requireActivity().contentResolver.openInputStream(uri)
        if (stream == null) {
            Log.e(Constants.TAG, "Stream is null. Not saving to storage")
            return
        }

        // TODO: Add to storage
        val imageId = Math.abs(Random.nextLong()).toString()
        storageImagesRef.child(imageId).putStream(stream)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageImagesRef.child(imageId).downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    this.storageUriStringInFragment = task.result.toString()
                    Log.d(Constants.TAG, "Got download uri: ${this.storageUriStringInFragment}")
                    binding.editProfileSubmitButton.text = "done"
                    binding.editProfileSubmitButton.isEnabled = true
                } else {
                    // Handle failures
                    // ...
                }
            }

    }

}