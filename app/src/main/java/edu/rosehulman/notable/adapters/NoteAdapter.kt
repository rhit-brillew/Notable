package edu.rosehulman.notable.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.notable.R
import edu.rosehulman.notable.models.Note
import edu.rosehulman.notable.models.NoteViewModel
import edu.rosehulman.notable.ui.notes.NotesListFragment

class NoteAdapter(val fragment: NotesListFragment): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    val model = ViewModelProvider(fragment.requireActivity()).get(NoteViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(model.getNoteAt(position))
    }

    override fun getItemCount() = model.size()

    fun addNote(note: Note?) {
        model.addNote(note)
        notifyDataSetChanged()
    }

    fun removeNotes() {
        model.removeNotes()
        notifyDataSetChanged()
    }

    fun removeCurrentNote() {
        model.removeCurrentNote()
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val noteTitleTextView = itemView.findViewById<TextView>(R.id.row_note_title)
        val noteDescriptionTextView = itemView.findViewById<TextView>(R.id.row_note_description)

        init {
            itemView.setOnClickListener {
                model.updatePos(adapterPosition)
                fragment.findNavController().navigate(R.id.nav_note_detail)
            }

            itemView.setOnLongClickListener {
                model.updatePos(adapterPosition)
                model.toggleCurrentNote()
                notifyDataSetChanged()
                true
            }
        }

        fun bind(note: Note) {
            noteTitleTextView.text = note.title
            noteDescriptionTextView.text = note.description
            itemView.setBackgroundColor(
                if (note.isSelected) {
                    Color.GREEN
                } else {
                    Color.WHITE
                }
            )
        }
    }
}