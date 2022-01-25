package edu.rosehulman.notable.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import edu.rosehulman.notable.R
import edu.rosehulman.notable.databinding.FragmentNotesListBinding
import edu.rosehulman.notable.databinding.FragmentProfileEditBinding

class ProfileEditFragment : Fragment() {

    private lateinit var binding: FragmentProfileEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        setupButtons()
        return binding.root
    }

    private fun setupButtons() {
        binding.changeImageButton.setOnClickListener {
            // do stuff to change the image
        }
        binding.editProfileSubmitButton.setOnClickListener {
            // eventually this will make a call to the model
            // for updating firebase
            parentFragmentManager.popBackStack()
        }
    }

}