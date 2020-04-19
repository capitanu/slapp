package com.darthvader11.slapp.models

import android.graphics.Bitmap

data class Feed(var postTitle: String,
                var author: String,
                var likesCount: Int,
                var tags: String,
                var description: String,
                var postPic: Bitmap?,
                var profilePicture: Bitmap?,
                var location: String,
                var genre: String,
                var post_id: Int) : Comparable<Feed>{
    constructor(postTitle: String,
                author: String,
                likesCount: Int,
                tags: String,
                description: String,
                postPic: Bitmap?,
                profilePicture: Bitmap?,
                location: String,
                genre: String) : this(postTitle,author,likesCount,tags,description,postPic,profilePicture,location,genre, 100)
    constructor(post_id: Int) : this("","", 0,"", "", null, null, "", "", post_id)

    override fun compareTo(other: Feed): Int {
        if(this.post_id > other.post_id)
            return 1
        if(this.post_id < other.post_id)
            return -1
        return 0
    }


}

object feedSupplier{
    val feedContent = mutableListOf<Feed>(
        //Feed("Looking for Drums for upcoming gig!", "John Appleseed",  8,  "Indie", R.drawable.drums)
    )
}

