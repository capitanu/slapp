package com.darthvader11.slapp.ui.messages

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.darthvader11.slapp.MessagingNetwork.MessageRequest
import com.darthvader11.slapp.MessagingNetwork.MessageResponse
import com.darthvader11.slapp.MessagingNetwork.MessageSessionResponse
import com.darthvader11.slapp.MessagingNetwork.MessagingAPI
import com.darthvader11.slapp.Objects.user_id
import com.darthvader11.slapp.R
import com.darthvader11.slapp.ui.ChatActivities.StartNewChatActivity
import com.example.messagebox.Model.DummyDataProvider
import com.example.messagebox.View.MessageBoxRecyclerAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_messega_box.recycler_view
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_messages.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okhttp3.internal.toImmutableList
import java.io.IOException

class MessagesFragment : Fragment() {

    private lateinit var messagePreviewAdapter: MessageBoxRecyclerAdapter
    private val api = MessagingAPI()
    private val messagesHandler = Handler(Looper.getMainLooper())


    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_messages, container, false)
        Log.v("user_id",user_id.toString())

        return root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        initFabButton()
        Log.v("user_id", user_id.user_id.toString())
        api.getSessions(user_id.user_id, object: Callback {
            override fun onResponse(call: Call, response: Response) {
                populateSessions(response)

            }
            override fun onFailure(call: Call, e: IOException) {
                println(e)
            }
        })
        setSearchBar()

        messagesHandler.post(object : Runnable {
            override fun run() {


                api.getSessions(user_id.user_id, object: Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                    override fun onResponse(call: Call, response: Response) {
                        populateSessions(response)
                    }
                })

                messagesHandler.postDelayed(this, 1000)
            }
        })
    }


    private fun initRecyclerView() {

        val itemDecor = DividerItemDecoration(activity, HORIZONTAL)
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            messagePreviewAdapter = MessageBoxRecyclerAdapter()
            adapter = messagePreviewAdapter
            addItemDecoration(itemDecor)
        }

    }

    private fun populateSessions(response: Response) {
            if (response.body != null) {
                val jsonString = response.body!!.string()
                val sessionResponseList: List<MessageSessionResponse> = Gson().fromJson(jsonString, Array<MessageSessionResponse>::class.java).toList()
                activity?.runOnUiThread {
                messagePreviewAdapter.submitList(sessionResponseList)
                messagePreviewAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initFabButton() {
        val floatingActionButton = fabMessages
        floatingActionButton.setOnClickListener {

//            val message = MessageRequest("testtestetsetsetsetse", 21, 54)
//
//            api.postMessage(message, object: Callback {
//                override fun onResponse(call: Call, response: Response) {
//                    print(response.isSuccessful)
//                }
//                override fun onFailure(call: Call, e: IOException) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//            })

            val intent = Intent(activity, StartNewChatActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSearchBar() {

//        val editText = edit_text_of_message_search
//
//        editText.addTextChangedListener(object : TextWatcher {
//
//            override fun afterTextChanged(p0: Editable?) {
//
//                var list = ArrayList<MessageSessionResponse>()
//
//                messagePreviewAdapter.items.forEach { element ->
//                    p0?.let {
//                        if (element.messages[0]..length >= it.length && it.isNotEmpty() ){
//                            if (element.messages[0].username.contains(it.subSequence(0,it.length), ignoreCase = true)) {
//                                list.add(element)
//                                messagePreviewAdapter.submitList(list)
//                                messagePreviewAdapter.notifyDataSetChanged()
//                            } else {
//                                messagePreviewAdapter.submitList(list)
//                                messagePreviewAdapter.notifyDataSetChanged()
//                            }
//                        }
//                    }
//                }
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
////                api.getSessions(21, object: Callback {
////                    override fun onResponse(call: Call, response: Response) {
////                        populateSessions(response)
////                    }
////                    override fun onFailure(call: Call, e: IOException) {
////                        println(e)
////                    }
////                })
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//        })
//    }
    }
}

