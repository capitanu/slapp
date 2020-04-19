package com.darthvader11.slapp.MessagingNetwork

import android.media.Image

data class MessageResponse (
    var id: Int,
    var content: String,
    var timestamp: String,
    var user_id: Int,
    var user_name: String,
    var profile_picture: String? = null
)

data class MessageRequest(
    var content: String,
    var user_id: Int,
    var session_id: Int
)

data class MessageSessionResponse (
    var id: String,
    var last_updated: String,
    var messages: List<MessageResponse>)

data class MessageSessionRequest (
    var user_id: Int,
    var target_user_id: Int
)

data class PostSessionResponse(
    var session_id: Int
)

data class UserResponse (
    var id: Int,
    var user_name: String,
    var profile_picture: String? = null
)

data class MessageResponseForSession (
    var id: Int,
    var content: String,
    var timestamp: String,
    var user_id: Int,
    var session_id: Int,
    var user_name: String,
    var profile_picture: String?
)