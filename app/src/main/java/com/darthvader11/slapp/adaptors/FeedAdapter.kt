package com.darthvader11.slapp.adaptors

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.darthvader11.slapp.R
import com.darthvader11.slapp.models.Feed
import com.darthvader11.slapp.models.Post
import com.darthvader11.slapp.ui.feed.FeedFragment
import com.darthvader11.slapp.ui.post.PostFragment
import kotlinx.android.synthetic.main.item_feed.view.*

class FeedAdapter (val context: Context, private val feedContent: List<Feed>, val fragment: Fragment):
        RecyclerView.Adapter<FeedAdapter.MyViewHolder>(){
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val feed = feedContent[position]
        holder.setData(feed, position)
    }

    override fun getItemCount(): Int {
        return feedContent.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_feed,parent, false)
        return MyViewHolder(view)
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var currentPost: Feed? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                currentPost?.let {

                    val args = Bundle()
                    args.putString("post_id", feedContent[currentPosition].post_id.toString())

                    val fragmentPost = PostFragment()
                    fragmentPost.arguments = args
                    val fragment = fragment
                    val manager = fragment.fragmentManager
                    val transaction: FragmentTransaction? = manager?.beginTransaction()
                    transaction?.remove(FeedFragment())
                    transaction?.replace(R.id.nav_host_fragment, fragmentPost, PostFragment::class.java.simpleName)
                    transaction?.addToBackStack(null)
                    transaction?.commit()
                }
            }
        }

        fun setData(feed: Feed?, pos: Int){
            feed?.let {
                itemView.feedTitle.text = feed.postTitle
                itemView.postAuthor.text = feed.author + " | "
                itemView.postLikes.text = feed.likesCount.toString() + " likes | "
                itemView.postGenre.text = feed.genre
                itemView.feedPic.setImageBitmap(feed.postPic)
            }
            this.currentPost = feed
            this.currentPosition = pos
        }
    }
}