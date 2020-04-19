package com.darthvader11.slapp.ui.ChatActivities

import android.graphics.drawable.ClipDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.darthvader11.slapp.MessagingNetwork.MessageRequest
import com.darthvader11.slapp.MessagingNetwork.MessageSessionResponse
import com.darthvader11.slapp.MessagingNetwork.MessagingAPI
import com.darthvader11.slapp.MessagingNetwork.UserResponse
import com.darthvader11.slapp.R
import com.darthvader11.slapp.adaptors.StartNewChatAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_messega_box.recycler_view
import kotlinx.android.synthetic.main.activity_start_new_chat.*
import kotlinx.android.synthetic.main.fragment_messages.edit_text_of_message_search
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class StartNewChatActivity : AppCompatActivity() {

    private lateinit var startNewCharAdapter: StartNewChatAdapter
    private val api = MessagingAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_new_chat)

        initRecyclerView()
        setSearchBar()
       // startNewCharAdapter.submitList(DummyDataProvider.dummyMessagePreview())


        api.getUsers(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call, response: Response) {
                populateUsers(response)
            }
        })


//        val editText = edittext_newchatbox
//        val sendButton: Button = button_newchatbox_send
//        sendButton.setOnClickListener {
//
//            val message = MessageRequest(editText.text.toString(), 21, 54)
//            api.postMessage(message, object: Callback {
//                override fun onResponse(call: Call, response: Response) {
//                    print(response.isSuccessful)
//                }
//                override fun onFailure(call: Call, e: IOException) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//            })
//        }
    }

    private fun initRecyclerView() {

        val itemDecor = DividerItemDecoration(this, ClipDrawable.HORIZONTAL)
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@StartNewChatActivity)
            startNewCharAdapter = StartNewChatAdapter()
            adapter = startNewCharAdapter
            addItemDecoration(itemDecor)
        }
    }

    private fun populateUsers(response: Response) {
            if (response.body != null) {
                val jsonString = response.body!!.string()
                val sessionResponseList: List<UserResponse> = Gson().fromJson(jsonString, Array<UserResponse>::class.java).toList()
                this.runOnUiThread {
                startNewCharAdapter.submitList(sessionResponseList)
                startNewCharAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setSearchBar() {

        val editText = edit_text_of_message_search

        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {

                var list = ArrayList<UserResponse>()

                startNewCharAdapter.items.forEach { element ->
                    p0?.let {
                        if (element.user_name.length >= it.length && it.isNotEmpty() ){
                            if (element.user_name.contains(it.subSequence(0,it.length), ignoreCase = true)) {
                                list.add(element)
                                startNewCharAdapter.submitList(list)
                                startNewCharAdapter.notifyDataSetChanged()
                            } else {
                                startNewCharAdapter.submitList(list)
                                startNewCharAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }
}

