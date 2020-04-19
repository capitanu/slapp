package com.darthvader11.slapp.adaptors


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.darthvader11.slapp.R
import com.darthvader11.slapp.models.Post

import kotlinx.android.synthetic.main.postlist_item.view.*


class PostListAdapter(val context: Context, private val posts: List<Post>) :
    RecyclerView.Adapter<PostListAdapter.MyViewHolder>() {


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = posts[position]
        holder.setData(post, position)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.postlist_item, parent, false)
        return MyViewHolder(view)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentPost: Post? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                currentPost?.let {

                 //   context.showToast(currentPost!!.title + " clicked!")
                 //   val referenceToFragment = CommentsFragment()
                 //   referenceToFragment.fragmentManager
               }

            }

        }

        fun setData(post: Post?, pos: Int) {
            post?.let {
                itemView.txtProfile.text = post.title
                itemView.txtDescription.text = post.post
                itemView.profilePic.setImageResource(post.drb)
                itemView.postPic.setImageResource(post.drb2)
                itemView.txtInputLoc.text=post.city
            }
            this.currentPost = post
            this.currentPosition = pos
        }
    }
}