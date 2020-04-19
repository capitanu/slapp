package com.darthvader11.slapp.ui.comment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darthvader11.slapp.Objects.User
import com.darthvader11.slapp.Objects.UserLocalStore
import com.darthvader11.slapp.R
import com.darthvader11.slapp.adaptors.CommentsAdapter
import com.darthvader11.slapp.models.Comment
import com.darthvader11.slapp.models.Supplier
import com.darthvader11.slapp.server.GetCommentCallback
import com.darthvader11.slapp.server.ServerRequest
import kotlinx.android.synthetic.main.fragment_comments.*
import org.json.JSONArray
import org.json.JSONObject

class CommentsFragment : Fragment(), View.OnClickListener {

    lateinit var adapter: CommentsAdapter
    var post_id: Int = 0

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_comments, container, false)
        val recyclerComments: RecyclerView = root.findViewById(R.id.recyclerComments)
        setupRecyclerView(recyclerComments)

        val sendComment : ImageButton = root.findViewById(R.id.sendComment)
        sendComment.setOnClickListener(this)

        var args = arguments
        if(args != null){
            post_id = args.getString("post_id")!!.toInt()
        }

        Supplier.comments.clear()

        var serverRequest = ServerRequest(context!!,R.layout.fragment_comments)
        serverRequest.fetchComments(post_id, object: GetCommentCallback{
            override fun done(returnedCode: JSONArray) {
                for(i in 0 until returnedCode.length()){

                    var jObject = returnedCode[i] as JSONObject
                    Log.v("commentTestJSON", jObject.toString())
                    Supplier.comments.add(Comment(
                        jObject.getString("Author"),
                        jObject.getString("Text")
                    ))
                }
                setupRecyclerView(recyclerComments)
            }

        })


        return root

    }

    private fun setupRecyclerView(recyclerComments: RecyclerView){


        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerComments.layoutManager = layoutManager
        adapter = CommentsAdapter(context!!, Supplier.comments)
        recyclerComments.adapter = adapter



    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.sendComment -> {
                val message = inputComment.text.toString()
                //Supplier.comments.add(Supplier.comments.lastIndex + 1)//comment here)
                var userlogin: UserLocalStore = UserLocalStore(context!!)
                var user: User = userlogin.getLoggedInUser()
                var comment: Comment = Comment(user.username, message)
                Supplier.comments.add(comment)
                setupRecyclerView(recyclerComments)
                var serverRequest = ServerRequest(context!!,R.layout.fragment_comments)
                serverRequest.addComment(comment, post_id)
                adapter.notifyDataSetChanged()
                inputComment.setText("")
                inputComment.clearComposingText()
            }



        }

    }


}