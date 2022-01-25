package edu.rosehulman.notable.ui.guitars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.notable.adapters.GuitarsAdapter
import edu.rosehulman.notable.databinding.FragmentGuitarsListBinding

class GuitarsListFragment : Fragment() {

    private lateinit var binding: FragmentGuitarsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGuitarsListBinding.inflate(inflater, container, false)

        val adapter = GuitarsAdapter(this)
        binding.recyclerView.adapter=adapter

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)

        binding.fab.setOnClickListener{
            adapter.addGuitar(null)
        }

        return binding.root
    }

}