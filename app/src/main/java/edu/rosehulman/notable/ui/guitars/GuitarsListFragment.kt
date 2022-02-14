package edu.rosehulman.notable.ui.guitars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.notable.R
import edu.rosehulman.notable.adapters.GuitarsAdapter
import edu.rosehulman.notable.databinding.FragmentGuitarsListBinding

class GuitarsListFragment : Fragment() {

    private lateinit var binding: FragmentGuitarsListBinding
    lateinit var adapter: GuitarsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGuitarsListBinding.inflate(inflater, container, false)

        adapter = GuitarsAdapter(this)
        binding.recyclerView.adapter=adapter
        adapter.addListener(fragmentName)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)

        binding.fab.setOnClickListener{
            adapter.addGuitar(null)
            //adapter.setCurrentToLastGuitar()
            //findNavController().navigate(R.id.nav_guitars_edit)
        }

        return binding.root
    }

    companion object{
        const val fragmentName = "GuitarsListFragment"
    }

}