package edu.rosehulman.notable.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

class Guitar(var location:String="", var name:String="", var description:String="") {

    @get:Exclude
    var id = ""

    @ServerTimestamp
    var created: Timestamp? = null

    override fun toString():String{
        return "'$name': $description"
    }

    companion object{
        const val COLLECTION_PATH = "Guitars"
        const val CREATED_KEY = "created"

        fun from(snapshot: DocumentSnapshot):Guitar{
            val g = snapshot.toObject(Guitar::class.java)!!
            g.id=snapshot.id
            return g
        }
    }
}