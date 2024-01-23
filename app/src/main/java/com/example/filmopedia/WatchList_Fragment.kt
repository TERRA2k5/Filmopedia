package com.example.filmopedia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmopedia.data.WatchListData
import com.example.filmopedia.databinding.FragmentWatchListBinding
import com.example.filmopedia.model.WatchlistAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class WatchList_Fragment : Fragment() {

    private lateinit var binding: FragmentWatchListBinding
    private lateinit var adapter: WatchlistAdapter
    private lateinit var auth: FirebaseAuth
    lateinit var dbRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_watch_list_, container, false)

        getWatchRecycler()

        binding.swipeRefresh.setOnRefreshListener {
            getWatchRecycler()
        }

        return binding.root
    }

     fun getWatchRecycler(){

        auth = Firebase.auth
        var email = auth.currentUser?.email.toString()

        email = email.replace(".", "")
        email = email.replace("[", "")
        email = email.replace("]", "")
        email = email.replace("#", "")

        dbRef = FirebaseDatabase.getInstance().getReference(email)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {

        /****Sending Data only at create *****/

            override fun onDataChange(snapshot: DataSnapshot) {
                val watchlist = mutableListOf<WatchListData>()


                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val data = i.getValue(WatchListData::class.java)
                        watchlist.add(data!!)

                    }

                    adapter = WatchlistAdapter(context!!)

                    /*** SENDING DATA ***/
                    adapter.differ.submitList(watchlist)
                    /*********/

                    binding.rvWatchList?.adapter = adapter

                    binding.rvWatchList?.layoutManager = GridLayoutManager(context, 2)

                    binding.swipeRefresh.isRefreshing = false

                    adapter.setOnClickListener(object :
                        WatchlistAdapter.onClickListener {
                        override fun onClick(position: Int) {

                            val item = adapter.differ.currentList[position]

                            var i = Intent(context, DetailsActivity::class.java)

                            if (item.title != null) {
                                i.putExtra("title", item.title.toString())
                            }

                            if (item.url != null) {
                                i.putExtra("url", item.url.toString())
                            }
                            if (item.year != null) {
                                i.putExtra("year", item.year)
                            }
                            if (item.imdbID != null) {
                                i.putExtra("id", item.imdbID.toString())
                            }
                            startActivity(i)
                        }
                    })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}