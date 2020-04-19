package com.darthvader11.slapp.ui.ChatActivities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.darthvader11.slapp.MessagingNetwork.*
import com.darthvader11.slapp.Objects.UserLocalStore
import com.darthvader11.slapp.Objects.user_id
import com.darthvader11.slapp.R
import com.darthvader11.slapp.adaptors.MessageListAdapter
import com.darthvader11.slapp.server.GetUserCallback
import com.darthvader11.slapp.server.ServerRequest
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_message_list.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class ChatActivity : AppCompatActivity() {

    private lateinit var messageListAdapter: MessageListAdapter
    private val api = MessagingAPI()
    private val messagesHandler = Handler(Looper.getMainLooper())
    private var sessionID: Int? = null
    private var targetUserID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)

        initRecyclerView()

//        var serverRequest1 = ServerRequest(this, R.layout.activity_message_list)
//        val a = serverRequest1.getUserId(this)
//
//        println(a)
        this.sessionID = intent.getIntExtra("sessionID",0)
        this.targetUserID = intent.getIntExtra("userID", 0)


        //var serverRequest: ServerRequest = ServerRequest(this, R.layout.fragment_messages)
        //Log.v("userIdTest",serverRequest.getUserId(this).toString())
        Log.v("TESSST", "cmon pls?")
        var sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        var user_id: Int = sharedPreferences.getInt("user_id", 0)
        Log.v("user_id",user_id.toString())


        if (this.sessionID != 0) {
            getMessages(this.sessionID!!)
        } else {
            val targetUserId = this.targetUserID
            print("###### TargetUserId = $targetUserId")
            if (targetUserId != 0) {
                val messageSessionRequest = MessageSessionRequest(user_id, targetUserId!!)
                api.postSession(messageSessionRequest, object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        println("#### ERROR")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val jsonString = response.body!!.string()
                        val postSessionResponse: PostSessionResponse = Gson().fromJson(jsonString, PostSessionResponse::class.java)
                        val sessionId = postSessionResponse.session_id
                        sessionID = sessionId
                        getMessages(sessionId)
                    }
                })
            } else {
                println("Something went wrong")
            }
        }

        val editText = edittext_chatbox
        val sendButton: Button = button_chatbox_send
        sendButton.setOnClickListener {

            if (sessionID!! != 0) {
                val message = MessageRequest(editText.text.toString(), user_id, sessionID!!)

                api.postMessage(message, object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        print(response.isSuccessful)
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })

                editText.text.clear()
            }
        }

        messagesHandler.post(object : Runnable {
            override fun run() {
                if(sessionID!! != 0) {
                    api.getMessages(sessionID!!, object : Callback {
                        override fun onResponse(call: Call, response: Response) {
                            populateMessages(response)
                        }

                        override fun onFailure(call: Call, e: IOException) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                }
                messagesHandler.postDelayed(this, 1000)
            }
        })
    }

    private fun getMessages(sessionId: Int) {
        api.getMessages(sessionId, object : Callback {
            override fun onResponse(call: Call, response: Response) {
                populateMessages(response)
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun initRecyclerView() {

        reyclerview_message_list.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            messageListAdapter = MessageListAdapter()
            adapter = messageListAdapter
        }
    }

    private fun populateMessages(response: Response) {
            if (response.body != null) {
                val jsonString = response.body!!.string()
                val messageResponseList: List<MessageResponseForSession> = Gson().fromJson(jsonString, Array<MessageResponseForSession>::class.java).toList()
                this.runOnUiThread {
                messageListAdapter.submitList(messageResponseList)
                messageListAdapter.notifyDataSetChanged()
                reyclerview_message_list.scrollToPosition(messageListAdapter.items.size - 1)
            }
        }
    }
}
