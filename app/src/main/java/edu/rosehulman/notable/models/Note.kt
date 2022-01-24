package edu.rosehulman.notable.models

data class Note(var title: String, var description: String,
                var tab: String = "", var videoUrl: String = "", var isSelected: Boolean = false) {

}