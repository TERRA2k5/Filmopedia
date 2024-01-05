package com.example.filmopedia

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.example.filmopedia.data.MovieResponse
import com.example.filmopedia.data.MoviesData
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmopedia.data.WatchListData
import com.example.filmopedia.databinding.FragmentHomeBinding
import com.example.filmopedia.databinding.FragmentSearchBinding
import com.example.filmopedia.model.MyAdapter
import com.example.filmopedia.model.WatchlistAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_, container, false)

        binding.searchBtn.clearFocus()
        binding.progressBarSearch.visibility = View.GONE



        var sortOption: String? = null

        val sort = resources.getStringArray(R.array.Sort)

        if (binding.btnSort != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, sort
            )
            binding.btnSort.adapter = adapter
        }

        binding.btnSort.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

//                Log.i("hsh" , sort[position].toString())

                if (sort[position].toString() == "Latest First"){
                    sortOption = "year.decr"
                }

                else if (sort[position].toString() == "Old First"){
                    sortOption = "year.incr"
                }

                else {
                    sortOption = null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

                TODO("Not yet implemented")


            }

        }







        binding.searchBtn.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        var page = 1

                        var sorting: String? = sortOption

                        binding.progressBarSearch.visibility = View.VISIBLE

                        if (sorting != null){
                            getRecycler(query, page , sorting)

                        }
                        else{
                            getRecycler(query, page , null)

                        }


                        binding.imgNext.setOnClickListener() {
                            if (page < 20) {
                                binding.progressBarSearch.visibility = View.VISIBLE
                                page++
                                binding.page.text = page.toString()
                                getRecycler(query, page , sorting)
                            }

                        }
                        binding.nextBtn.setOnClickListener() {
                            if (page < 20) {
                                binding.progressBarSearch.visibility = View.VISIBLE

                                page++
                                binding.page.text = page.toString()
                                getRecycler(query, page , sorting)
                            }
                        }


                        binding.prevBtn.setOnClickListener() {
                            if (page > 1) {
                                binding.progressBarSearch.visibility = View.VISIBLE

                                page--
                                binding.page.text = page.toString()
                                getRecycler(query, page , sorting)
                            }
                        }
                        binding.prevBtn.setOnClickListener() {
                            if (page > 1) {
                                binding.progressBarSearch.visibility = View.VISIBLE

                                page--
                                binding.page.text = page.toString()
                                getRecycler(query, page , sorting)
                            }
                        }

                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText != "") {
                        var page = 1
                        var sorting: String? = sortOption


                        if (sorting != null){
                            getRecycler(newText!!, page , sorting)

                        }
                        else{
                            getRecycler(newText!!, page , null)

                        }

                        binding.imgNext.setOnClickListener() {
                            if (page < 20) {
                                binding.progressBarSearch.visibility = View.VISIBLE
                                page++
                                binding.page.text = page.toString()
                                getRecycler(newText, page , sorting)
                            }

                        }
                        binding.nextBtn.setOnClickListener() {
                            if (page < 20) {
                                binding.progressBarSearch.visibility = View.VISIBLE

                                page++
                                binding.page.text = page.toString()
                                getRecycler(newText, page ,sorting)
                            }
                        }


                        binding.prevBtn.setOnClickListener() {
                            if (page > 1) {
                                binding.progressBarSearch.visibility = View.VISIBLE

                                page--
                                binding.page.text = page.toString()
                                getRecycler(newText, page , sorting)
                            }
                        }
                        binding.prevBtn.setOnClickListener() {
                            if (page > 1) {
                                binding.progressBarSearch.visibility = View.VISIBLE

                                page--
                                binding.page.text = page.toString()
                                getRecycler(newText, page , sorting)
                            }
                        }
                    }
                    return true
                }

            }
        )

        return binding.root
    }




    fun getRecycler(key: String , page: Int , sorting: String?) {


        val retrofitbuilder = Retrofit.Builder()
            .baseUrl("https://moviesdatabase.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchInterface::class.java)


        val retrofitData = retrofitbuilder.getMoviesSearch(key , page , sorting)

        Log.i("TAGY" , sorting.toString())



        retrofitData.enqueue(object : Callback<MovieResponse?> {

            override fun onResponse(
                call: Call<MovieResponse?>,
                response: Response<MovieResponse?>
            ) {
                var responsebody = response.body()
                val movieList = responsebody?.results!!

                binding.progressBarSearch.visibility = View.GONE
                binding.noresult.setText("")
                adapter = MyAdapter(context!!, movieList )
                binding.rvSearchContainer.adapter = adapter

                if(adapter.itemCount == 0){
                    binding.noresult.setText("No Movies Found")
                }

                binding.rvSearchContainer.layoutManager = GridLayoutManager(context!!, 2)
            }

            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {
                Log.i("RetroFail", "Failure")
            }
        })
    }


}
