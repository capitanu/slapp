package com.darthvader11.slapp.ui.post

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.darthvader11.slapp.Objects.User
import com.darthvader11.slapp.Objects.UserLocalStore
import com.darthvader11.slapp.R
import com.darthvader11.slapp.models.Feed
import com.darthvader11.slapp.models.Supplier
import com.darthvader11.slapp.models.feedSupplier
import com.darthvader11.slapp.server.GetFeedCallback
import com.darthvader11.slapp.server.GetLikeCallback
import com.darthvader11.slapp.server.ServerRequest
import com.darthvader11.slapp.ui.comment.CommentsFragment
import com.darthvader11.slapp.ui.home.HomeFragment
import com.darthvader11.slapp.ui.newpost.NewpostFragment
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class PostFragment : Fragment(), View.OnClickListener {

    lateinit var element: Feed
    lateinit var postLikes: TextView
    lateinit var user: User
    lateinit var argsComment: Bundle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_post, container, false)

        val btnComment : Button =  root.findViewById(R.id.btnComment)
        btnComment.setOnClickListener(this)
        val btnShare : Button = root.findViewById(R.id.btnShare)
        btnShare.setOnClickListener(this)
        val btnApply : Button = root.findViewById(R.id.btnApply)
        btnApply.setOnClickListener(this)
        val backButton : ImageView = root.findViewById(R.id.backButton)
        backButton.setOnClickListener(this)
        val likeButton: Button = root.findViewById(R.id.btnLike)
        likeButton.setOnClickListener(this)


        var args = arguments
        argsComment = Bundle()
        argsComment.putString("post_id", args!!.get("post_id").toString())


        //if(args != null) {
          //  Supplier.comments[3].comment = args.getString("post_id").toString()
        //}

        element = Feed(args?.getString("post_id")!!.toInt())
        var index: Int = feedSupplier.feedContent.binarySearch(element)

        element = feedSupplier.feedContent[index]

        var postTitle = root.findViewById<TextView>(R.id.titleText)
        postTitle.text = element.postTitle
        var profilePic = root.findViewById<ImageView>(R.id.profilePic)
        profilePic.setImageBitmap(element.profilePicture)
        var postPic = root.findViewById<ImageView>(R.id.postPic)
        postPic.setImageBitmap(element.postPic)
        var postAuthor = root.findViewById<TextView>(R.id.profileText)
        postAuthor.text = element.author
        postLikes = root.findViewById<TextView>(R.id.likesCount)
        postLikes.text = element.likesCount.toString()
        var postDescription = root.findViewById<TextView>(R.id.txtDescription)
        postDescription.text = element.description
        var postLocation = root.findViewById<TextView>(R.id.txtInputLoc)
        postLocation.text = element.location
        var postGenre = root.findViewById<TextView>(R.id.txtInputGenre)
        postGenre.text = element.genre


        var userlogin: UserLocalStore = UserLocalStore(context!!)
        user = userlogin.getLoggedInUser()


        return root
    }



    override fun onClick(v: View?) {
        when (v?.id){
            R.id.backButton -> {
                fragmentManager?.popBackStack()
            }
            R.id.btnComment -> {
                Toast.makeText(context, "DIZ WORKS", Toast.LENGTH_SHORT).show()
                var commentsFragment = CommentsFragment()
                commentsFragment.arguments = argsComment
                val manager: FragmentManager? = fragmentManager
                val transaction: FragmentTransaction? = manager?.beginTransaction()
                transaction?.replace(R.id.nav_host_fragment, commentsFragment , CommentsFragment::class.java.simpleName  )
                transaction?.addToBackStack(null)
                transaction?.commit()
            }
            R.id.btnShare -> {
                val intent = Intent()
                val message = "capitanu.tech/Post.html"
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, message)
                intent.type = "text/plain"

                startActivity(Intent.createChooser(intent, "Share to: "))
            }

            R.id.btnApply -> {
                Toast.makeText(context, "DIZ WORKS aswell", Toast.LENGTH_SHORT).show()
                val manager: FragmentManager? = fragmentManager
                val transaction: FragmentTransaction? = manager?.beginTransaction()
                transaction?.replace(R.id.nav_host_fragment, NewpostFragment() , NewpostFragment::class.java.simpleName  )
                transaction?.addToBackStack(null)
                transaction?.commit()
            }
            R.id.btnLike -> {

                var serverRequest1 = ServerRequest(context!!,R.layout.fragment_feed)
                serverRequest1.checkLikes(user.username, element.post_id, object : GetLikeCallback{
                    override fun done(returnedCode: Boolean) {
                        if(returnedCode == true){
                        element.likesCount++
                        postLikes.text = element.likesCount.toString()
                        var serverRequest = ServerRequest(context!!,R.layout.fragment_feed)
                        serverRequest.updateLikes(element.likesCount, element.post_id)}
                        else {
                            Toast.makeText(context, "You have already liked this post", Toast.LENGTH_SHORT).show()
                        }

                    }


                })



            }

        }

    }
}