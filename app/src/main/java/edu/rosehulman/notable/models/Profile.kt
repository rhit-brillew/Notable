package edu.rosehulman.notable.models

data class Profile(
    var name:String = "",
    var storageURIString:String = "") {

    companion object{
        const val COLLECTION_PATH = "User"
    }
}