package com.darthvader11.slapp.adaptors

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.darthvader11.slapp.R
import com.darthvader11.slapp.models.Feed
import com.darthvader11.slapp.models.Search
import com.darthvader11.slapp.showToast
import com.darthvader11.slapp.ui.feed.FeedFragment
import com.darthvader11.slapp.ui.post.PostFragment
import kotlinx.android.synthetic.main.item_search.view.*


class SearchAdapter(val context: Context, private val searchs: List<Feed>, val fragment: Fragment) :
    RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val search = searchs[position]
        holder.setData(search, position)
    }

    override fun getItemCount(): Int {
        return searchs.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)
        return MyViewHolder(view)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentSearch: Feed? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                currentSearch?.let {

                    context.showToast(currentSearch!!.postTitle + " clicked!")

                    val args = Bundle()
                    args.putString("post_id", searchs[currentPosition].post_id.toString())

                    val fragmentPost = PostFragment()
                    fragmentPost.arguments = args
                    val fragment = fragment
                    val manager = fragment.fragmentManager
                    val transaction: FragmentTransaction? = manager?.beginTransaction()
                    transaction?.remove(FeedFragment())
                    transaction?.replace(R.id.nav_host_fragment, fragmentPost, PostFragment::class.java.simpleName)
                    transaction?.addToBackStack(null)
                    transaction?.commit()
                }

            }

        }

        fun setData(search: Feed?, pos: Int) {
            search?.let {
                itemView.titleSearch.text = search.postTitle
                itemView.authorSearch.text = search.author
                itemView.locationSearch.text = search.location
                itemView.profilePic.setImageBitmap(search.postPic)
            }
            this.currentSearch = search
            this.currentPosition = pos
        }
    }
}