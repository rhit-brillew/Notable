package edu.rosehulman.notable.ui.guitars

import android.app.Activity.RESULT_OK
import android.content.Intent
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
import edu.rosehulman.notable.databinding.FragmentGuitarsEditBinding
import edu.rosehulman.notable.models.GuitarViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class GuitarsEditFragment : Fragment() {

    private lateinit var model: GuitarViewModel
    private lateinit var binding: FragmentGuitarsEditBinding



    //guitar image values
    private var storageUriStringInFragment: String = ""
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    binding.guitarImage.setImageURI(uri)
                    addPhotoFromUri(uri)
                }
            }
        }
    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.guitarImage.setImageURI(uri)
                addPhotoFromUri(uri)
            }
        }
    private val storageImagesRef = Firebase.storage
        .reference
        .child("guitarImages")
    private var latestTmpUri: Uri? = null





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGuitarsEditBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(GuitarViewModel::class.java)

        setupButtons()
        updateView()
        return binding.root
    }

    fun updateView(){
        if(model.size()!=0){
            binding.editGuitarName.setText(model.getCurrentGuitar().name)
            binding.editGuitarDescription.setText(model.getCurrentGuitar().description)
            Log.d(Constants.TAG, model.getCurrentGuitar().storageURIString)
            if(model.getCurrentGuitar().storageURIString.isEmpty()){
                binding.guitarImage.setImageDrawable(getResources().getDrawable(R.drawable.guitar_icon2))
            }else{
                binding.guitarImage.load(model.getCurrentGuitar().storageURIString)
            }
        }
    }

    fun setupButtons(){
        binding.editGuitarSubmitButton.setOnClickListener{
            //todo: send new image to firebase storage
            val name = binding.editGuitarName.text.toString()
            val description = binding.editGuitarDescription.text.toString()
            lateinit var location: String
            if(storageUriStringInFragment.isEmpty()){
                location=model.getCurrentGuitar().storageURIString
            }else{
                location=storageUriStringInFragment
            }
            model.updateCurrentGuitar(location, name, description)
            //updateView()
            findNavController().navigate(R.id.nav_guitars_detail)
        }

        binding.guitarChangeImageButton.setOnClickListener{
            showPictureDialog()
        }
    }



    //image methods
    private fun showPictureDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose a photo source")
        builder.setMessage("Would you like to take a new picture?\nOr choose an existing one?")
        builder.setPositiveButton("Take Picture") { _, _ ->
            binding.editGuitarSubmitButton.isEnabled = false
            binding.editGuitarSubmitButton.text = "Loading image"
            takeImage()
        }

        builder.setNegativeButton("Choose Picture") { _, _ ->
            binding.editGuitarSubmitButton.isEnabled = false
            binding.editGuitarSubmitButton.text = "Loading image"
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
                    storageUriStringInFragment = task.result.toString()
                    Log.d(Constants.TAG, "Got download uri: $storageUriStringInFragment")
                    binding.editGuitarSubmitButton.text = "done"
                    binding.editGuitarSubmitButton.isEnabled = true
                } else {
                    // Handle failures
                    // ...
                }
            }

    }

}