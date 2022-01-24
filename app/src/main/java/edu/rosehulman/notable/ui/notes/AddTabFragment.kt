package edu.rosehulman.notable.ui.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.notable.Constants
import edu.rosehulman.notable.R
import edu.rosehulman.notable.databinding.FragmentAddNoteBinding
import edu.rosehulman.notable.databinding.FragmentAddTabBinding
import edu.rosehulman.notable.models.NoteViewModel
import kotlin.concurrent.fixedRateTimer

class AddTabFragment : Fragment() {

    private lateinit var binding: FragmentAddTabBinding
    private lateinit var model: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        model =
            ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)

        binding = FragmentAddTabBinding.inflate(inflater, container, false)
        setupButtons()

        return binding.root
    }

    private fun setupButtons() {
        binding.addBlock.setOnClickListener {
            val tabEditText = binding.tabEditText
            tabEditText.setText(tabEditText.text.toString().plus(Constants.EMPTY_TAB_BLOCK))
        }
        binding.submitTab.setOnClickListener {
            model.tabTemp = binding.tabEditText.text.toString()
            findNavController().popBackStack()
        }
    }

}