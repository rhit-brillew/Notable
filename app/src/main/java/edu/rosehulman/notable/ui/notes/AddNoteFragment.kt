package edu.rosehulman.notable.ui.notes

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import edu.rosehulman.notable.R
import edu.rosehulman.notable.adapters.NoteAdapter
import edu.rosehulman.notable.databinding.FragmentAddNoteBinding
import edu.rosehulman.notable.models.Note
import edu.rosehulman.notable.models.NoteViewModel
import kotlin.random.Random

class AddNoteFragment : Fragment() {

    private lateinit var model: NoteViewModel
    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var adapter: NoteAdapter
    private val storageImagesRef = Firebase.storage
        .reference
        .child("videos")
    private var storageUriStringInFragment: String = ""
    private val REQUEST_VIDEO_CAPTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        model =
            ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)

        adapter = model.getAdapter()

        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        setupButtons()

        return binding.root
    }

    private fun setupButtons() {
        binding.cameraButton.setOnClickListener {
            binding.submitButton.isEnabled = false
            binding.submitButton.text = "LOADING..."
            Log.d("NTBLE", "Camera button clicked")
            dispatchTakeVideoIntent()
        }
        binding.tabButton.setOnClickListener {
            findNavController().navigate(R.id.nav_add_tab)
        }
        binding.submitButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val tab = model.tabTemp
            val downloadUrl = storageUriStringInFragment
            adapter.addNote(Note(title, description, tab, downloadUrl))
            //model.addNote(Note(title, description, tab))
            findNavController().navigate(R.id.nav_notes)
        }
    }

    private fun dispatchTakeVideoIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            Log.d("NTBLE", "Here")
            takeVideoIntent.resolveActivity(requireContext().packageManager)?.also {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
                Log.d("NTBLE", "Launches camera")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri: Uri = data!!.data!!
            Log.d("NTBLE", videoUri.toString())
            //videoView.setVideoURI(videoUri)
            addPhotoFromUri(videoUri)

        }
    }

    private fun addPhotoFromUri(uri: Uri?) {
        Log.d("NTBLE", "Calls addPhotoFromUri")
        if (uri == null) {
            Log.e("NTBLE", "Uri is null. Not saving to storage")
            return
        }
        // https://stackoverflow.com/a/5657557
        val stream = requireActivity().contentResolver.openInputStream(uri)
        if (stream == null) {
            Log.e("NTBLE", "Stream is null. Not saving to storage")
            return
        }

        val videoId = kotlin.math.abs(Random.nextLong()).toString()

        storageImagesRef.child(videoId).putStream(stream)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageImagesRef.child(videoId).downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageUriStringInFragment = task.result.toString()
                    Log.d("NTBLE", "Got download uri: $storageUriStringInFragment")
                    binding.submitButton.text = "SUBMIT"
                    binding.submitButton.isEnabled = true
                } else {
                    Log.d("NTBLE", "Task failed!")
                }
            }
    }
}