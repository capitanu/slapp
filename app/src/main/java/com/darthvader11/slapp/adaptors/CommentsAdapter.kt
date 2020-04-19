package com.darthvader11.slapp.adaptors


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.darthvader11.slapp.R
import com.darthvader11.slapp.models.Comment
import com.darthvader11.slapp.showToast
import kotlinx.android.synthetic.main.item_comment.view.*


class CommentsAdapter(val context: Context, private val comments: List<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = comments[position]
        holder.setData(comment, position)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        return MyViewHolder(view)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentComment: Comment? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                currentComment?.let {

                    context.showToast(currentComment!!.author + " clicked!")
                }

            }

        }

        fun setData(comment: Comment?, pos: Int) {
            comment?.let {
                itemView.txtProfile.text = comment.author
                itemView.txtComment.text = comment.comment
                //itemView.profilePic.setImageResource(comment.drb)
            }
            this.currentComment = comment
            this.currentPosition = pos
        }
    }
}