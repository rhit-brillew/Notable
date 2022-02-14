package edu.rosehulman.notable.models

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.notable.Constants

class  GuitarViewModel : ViewModel() {

    var guitars = ArrayList<Guitar>()
    var currentPos = 0
    fun getGuitarAt(pos:Int) = guitars[pos]
    fun getCurrentGuitar() = getGuitarAt(currentPos)
    fun addGuitar(g: Guitar?){
        val newGuitar = g ?: Guitar("", "", "")
        //guitars.add(newGuitar)
        ref.add(newGuitar)
    }
    fun updateCurrentGuitar(photoLocation:String,name:String,description:String){
        guitars[currentPos].storageURIString=photoLocation
        guitars[currentPos].name=name
        guitars[currentPos].description=description
        ref.document(getCurrentGuitar().id).set(getCurrentGuitar())
    }
    fun removeCurrentGuitar(){
        //guitars.removeAt(currentPos)
        ref.document(getCurrentGuitar().id).delete()
        currentPos=0
    }
    fun updatePos(pos: Int){
        currentPos = pos
    }
    fun size() = guitars.size

    //firebase
    //users have a list of guitars in them.
    lateinit var ref: CollectionReference
    val subscriptions = HashMap<String, ListenerRegistration>()

    fun addListener(fragmentName: String, observer: () -> Unit){
        val uid = Firebase.auth.currentUser!!.uid
        ref = Firebase.firestore.collection(Profile.COLLECTION_PATH).document(uid).collection(Guitar.COLLECTION_PATH)
        val subscription = ref.orderBy(Guitar.CREATED_KEY, Query.Direction.ASCENDING).addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
            error?.let{
                Log.d(Constants.TAG, "Error: $error")
                return@addSnapshotListener
            }
            guitars.clear()
            snapshot?.documents?.forEach{
                guitars.add(Guitar.from(it))
            }
            observer()
        }
        subscriptions[fragmentName] = subscription
    }

    fun removeListener(fragmentName: String){
        subscriptions[fragmentName]?.remove()
    }
}