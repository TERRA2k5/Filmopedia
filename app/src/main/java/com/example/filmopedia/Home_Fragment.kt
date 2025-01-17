package com.example.filmopedia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmopedia.data.MovieResponse
import com.example.filmopedia.data.WatchListData
import com.example.filmopedia.databinding.FragmentHomeBinding
import com.example.filmopedia.model.MyAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Collections


class Home_Fragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: MyAdapter

    //    lateinit var watchlist: ArrayList<WatchListData>

    lateinit var dbRef: DatabaseReference
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    /********/
    var page: Int = 1
    var list: String? = "top_boxoffice_200"
    var sortOption: String? = "year.decr"
    var genreOption: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_, container, false)

        database = Firebase.database.reference

        binding.tvNoresult.setText("")
        binding.noresult.visibility = View.GONE


        /** making sort spinner **/

        val sort = resources.getStringArray(R.array.Sort)
        if (binding.btnSort != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_activated_1, sort
            )
            binding.btnSort.adapter = adapter
        }
        binding.btnSort.setSelection(0)


        /** making genre spinner **/


        val genre = resources.getStringArray(R.array.Genre)
        if (binding.btnFilter != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, genre
            )
            binding.btnFilter.adapter = adapter
        }
        binding.btnFilter.setSelection(0)


        /** Genre query **/


        binding.btnFilter.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                binding.progressBar2.visibility = View.VISIBLE

                if (genre[position].toString() != "All") {
                    genreOption = genre[position].toString()
                    page = 1
                    binding.page.setText(page.toString())

                } else {
                    genreOption = null
                    page = 1
                    binding.page.setText(page.toString())


                }

                getRecycler(page, list, sortOption, genreOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        /** Sorting query **/




        /** changing pages **/
        binding.btnSort.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (sort[position].toString() == "Popular") {
                    sortOption = "year.decr"
                    page = 1
                    binding.page.setText(page.toString())
                    list = "top_boxoffice_200"
                    binding.progressBar2.visibility = View.VISIBLE
                }

                if (sort[position].toString() == "Latest First") {
                    sortOption = "year.decr"
                    list = null
                    page = 1
                    binding.page.setText(page.toString())
                    binding.progressBar2.visibility = View.VISIBLE

                } else if (sort[position].toString() == "Old First") {
                    sortOption = "year.incr"
                    list = null
                    page = 1
                    binding.page.setText(page.toString())
                    binding.progressBar2.visibility = View.VISIBLE

                } else {
                    sortOption = "year.decr"
                }

                getRecycler(page, list, sortOption, genreOption)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        binding.imgNext.setOnClickListener() {
            if (page < 20) {
                binding.progressBar2.visibility = View.VISIBLE
                page++
                binding.page.text = page.toString()
                getRecycler(page, list, sortOption, genreOption)
            }

        }

        binding.imgPrev.setOnClickListener() {
            if (page > 1) {
                binding.progressBar2.visibility = View.VISIBLE

                page--
                binding.page.text = page.toString()
                getRecycler(page, list, sortOption, genreOption)
            }
        }

        /****** Refresh *********************/

        binding.swipeRefresh.setOnRefreshListener {

            getRecycler(page , list , sortOption , genreOption )

        }

        /************************************/
        return binding.root
    }
    
    override fun onResume() {
        super.onResume()

//        Toast.makeText(context, "Resume", Toast.LENGTH_SHORT).show()
        binding.progressBar2.visibility = View.VISIBLE
        binding.rvHomeContainer.layoutManager = null
        getRecycler(page,list,sortOption,genreOption)

    }



     private fun getRecycler(page: Int, list: String?, sorting: String?, genreOption: String?) {

        val retrofitbuilder = Retrofit.Builder()
            .baseUrl("https://moviesdatabase.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesInterface::class.java)


        val retrofitData =
            retrofitbuilder.getMoviesList(page, list, sorting, genreOption)

        retrofitData.enqueue(object : Callback<MovieResponse?> {

            override fun onResponse(
                call: Call<MovieResponse?>,
                response: Response<MovieResponse?>
            ) {
                var responsebody = response?.body()

                if (responsebody?.results != null) {
                    val movieList = responsebody?.results!!


                    /*****************************************/


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

                                /*********/
                                binding.tvNoresult.setText("")
                                binding.noresult.visibility = View.GONE
                                binding.imgNext.isClickable = true
                                binding.progressBar2.visibility = View.GONE
                                binding.swipeRefresh.isRefreshing = false
                                /*********/

                                adapter = MyAdapter(context!!, movieList!!, watchlist!!)
                                binding.rvHomeContainer.adapter = adapter

                                /*** No Result ***/
                                if (adapter.itemCount == 0){
                                    binding.noresult.visibility = View.VISIBLE
                                    binding.tvNoresult.setText("No Movies Found")
                                    binding.imgNext.isClickable = false
                                }

                                binding.rvHomeContainer.layoutManager =
                                    GridLayoutManager(context!!, 2)


                            } else {

                                // when watchlist is null


                                binding.progressBar2.visibility = View.GONE

                                adapter = MyAdapter(context!!, movieList!!, watchlist!!)
                                binding.rvHomeContainer.adapter = adapter

                                if (adapter.itemCount == 0){
                                    binding.imgNext.isClickable = false
                                }

                                binding.rvHomeContainer.layoutManager =
                                    GridLayoutManager(context!!, 2)
                            }

                            adapter.setOnClickListener(object : MyAdapter.onClickListener {
                                override fun onClick(position: Int) {

                                    val item = movieList[position]

                                    var i = Intent(context, DetailsActivity::class.java)

                                    if (item.titleText != null) {
                                        i.putExtra("title", item.titleText.text.toString())
                                    }

                                    if (item.primaryImage != null) {
                                        i.putExtra("url", item.primaryImage.url.toString())
                                    }
                                    if (item.releaseYear != null) {
                                        i.putExtra("year", item.releaseYear.year)
                                    }
                                    if (item.id != null) {
                                        i.putExtra("id", item.id.toString())
                                    }
                                    startActivity(i)
                                }
                            })
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

                    /*****************************************/


                }
            }

            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {
                Log.i("RetroFail", "Failure")
            }

        })
    }

}