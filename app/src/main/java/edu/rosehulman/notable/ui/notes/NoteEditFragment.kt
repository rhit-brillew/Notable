package edu.rosehulman.notable.ui.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.notable.R
import edu.rosehulman.notable.databinding.FragmentNoteDetailBinding
import edu.rosehulman.notable.databinding.FragmentNoteEditBinding
import edu.rosehulman.notable.models.NoteViewModel

class NoteEditFragment : Fragment() {
    private lateinit var binding: FragmentNoteEditBinding
    private lateinit var model: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        model =
            ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)

        binding = FragmentNoteEditBinding.inflate(inflater, container, false)
        setupButtons()
        updateView()

        return binding.root
    }

    private fun setupButtons() {
        binding.saveButton.setOnClickListener {
            val title = binding.editTitleEditText.text.toString()
            val description = binding.editDescriptionEditText.text.toString()
            val tab = model.tabTemp
            model.updateCurrentNote(title, description, tab)
            findNavController().popBackStack()
        }
    }

    private fun updateView() {
        binding.editDescriptionEditText.setText(model.getCurrentNote().description)
        binding.editTabEditText.setText(model.getCurrentNote().tab)
        binding.editTitleEditText.setText(model.getCurrentNote().title)

    }
}