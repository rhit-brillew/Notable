package edu.rosehulman.notable.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import edu.rosehulman.notable.R
import edu.rosehulman.notable.models.Guitar
import edu.rosehulman.notable.models.GuitarViewModel
import edu.rosehulman.notable.ui.guitars.GuitarsListFragment

class GuitarsAdapter(val fragment: GuitarsListFragment) : RecyclerView.Adapter<GuitarsAdapter.GuitarsViewHolder>() {

    val model = ViewModelProvider(fragment.requireActivity()).get(GuitarViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuitarsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_guitar, parent, false)
        return GuitarsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuitarsViewHolder, position: Int){
        holder.bind(model.getGuitarAt(position))
    }

    override fun getItemCount() = model.size()

    fun addGuitar(g: Guitar?){
        model.addGuitar(g)
        this.notifyDataSetChanged()
    }

    fun setCurrentToLastGuitar(){
        model.updatePos(model.size()-1)
    }

    inner class GuitarsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val guitarImage = itemView.findViewById<ImageView>(R.id.row_guitar_image_view)
        val guitarName = itemView.findViewById<TextView>(R.id.row_guitar_name_text)
        val guitarDescription = itemView.findViewById<TextView>(R.id.row_guitar_description_text)

        init{
            itemView.setOnClickListener{
                model.updatePos(adapterPosition)
                //todo: this will have to change
                fragment.findNavController().navigate(R.id.nav_guitars_edit)
            }
        }

        fun bind(g: Guitar){
            //todo: figure out how to load guitar image from Firebase storage
            if(!g.storageURIString.isEmpty()){
                //todo: load image from Firebase storage here. this is temporary
                //this.guitarImage.setImageURI(Uri.parse(model.getCurrentGuitar().location))
                this.guitarImage.load(Uri.parse(g.storageURIString))
            }else{
                this.guitarImage.load(R.drawable.guitar_icon2)
                //this.guitarImage.setImageDrawable(fragment.getResources().getDrawable(R.drawable.guitar_icon2))
            }
            this.guitarName.text = g.name
            this.guitarDescription.text = g.description
            itemView.setBackgroundColor(
                if (adapterPosition % 2 == 0) {
                    fragment.requireContext().getColor(R.color.primaryColor)
                } else {
                    fragment.requireContext().getColor(R.color.secondaryColor)
                }
            )
        }
    }

    //firebase
    fun addListener(fragmentName: String){
        model.addListener(fragmentName){ notifyDataSetChanged()}
    }

    fun removeListener(fragmentName: String){
        model.removeListener(fragmentName)
    }

}