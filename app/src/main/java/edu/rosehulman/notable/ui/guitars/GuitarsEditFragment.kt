package edu.rosehulman.notable.ui.guitars

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.notable.R
import edu.rosehulman.notable.databinding.FragmentGuitarsEditBinding
import edu.rosehulman.notable.models.GuitarViewModel
import java.net.URI

class GuitarsEditFragment : Fragment() {

    private lateinit var model: GuitarViewModel
    private lateinit var binding: FragmentGuitarsEditBinding

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGuitarsEditBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(GuitarViewModel::class.java)

        setupButtons()
        updateView()
        return binding.root
    }

    fun updateView(){
        if(model.size()!=0){
            binding.editGuitarName.setText(model.getCurrentGuitar().name)
            binding.editGuitarDescription.setText(model.getCurrentGuitar().description)
            if(model.getCurrentGuitar().location.isEmpty()){
                binding.guitarImage.setImageDrawable(getResources().getDrawable(R.drawable.guitar_icon2))
            }else{
                binding.guitarImage.setImageURI(Uri.parse(model.getCurrentGuitar().location))
            }
            //todo: set image here. If no image exists, then load the icon
        }
    }

    fun setupButtons(){
        binding.editGuitarSubmitButton.setOnClickListener{
            //todo: send new image to firebase storage
            val name = binding.editGuitarName.text.toString()
            val description = binding.editGuitarDescription.text.toString()
            lateinit var location: String
            if(imageUri==null){
                if(model.getCurrentGuitar().location.isEmpty()){
                    location=""
                }else{
                    location=model.getCurrentGuitar().location
                }
            }else{
                location = imageUri.toString()
            }
            model.updateCurrentGuitar(location, name, description)
            updateView()
            findNavController().navigate(R.id.nav_guitars_detail)
        }

        binding.changeImageButton.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            binding.guitarImage.setImageURI(gallery.data)
            imageUri = gallery?.data
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            imageUri = data?.data
            binding.guitarImage.setImageURI(imageUri)
        }
    }
}