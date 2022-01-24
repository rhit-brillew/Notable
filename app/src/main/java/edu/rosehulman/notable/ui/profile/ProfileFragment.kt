package edu.rosehulman.notable.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.rosehulman.notable.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupButtons()

        return binding.root
    }

    private fun setupButtons() {
        binding.editName.setOnClickListener {
            binding.name.visibility = View.GONE
            binding.nameEditText.setText(binding.name.text)
            binding.nameEditText.visibility = View.VISIBLE
        }
    }
}