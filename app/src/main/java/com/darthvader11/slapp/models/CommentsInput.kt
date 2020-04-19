package com.darthvader11.slapp.models



data class Comment(var author: String, var comment: String) {

}

object Supplier {
    val comments = mutableListOf<Comment>()
}
