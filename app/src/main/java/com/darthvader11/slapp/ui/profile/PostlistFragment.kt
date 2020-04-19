
package com.darthvader11.slapp.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darthvader11.slapp.R
import com.darthvader11.slapp.adaptors.PostListAdapter
import com.darthvader11.slapp.models.Post
import com.darthvader11.slapp.models.Supplier3
import kotlinx.android.synthetic.main.fragment_postlist.*

class PostlistFragment : Fragment() {

    lateinit var adapter: PostListAdapter


    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_postlist, container, false)
        val recyclerPosts: RecyclerView = root.findViewById(R.id.recyclerPosts)
        setupRecyclerView(recyclerPosts)



        return root

    }

    private fun setupRecyclerView(recyclerPosts: RecyclerView){


        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerPosts.layoutManager = layoutManager
        adapter = PostListAdapter(context!!, Supplier3.posts)
        recyclerPosts.adapter = adapter




    }


}



