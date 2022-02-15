package edu.rosehulman.notable.models

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.notable.adapters.NoteAdapter

class NoteViewModel: ViewModel() {
    var notes = ArrayList<Note>()
    var removalList = ArrayList<Note>()
    var currentPos = 0
    var tabTemp = ""
    private lateinit var ref: CollectionReference
    val subscriptions = HashMap<String, ListenerRegistration>()
    private lateinit var adapter: NoteAdapter


    fun getNoteAt(pos: Int) = notes[pos]
    fun getCurrentNote() = notes[currentPos]

    fun addListener(fragmentName: String, observer: () -> Unit) {
        val uid = Firebase.auth.currentUser!!.uid
        ref = Firebase.firestore.collection(Profile.COLLECTION_PATH).document(uid).collection(Note.COLLECTION_PATH)
        val subscription =  ref
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    return@addSnapshotListener
                }

                notes.clear()
                snapshot?.documents?.forEach {
                    notes.add(Note.from(it))
                }
                observer()
            }
        subscriptions[fragmentName] = subscription
    }

    fun removeListener(fragmentName: String) {
        subscriptions[fragmentName]?.remove() //tells firebase to stop listening
        subscriptions.remove(fragmentName) //removes fragment name
    }

    fun addNote(note: Note?) {
        val newNote = note ?: Note("Title", "Description", "Tab")
        ref.add(newNote)
        //notes.add(newNote)
    }

    fun removeNotes() {
        removalList.clear()
        for (note in notes) {
            if (note.isSelected) {
                note.isSelected = false
                removalList.add(note)
            }
        }
        for (note in removalList) {
            //notes.remove(note)
            ref.document(note.id).delete()
        }
    }

    fun updateCurrentNote(title: String, description: String, tab: String) {
        notes[currentPos].title = title
        notes[currentPos].description = description
        notes[currentPos].tab = tab
        ref.document(getCurrentNote().id).set(getCurrentNote())
    }

    fun setAdapter(listAdapter: NoteAdapter) {
        adapter = listAdapter
    }

    fun getAdapter(): NoteAdapter {
        return this.adapter
    }

    fun removeCurrentNote() {
        notes.remove(getCurrentNote())
        //ref.document(getCurrentNote().id).delete()
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