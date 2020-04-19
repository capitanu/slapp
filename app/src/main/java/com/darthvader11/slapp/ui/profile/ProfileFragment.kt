package com.darthvader11.slapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.darthvader11.slapp.R
import com.darthvader11.slapp.ui.comment.PostlistFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darthvader11.slapp.adaptors.PostListAdapter
import com.darthvader11.slapp.models.Supplier3

class ProfileFragment : Fragment(), View.OnClickListener {
    lateinit var adapter: PostListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val recyclerPosts: RecyclerView = root.findViewById(R.id.recyclerPosts)
        setupRecyclerView(recyclerPosts)

        val submitButton: Button = root.findViewById(R.id.submitButton)
        submitButton.setOnClickListener(this)
        val backButton : ImageView = root.findViewById(R.id.backButton)
        backButton.setOnClickListener(this)
        
        val btnInstagram : ImageView = root.findViewById(R.id.btnInstagram)
        btnInstagram .setOnClickListener(this)
        val btnYoutube : ImageView = root.findViewById(R.id.btnYoutube)
        btnYoutube.setOnClickListener(this)
        val btnSpotify : ImageView = root.findViewById(R.id.btnSpotify)
        btnSpotify.setOnClickListener(this)
        return root
    }

    private fun setupRecyclerView(recyclerPosts: RecyclerView){


        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerPosts.layoutManager = layoutManager
        adapter = PostListAdapter(context!!, Supplier3.posts)
        recyclerPosts.adapter = adapter




    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backButton -> {
                fragmentManager?.popBackStack()
            }
            R.id.submitButton -> {

                //   val totalStarts = "total Stars :" + ratingBar.numStars
                val rating = "Rating :" +  ratingBar.rating
                Toast.makeText(context, rating, Toast.LENGTH_SHORT).show()

            }

                R.id.btnInstagram -> {
                val webIntent: Intent = Uri.parse("http://www.Instagram.com").let {
                        webpage -> Intent(Intent.ACTION_VIEW, webpage)
                }
                startActivity(webIntent)
            }
            R.id.btnYoutube -> {
                val webIntent: Intent = Uri.parse("http://www.youtube.com").let {
                        webpage -> Intent(Intent.ACTION_VIEW, webpage)
                }
                startActivity(webIntent)
            }
            R.id.btnSpotify -> {
                val webIntent: Intent = Uri.parse("http://www.spotify.com").let {
                        webpage -> Intent(Intent.ACTION_VIEW, webpage)
                }
                startActivity(webIntent)
            }

        }
    }
}