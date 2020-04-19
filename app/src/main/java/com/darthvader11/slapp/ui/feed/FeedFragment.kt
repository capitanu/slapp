package com.darthvader11.slapp.ui.feed

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darthvader11.slapp.R
import com.darthvader11.slapp.adaptors.FeedAdapter
import com.darthvader11.slapp.models.Feed
import com.darthvader11.slapp.models.feedSupplier
import com.darthvader11.slapp.server.GetFeedCallback
import com.darthvader11.slapp.server.GetPostCallback
import com.darthvader11.slapp.server.ServerRequest
import com.darthvader11.slapp.ui.login.LoginActivity
import com.darthvader11.slapp.ui.newpost.NewpostFragment
import org.json.JSONArray
import org.json.JSONObject
import java.sql.Types.NULL

class FeedFragment : Fragment(), View.OnClickListener {

    lateinit var adapter: FeedAdapter
    lateinit var progressDialog: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_feed, container, false) as ConstraintLayout


        val createPost: AppCompatImageButton = root.findViewById(R.id.createPost)
        createPost.setOnClickListener(this)
        var logoutButton: Button = root.findViewById(R.id.btnLogout)
        logoutButton.setOnClickListener(this)


        val recyclerFeed: RecyclerView = root.findViewById(R.id.recyclerFeed)


        val feedTop: RelativeLayout = root.findViewById(R.id.feedTop)
        progressDialog = ProgressBar(context, null, android.R. attr.progressBarStyleLarge)
        var params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        //params.addRule(RelativeLayout.ALIGN_BASELINE)
        feedTop.addView(progressDialog, params)
        progressDialog.visibility = View.VISIBLE


        var serverRequest = ServerRequest(context!!,R.layout.fragment_feed)
        serverRequest.updateAllFeed(object: GetFeedCallback{
            override fun done(returnedCode: JSONArray) {

                Log.v("SIZES", feedSupplier.feedContent.size.toString())
                Log.v("SIZES", returnedCode.length().toString())
                if(feedSupplier.feedContent.size == returnedCode.length()) {
                    setupRecyclerView(recyclerFeed)
                    progressDialog.visibility = View.INVISIBLE
                    return
                }

                feedSupplier.feedContent.clear()


                for(i in 0 until returnedCode.length()){
                    Log.v("JObject",returnedCode[i].toString())
                    var jObject: JSONObject = returnedCode[i] as JSONObject

                    serverRequest.downloadImage(jObject.getString("Title"), object: GetPostCallback {
                        override fun done(returnedImage: Bitmap?) {
                            if(returnedImage != null) {
                                feedSupplier.feedContent.add(
                                    Feed(
                                        jObject.getString("Title"),
                                        "@" + jObject.getString("Author"),
                                        jObject.getInt("LikesCount"),
                                        jObject.getString("Tags"),
                                        jObject.getString("Description"),
                                        returnedImage,
                                        returnedImage,
                                        jObject.getString("Location"),
                                        jObject.getString("Genre"),
                                        jObject.getInt("post_id")
                                    )
                                )
                                Log.v("shouldBeAfterFeed", feedSupplier.feedContent.size.toString())
                                Log.v("added", "one feed item has been added")
                            }
                            if(returnedCode.length() == feedSupplier.feedContent.size) {
                                setupRecyclerView(recyclerFeed)
                                progressDialog.visibility = View.INVISIBLE
                            }

                        }

                    })

                    Log.v("download", jObject.getString("Title"))
                }
                Log.v("JARRAY", returnedCode.length().toString())
                Log.v("afterFeed","PLS TELL ME IT GOT HERE")

                //Log.v("afterFeed", feedSupplier.feedContent[0].toString())
            }
        })



        return root
    }


    fun setupRecyclerView(recyclerFeed: RecyclerView){

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerFeed.layoutManager = layoutManager
        adapter = FeedAdapter(context!!, feedSupplier.feedContent, this)
        recyclerFeed.adapter = adapter

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.createPost -> {
                Toast.makeText(context, "This button is working", Toast.LENGTH_SHORT).show()
                val manager: FragmentManager? = fragmentManager
                val transaction: FragmentTransaction? = manager?.beginTransaction()
                transaction?.replace(R.id.nav_host_fragment, NewpostFragment(), NewpostFragment::class.java.simpleName)
                transaction?.addToBackStack(null)
                transaction?.commit()
            }
            R.id.btnLogout -> {

                var sharedPreferences = context?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                var editor = sharedPreferences?.edit()
                editor?.putString("email", "")
                editor?.apply()
                var intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }

        }


    }
}
