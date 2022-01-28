package edu.rosehulman.notable.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.notable.R
import edu.rosehulman.notable.models.Guitar
import edu.rosehulman.notable.models.GuitarViewModel
import edu.rosehulman.notable.ui.guitars.GuitarsListFragment

class GuitarsAdapter(val fragment: GuitarsListFragment) : RecyclerView.Adapter<GuitarsAdapter.GuitarsViewHolder>() {

    val model = ViewModelProvider(fragment.requireActivity()).get(GuitarViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuitarsViewHolder {
        //todo: this will need to be uncommented when row_guitar is created
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

    inner class GuitarsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //val guitarLocation
        //val guitarName
        //val guitarDescription

        init{
            itemView.setOnClickListener{
                model.updatePos(adapterPosition)
                //todo: this will have to change
                fragment.findNavController().navigate(R.id.nav_guitars_edit)
            }
        }

        fun bind(g: Guitar){
            //todo: figure out how to load guitar image from Firebase storage
            //this.guitarName.text = g.name
            //this.guitarDescription.text = g.description
            itemView.setBackgroundColor(
                if (adapterPosition % 2 == 0) {
                    fragment.requireContext().getColor(R.color.primaryColor)
                } else {
                    fragment.requireContext().getColor(R.color.secondaryColor)
                }
            )
        }
    }

}