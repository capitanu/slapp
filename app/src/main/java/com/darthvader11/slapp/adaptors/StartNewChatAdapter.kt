package com.darthvader11.slapp.adaptors


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.darthvader11.slapp.MessagingNetwork.UserResponse
import com.darthvader11.slapp.R
import com.darthvader11.slapp.ui.ChatActivities.ChatActivity
import kotlinx.android.synthetic.main.item_user.view.*
import kotlinx.android.synthetic.main.message_box_list_item.view.profile_picture

class StartNewChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<UserResponse> = ArrayList()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MessageBoxPreviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_user,
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
                    val userId = items[position].id
                    intent.putExtra("userID", userId)
                    println("############# userId = $userId")
                    holder.itemView.context.startActivity(intent)
                }
                holder.bind(items[position])
            }
        }
    }

    fun submitList(messageList: List<UserResponse>) {
        items = messageList
    }

    class MessageBoxPreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val profilePic = itemView.profile_picture
        val senderName = itemView.contact_name

        fun bind(userResponse: UserResponse) {

            senderName.text = userResponse.user_name

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(userResponse.profile_picture)
                .into(profilePic)
        }
    }
}

