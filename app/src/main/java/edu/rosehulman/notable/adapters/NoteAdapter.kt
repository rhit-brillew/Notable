package edu.rosehulman.notable.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.notable.ui.notes.NotesListFragment

class NoteAdapter(val fragment: NotesListFragment): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    fun addNote(nothing: Nothing?) {

    }

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        
    }
}