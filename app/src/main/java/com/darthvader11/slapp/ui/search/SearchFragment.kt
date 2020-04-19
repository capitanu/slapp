package com.darthvader11.slapp.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darthvader11.slapp.R
import com.darthvader11.slapp.adaptors.SearchAdapter
import com.darthvader11.slapp.models.Feed
import com.darthvader11.slapp.models.Supplier2
import com.darthvader11.slapp.models.feedSupplier
import com.darthvader11.slapp.ui.post.PostFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment(), View.OnClickListener {

    lateinit var adapter: SearchAdapter
    lateinit var recyclerSearch: RecyclerView
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerSearch = root.findViewById(R.id.recyclerSearch)

        val sendSearch: ImageButton = root.findViewById(R.id.sendSearch)
        sendSearch.setOnClickListener(this)





        return root

    }



    private fun setupRecyclerView(recyclerSearch: RecyclerView) {


        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerSearch.layoutManager = layoutManager
        adapter = SearchAdapter(context!!, Supplier2.searchResults, this)
        recyclerSearch.adapter = adapter


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sendSearch -> {
                setupRecyclerView(recyclerSearch)
                Supplier2.searchResults.clear()
                val searchText = inputSearch.text.toString()
                if(searchText.equals("")){
                    Toast.makeText(context,"You need to type something in", Toast.LENGTH_SHORT).show()
                    return
                }
                for(element: Feed in feedSupplier.feedContent){
                    Log.v("searchtest", element.postTitle)
                    if(element.genre.contains(searchText)) {
                        Supplier2.searchResults.add(element)
                        continue
                    }
                    if(element.postTitle.contains(searchText)){
                        Supplier2.searchResults.add(element)
                        continue
                    }
                    if(element.tags.contains(searchText)){
                        Supplier2.searchResults.add(element)
                        continue
                    }
                    if(element.location.contains(searchText)){
                        Supplier2.searchResults.add(element)
                        continue
                    }
                }
                if(Supplier2.searchResults.isEmpty()){
                    Toast.makeText(context,"Unfortunatelly, there were no results", Toast.LENGTH_SHORT).show()
                }
                setupRecyclerView(recyclerSearch)
                inputSearch.setText("")
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                inputSearch.clearComposingText()

            }

        }


    }
}