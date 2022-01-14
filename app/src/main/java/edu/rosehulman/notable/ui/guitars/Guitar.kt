package edu.rosehulman.notable.ui.guitars

class Guitar(var location:String="", var name:String="", var description:String="") {
    override fun toString():String{
        return "'$name': $description"
    }
}