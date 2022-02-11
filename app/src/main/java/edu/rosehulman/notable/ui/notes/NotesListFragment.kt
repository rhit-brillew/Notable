package edu.rosehulman.notable.ui.notes

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.rosehulman.notable.R
import edu.rosehulman.notable.adapters.NoteAdapter
import edu.rosehulman.notable.databinding.FragmentNotesListBinding

class NotesListFragment : Fragment() {

    private lateinit var binding: FragmentNotesListBinding
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesListBinding.inflate(inflater, container, false)
        adapter = NoteAdapter(this)
        adapter.addListener(fragmentName)
        setAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        setHasOptionsMenu(true)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.nav_note_add)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener(fragmentName)
    }

    companion object {
        const val fragmentName = "NotesListFragment"
    }

    private fun setAdapter() {
        adapter.model.setAdapter(adapter)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.delete_note -> {
            MaterialAlertDialogBuilder(requireContext())
                // Add customization options here
                .setTitle("Are you sure?")
                .setMessage("Are you sure you want to delete these notes?")
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    adapter.removeNotes()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}