package edu.rosehulman.notable.models

class Guitar(var location:String="", var name:String="", var description:String="") {
    override fun toString():String{
        return "'$name': $description"
    }
}