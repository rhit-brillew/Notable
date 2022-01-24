package edu.rosehulman.notable.ui.notes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.notable.R
import edu.rosehulman.notable.databinding.FragmentAddNoteBinding
import edu.rosehulman.notable.models.Note
import edu.rosehulman.notable.models.NoteViewModel

class AddNoteFragment : Fragment() {

    private lateinit var model: NoteViewModel
    private lateinit var binding: FragmentAddNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        model =
            ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)

        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        setupButtons()

        return binding.root
    }

    private fun setupButtons() {
        binding.cameraButton.setOnClickListener {
            Log.d("NTBLE", "Camera button clicked")
        }
        binding.tabButton.setOnClickListener {
            findNavController().navigate(R.id.nav_add_tab)
        }
        binding.submitButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val tab = model.tabTemp
            model.addNote(Note(title, description, tab))
            findNavController().navigate(R.id.nav_notes)
        }
    }

}