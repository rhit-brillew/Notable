package edu.rosehulman.notable.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.notable.Constants

class ProfileViewModel : ViewModel() {
    var ref = Firebase.firestore.collection(Profile.COLLECTION_PATH).document(Firebase.auth.uid!!)

    var profile: Profile? = null

    fun getOrMakeProfile(observer: () -> Unit){
        ref = Firebase.firestore.collection(Profile.COLLECTION_PATH).document(Firebase.auth.uid!!)
        if(profile!=null){
//            ref.get().addOnSuccessListener{snapshot: DocumentSnapshot ->
//                if(snapshot.exists()){
//                    profile = snapshot.toObject(Profile::class.java)
//                }else{
//                    profile= Profile(name=Firebase.auth.currentUser!!.displayName!!)//, snapshot.getString("storageURIString")!!, snapshot.getBoolean("hasCompletedSetup")!!)
//                    ref.set(profile!!)
//                }
//                observer()
//            }
            observer()
        }else{
            ref.get().addOnSuccessListener{snapshot: DocumentSnapshot ->
                if(snapshot.exists()){
                    profile = snapshot.toObject(Profile::class.java)
                }else{
                    if(Firebase.auth.currentUser!!.phoneNumber == null){
                        profile= Profile(name=Firebase.auth.currentUser!!.displayName!!)//, snapshot.getString("storageURIString")!!, snapshot.getBoolean("hasCompletedSetup")!!)
                        ref.set(profile!!)
                    }else{
                        profile= Profile(phoneNumber=Firebase.auth.currentUser!!.phoneNumber!!)
                        ref.set(profile!!)
                    }
                }
                observer()
            }
        }
    }

    fun update(newName: String, newStorageURIString: String, setup: Boolean){
        ref = Firebase.firestore.collection(Profile.COLLECTION_PATH).document(Firebase.auth.uid!!)
        if(profile!=null){
            with(profile!!){
                name=newName
                storageURIString=newStorageURIString
                hasCompletedSetup=setup
                ref.set(this)
            }
        }
    }

    fun hasCompletedSetup(): Boolean{
        return profile?.hasCompletedSetup ?: false
    }
}