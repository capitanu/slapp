package com.example.messagebox.View

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.darthvader11.slapp.MessagingNetwork.MessageSessionResponse
import com.darthvader11.slapp.R
import com.darthvader11.slapp.ui.ChatActivities.ChatActivity
import kotlinx.android.synthetic.main.message_box_list_item.view.*

class MessageBoxRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<MessageSessionResponse> = ArrayList()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MessageBoxPreviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.message_box_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {
            is MessageBoxPreviewViewHolder -> {
                holder.itemView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, ChatActivity::class.java)
                    val sessionID = items[position].id.toInt()
                    intent.putExtra("sessionID", sessionID)
 //                  intent.putExtra("targetUserID", items[position].id )
                    holder.itemView.context.startActivity(intent)
                }
                holder.bind(items[position])
            }
        }
    }

    fun submitList(messageListResponse: List<MessageSessionResponse>) {
        items = messageListResponse
    }

    class MessageBoxPreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val profilePic = itemView.profile_picture
        val senderLastMessage = itemView.sender_last_message
        val senderName = itemView.sender_name

        fun bind(messagePreviewResponse: MessageSessionResponse) {


            senderName.text = messagePreviewResponse.messages[0].user_name
            senderLastMessage.text = messagePreviewResponse.messages[messagePreviewResponse.messages.lastIndex].content

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(messagePreviewResponse.messages[0].profile_picture)
                .into(profilePic)
        }
    }
}

