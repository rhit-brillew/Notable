package edu.rosehulman.notable.ui.guitars

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import edu.rosehulman.notable.R
import edu.rosehulman.notable.databinding.FragmentGuitarsDetailBinding
import edu.rosehulman.notable.models.GuitarViewModel


class GuitarsDetailFragment : Fragment() {

    private lateinit var model: GuitarViewModel
    private lateinit var binding: FragmentGuitarsDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGuitarsDetailBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(GuitarViewModel::class.java)

        setHasOptionsMenu(true)
        setupButtons()
        updateView()

        return binding.root
    }

    fun setupButtons(){
        binding.guitarDetailEditButton.setOnClickListener{
            findNavController().navigate(R.id.nav_guitars_edit)
        }
    }

    fun updateView(){
        binding.guitarDetailTitle.text = model.getCurrentGuitar().name
        binding.guitarDetailDescription.text = model.getCurrentGuitar().description


        //todo: load image from Firebase storage. if there is no url, the use the icon
        if(model.getCurrentGuitar().storageURIString.isEmpty()){
            binding.guitarDetailImage.setImageDrawable(getResources().getDrawable(R.drawable.guitar_icon2))
        }else{
            //todo: load image from firestore. this is temporary
            binding.guitarDetailImage.load(model.getCurrentGuitar().storageURIString)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.delete_note -> {
            model.removeCurrentGuitar()
            findNavController().navigate(R.id.nav_guitars_list)
            true
        }

        R.id.edit_note -> {
            findNavController().navigate(R.id.nav_guitars_edit)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

}