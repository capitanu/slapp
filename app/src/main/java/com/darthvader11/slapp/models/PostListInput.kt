package com.darthvader11.slapp.models

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.darthvader11.slapp.R


data class Post(var title: String,
                var post: String,
                var drb: Int ,
                var drb2 : Int  ,
                var city : String) {

}

object Supplier3 {
    val posts = mutableListOf(
        Post("John Lewis", "We are playing in Östermalm tonight", R.drawable.johnlewis, R.drawable.ikris,"Stockholm,Sweden"),
        Post("John Lewis", "Our band needs a guitarist" , R.drawable.johnlewis , R.drawable.bass,"Stockholm,Sweden"),
        Post("John Lewis", "Are you a female vocal?", R.drawable.johnlewis,R.drawable.femalevocalist,"Stockholm,Sweden"),
        Post("John Lewis", "Guys, anyone interested in playing in a gig?", R.drawable.johnlewis,R.drawable.comm3,"Berlin,Germany")

    )
}
