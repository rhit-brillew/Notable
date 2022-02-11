package edu.rosehulman.notable.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import java.net.URI

data class Note(var title: String = "", var description: String = "",
                var tab: String = "", var videoUrl: String = "", var isSelected: Boolean = false) {
    @get:Exclude
    var id = ""

    companion object {
        const val COLLECTION_PATH = "notes"

        fun from(snapshot: DocumentSnapshot): Note {
            val note = snapshot.toObject(Note::class.java)!! // it will not be null
            note.id = snapshot.id
            return note
        }
    }
}