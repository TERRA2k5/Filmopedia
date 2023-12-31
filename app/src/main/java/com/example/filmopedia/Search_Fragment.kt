package com.example.filmopedia

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.example.filmopedia.data.MovieResponse
import com.example.filmopedia.data.MoviesData
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmopedia.databinding.FragmentHomeBinding
import com.example.filmopedia.databinding.FragmentSearchBinding
import com.example.filmopedia.model.MyAdapter
import com.example.filmopedia.model.SearchAdapter
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
    private lateinit var adapter: SearchAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_, container, false)

        binding.searchBtn.clearFocus()


        binding.searchBtn.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        getRecycler(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText != null) {
                        getRecycler(newText)
                    }
                    return true

                    return true
                }

            }
        )

        return binding.root
    }

    fun getRecycler(key: String) {

        val retrofitbuilder = Retrofit.Builder()
            .baseUrl("https://moviesdatabase.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchInterface::class.java)


        val retrofitData = retrofitbuilder.getMoviesSearch(key)

        retrofitData.enqueue(object : Callback<MovieResponse?> {

            override fun onResponse(
                call: Call<MovieResponse?>,
                response: Response<MovieResponse?>
            ) {
                var responsebody = response.body()
                val movieList = responsebody?.results!!

//                binding.progressBar.visibility = View.GONE

                adapter = SearchAdapter(context!!, movieList)
                binding.rvSearchContainer.adapter = adapter

                binding.rvSearchContainer.layoutManager = GridLayoutManager(context!!, 2)
            }

            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {
                Log.i("RetroFail", "Failure")
            }
        })
    }


}
