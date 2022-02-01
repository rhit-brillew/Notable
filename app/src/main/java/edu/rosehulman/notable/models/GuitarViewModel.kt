package edu.rosehulman.notable.models

import androidx.lifecycle.ViewModel
import edu.rosehulman.notable.models.Guitar

class  GuitarViewModel : ViewModel() {

    var guitars = ArrayList<Guitar>()
    var currentPos = 0
    fun getGuitarAt(pos:Int) = guitars[pos]
    fun getCurrentGuitar() = getGuitarAt(currentPos)
    fun addGuitar(g: Guitar?){
        val newGuitar = g ?: Guitar("", "", "")
        guitars.add(newGuitar)
    }
    fun updateCurrentGuitar(photoLocation:String,name:String,description:String){
        guitars[currentPos].location=photoLocation
        guitars[currentPos].name=name
        guitars[currentPos].description=description
    }
    fun removeCurrentGuitar(){
        guitars.removeAt(currentPos)
        currentPos=0
    }
    fun updatePos(pos: Int){
        currentPos = pos
    }
    fun size() = guitars.size
}