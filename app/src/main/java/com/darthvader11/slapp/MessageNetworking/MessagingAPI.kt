package com.darthvader11.slapp.MessagingNetwork

import android.net.Uri
import com.google.gson.Gson
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody

class MessagingAPI {

    private val client = OkHttpClient()

    fun getSessions(userId: Int, callback: Callback): Call {
        val url = "http://capitanu.tech/GetSessions.php"
        val uri = Uri.parse(url)
            .buildUpon()
            .appendQueryParameter("user_id", userId.toString())
            .build()

        val request = Request.Builder()
            .url(uri.toString())
            .addHeader("Content-Type", "application/json")
            .get()
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun postMessage(messageRequest: MessageRequest, callback: Callback): Call {
        val jsonBody = Gson().toJson(messageRequest)
        val url = "http://capitanu.tech/PostMessage.php"
        val uri = Uri.parse(url)
            .buildUpon()
            .build()

        val request = Request.Builder()
            .url(uri.toString())
            .addHeader("Content-Type", "application/json")
            .post(jsonBody.toRequestBody())
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun getMessages(sessionId: Int, callback: Callback): Call {
        val url = "http://capitanu.tech/GetMessages.php"
        val uri = Uri.parse(url)
            .buildUpon()
            .appendQueryParameter("session_id", sessionId.toString())
            .build()

        val request = Request.Builder()
            .url(uri.toString())
            .addHeader("Content-Type", "application/json")
            .get()
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun postSession(messageSessionRequest: MessageSessionRequest, callback: Callback): Call {
        val jsonBody = Gson().toJson(messageSessionRequest)
        val url = "http://capitanu.tech/PostSession.php"
        val uri = Uri.parse(url)
            .buildUpon()
            .build()

        val request = Request.Builder()
            .url(uri.toString())
            .addHeader("Content-Type", "application/json")
            .post(jsonBody.toRequestBody())
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun getUsers(callback: Callback): Call {
        val url = "http://capitanu.tech/GetUsersBgr.php"
        val uri = Uri.parse(url)
            .buildUpon()
            .build()

        val request = Request.Builder()
            .url(uri.toString())
            .addHeader("Content-Type", "application/json")
            .get()
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }
}