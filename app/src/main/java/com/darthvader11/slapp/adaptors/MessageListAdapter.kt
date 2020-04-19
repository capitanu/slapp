package com.darthvader11.slapp.adaptors

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.darthvader11.slapp.data.model.DummmySelfProfile
import com.darthvader11.slapp.data.model.UserMessage
import kotlinx.android.synthetic.main.item_message_received.view.*
import kotlinx.android.synthetic.main.item_message_received.view.text_message_body
import kotlinx.android.synthetic.main.item_message_received.view.text_message_time
import kotlinx.android.synthetic.main.item_message_sent.view.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.darthvader11.slapp.MessagingNetwork.MessageResponse
import com.darthvader11.slapp.MessagingNetwork.MessageResponseForSession
import com.darthvader11.slapp.Objects.user_id
import com.darthvader11.slapp.R
import com.darthvader11.slapp.data.model.DummyChatProvider
import com.darthvader11.slapp.data.model.User
import java.util.*
import kotlin.collections.ArrayList


class MessageListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2

    var items: List<MessageResponseForSession> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)
            return SentMessageHolder(view)
        }
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message_received, parent, false)
        return ReceivedMessageHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = items[position]

        when (holder.itemViewType) {

            VIEW_TYPE_MESSAGE_RECEIVED -> {
                (holder as ReceivedMessageHolder).bind(message)
            }

            VIEW_TYPE_MESSAGE_SENT -> {
                (holder as SentMessageHolder).bind(message)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        val userMessage = items[position]

        if (userMessage.user_id == user_id.user_id) { //CHANGE USER ID HERE
            return VIEW_TYPE_MESSAGE_SENT
        }
        return VIEW_TYPE_MESSAGE_RECEIVED

    }

    fun submitList(messageList: List<MessageResponseForSession>) {
        items = messageList
    }

//    fun addItemtoList(message: MessageResponse) {
//        items.add(message)
//        notifyDataSetChanged()
//    }

    private inner class SentMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val messageText = itemView.received_text_message_body
        val timeText = itemView.received_text_message_time

        fun bind(message: MessageResponseForSession) {

            messageText.text = message.content
            timeText.text = message.timestamp.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
        }
    }

    private inner class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val messageText = itemView.text_message_body
        val timeText = itemView.text_message_time
        val nameText = itemView.text_message_name
        val profileImage = itemView.image_message_profile


        fun bind(message: MessageResponseForSession) {

            messageText.text = message.content
            timeText.text = message.timestamp.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
            nameText.text = message.user_name
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(message.profile_picture)
                .into(profileImage)

        }
    }
}