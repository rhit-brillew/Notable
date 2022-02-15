package edu.rosehulman.notable.models

data class Profile(
    var name:String = "",
    var phoneNumber:String="",
    var storageURIString:String = "",
    var hasCompletedSetup:Boolean=false) {

    companion object{
        const val COLLECTION_PATH = "User"
    }
}