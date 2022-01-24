package edu.rosehulman.notable.models

import androidx.lifecycle.ViewModel

class NoteViewModel: ViewModel() {
    var notes = ArrayList<Note>()
    var currentPos = 0
    var tabTemp = ""


    fun getNoteAt(pos: Int) = notes[pos]
    fun getCurrentNote() = notes[currentPos]


    fun addNote(note: Note?) {
        val newNote = note ?: Note("Title", "Description", "Tab")
        notes.add(newNote)
    }

    fun updateCurrentNote(title: String, description: String, tab: String) {
        notes[currentPos].title = title
        notes[currentPos].description = description
        notes[currentPos].tab = tab
    }

    fun removeCurrentNote() {
        notes.remove(getCurrentNote())
        currentPos = 0
    }

    fun updatePos(pos: Int) {
        currentPos = pos
    }

    fun size() = notes.size

    fun toggleCurrentNote() {
        notes[currentPos].isSelected = !notes[currentPos].isSelected
    }

}