package edu.rosehulman.notable.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.notable.Constants
import edu.rosehulman.notable.R
import edu.rosehulman.notable.databinding.FragmentProfileBinding
import edu.rosehulman.notable.models.Profile
import edu.rosehulman.notable.models.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var model: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        model = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        //model.ref = Firebase.firestore.collection(Profile.COLLECTION_PATH).document(Firebase.auth.uid!!)

        setupButtons()
        updateView()

        return binding.root
    }

//    override fun onResume() {
//        super.onResume()
//        model.ref = Firebase.firestore.collection(Profile.COLLECTION_PATH).document(Firebase.auth.uid!!)
//        updateView()
//    }

    override fun onStart() {
        super.onStart()
        model.ref = Firebase.firestore.collection(Profile.COLLECTION_PATH).document(Firebase.auth.uid!!)
        updateView()
    }

    private fun setupButtons() {
        binding.editButton.setOnClickListener {
            findNavController().navigate(R.id.nav_profile_edit)
        }
        binding.logoutButton.setOnClickListener{
            Firebase.auth.signOut()
            findNavController().navigate(R.id.nav_profile_loading)
            model.profile=null
        }
    }

    private fun updateView() {
        //todo: this
        with(model.profile!!){
            Log.d(Constants.TAG, "Updating user: $this")
            binding.name.text=name
            Log.d(Constants.TAG, "URL: $storageURIString")
            if(storageURIString.isNotEmpty()){
                binding.centerImage.load(storageURIString)
                {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }
}