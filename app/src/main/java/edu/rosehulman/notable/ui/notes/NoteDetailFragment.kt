package edu.rosehulman.notable.ui.notes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import edu.rosehulman.notable.R
import edu.rosehulman.notable.databinding.FragmentAddTabBinding
import edu.rosehulman.notable.databinding.FragmentNoteDetailBinding
import edu.rosehulman.notable.models.NoteViewModel

class NoteDetailFragment : Fragment() {
    private lateinit var binding: FragmentNoteDetailBinding
    private lateinit var model: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        model =
            ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)

        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        updateView()
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.edit_note -> {
            Log.d("NTBLE", "Test")
            findNavController().navigate(R.id.nav_note_edit)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun updateView() {
        binding.detailTitleText.text = model.getCurrentNote().title
        binding.detailDescriptionText.text = model.getCurrentNote().description
        binding.tabText.text = model.getCurrentNote().tab
    }
}