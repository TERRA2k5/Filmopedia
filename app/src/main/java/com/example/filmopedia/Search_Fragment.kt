package com.example.filmopedia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.example.filmopedia.data.MovieResponse
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmopedia.data.WatchListData
import com.example.filmopedia.databinding.FragmentSearchBinding
import com.example.filmopedia.model.MyAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


open class Search_Fragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MyAdapter
    lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_, container, false)

        binding.searchBtn.clearFocus()
        binding.progressBarSearch.visibility = View.GONE


        var page = 1
        binding.page.setText(page.toString())


        binding.searchBtn.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {


                        binding.progressBarSearch.visibility = View.VISIBLE
                        getRecycler(query, page)


                        binding.imgNext.setOnClickListener() {
                            if (page < 20) {
                                binding.progressBarSearch.visibility = View.VISIBLE
                                page++
                                binding.page.text = page.toString()
                                getRecycler(query, page)
                            }

                        }
                        binding.nextBtn.setOnClickListener() {
                            if (page < 20) {
                                binding.progressBarSearch.visibility = View.VISIBLE

                                page++
                                binding.page.text = page.toString()
                                getRecycler(query, page)
                            }
                        }


                        binding.prevBtn.setOnClickListener() {
                            if (page > 1) {
                                binding.progressBarSearch.visibility = View.VISIBLE

                                page--
                                binding.page.text = page.toString()
                                getRecycler(query, page)
                            }
                        }
                        binding.prevBtn.setOnClickListener() {
                            if (page > 1) {
                                binding.progressBarSearch.visibility = View.VISIBLE

                                page--
                                binding.page.text = page.toString()
                                getRecycler(query, page)
                            }
                        }

                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText != "") {
                        var page = 1
//                        var sorting: String? = sortOption


                        getRecycler(newText!!, page)

                        binding.imgNext.setOnClickListener() {
                            if (page < 20) {
                                binding.progressBarSearch.visibility = View.VISIBLE
                                page++
                                binding.page.text = page.toString()
                                getRecycler(newText!!, page)
                            }

                        }
                        binding.nextBtn.setOnClickListener() {
                            if (page < 20) {
                                binding.progressBarSearch.visibility = View.VISIBLE

                                page++
                                binding.page.text = page.toString()
                                getRecycler(newText!!, page)
                            }
                        }


                        binding.prevBtn.setOnClickListener() {
                            if (page > 1) {
                                binding.progressBarSearch.visibility = View.VISIBLE

                                page--
                                binding.page.text = page.toString()
                                getRecycler(newText!!, page)
                            }
                        }
                        binding.prevBtn.setOnClickListener() {
                            if (page > 1) {
                                binding.progressBarSearch.visibility = View.VISIBLE

                                page--
                                binding.page.text = page.toString()
                                getRecycler(newText!!, page)
                            }
                        }
                    }
                    return true
                }

            }
        )

        return binding.root
    }


    fun getRecycler(key: String, page: Int) {


        val retrofitbuilder = Retrofit.Builder()
            .baseUrl("https://moviesdatabase.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchInterface::class.java)


        val retrofitData = retrofitbuilder.getMoviesSearch(key, page)

//        Log.i("TAGY" , sorting.toString())


        retrofitData.enqueue(object : Callback<MovieResponse?> {

            override fun onResponse(
                call: Call<MovieResponse?>,
                response: Response<MovieResponse?>
            ) {
                var responsebody = response.body()

                if (responsebody?.results != null ){

                    val movieList = responsebody?.results!!


                    auth = Firebase.auth
                    var email = auth.currentUser?.email.toString()

                    email = email.replace(".", "")
                    email = email.replace("[", "")
                    email = email.replace("]", "")
                    email = email.replace("#", "")

                    val watchlist = arrayListOf<WatchListData?>()

                    dbRef = FirebaseDatabase.getInstance().getReference(email)

                    dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            watchlist?.clear()

                            if (snapshot.exists()) {
                                for (i in snapshot.children) {
                                    val data = i.getValue(WatchListData::class.java)
                                    watchlist?.add(data!!)
                                }

//                            Log.i("TAGY" , watchlist[0].imdbID.toString())


                                binding.progressBarSearch.visibility = View.GONE
                                binding.noresult.setText("")


                                adapter = MyAdapter(context!!, movieList, watchlist)
                                binding.rvSearchContainer.adapter = adapter

                                if (adapter.itemCount == 0) {
                                    binding.noresult.setText("No Movies Found")
                                }

                                binding.rvSearchContainer.layoutManager =
                                    GridLayoutManager(context!!, 2)


                            } else {


                                binding.progressBarSearch.visibility = View.GONE
                                binding.noresult.setText("")


                                adapter = MyAdapter(context!!, movieList, watchlist)
                                binding.rvSearchContainer.adapter = adapter

                                if (adapter.itemCount == 0) {
                                    binding.noresult.setText("No Movies Found")
                                }

                                binding.rvSearchContainer.layoutManager =
                                    GridLayoutManager(context!!, 2)
                            }

                            adapter.setOnClickListener(object: MyAdapter.onClickListener{
                                override fun onClick(position: Int) {

                                    val item = movieList[position]

                                    var i = Intent(context , DetailsActivity::class.java)

                                    if (item.titleText != null){
                                        i.putExtra("title" , item.titleText.text.toString())
                                    }

                                    if (item.primaryImage != null){
                                        i.putExtra("url" , item.primaryImage.url.toString())
                                    }
                                    if (item.releaseYear != null){
                                        i.putExtra("year" , item.releaseYear.year)
                                    }
                                    if (item.id != null){
                                        i.putExtra("id" , item.id.toString())
                                    }
                                    startActivity(i)
                                }
                            })
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }

            }

            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {
                Log.i("RetroFail", "Failure")
            }
        })
    }


}
