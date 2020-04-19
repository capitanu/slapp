package com.example.messagebox.Model

import android.media.Image
import java.util.*

class MessagePreview(profile: Image?, name: String, message: String) {

    var isRead: Boolean = true
    var senderProfilePic = profile
        private set

    val senderName = name

    var senderLastMessage = message
        private set
}
