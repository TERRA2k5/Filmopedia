package com.example.filmopedia

// FRAGMENT CLASS

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmopedia.data.MovieResponse
import com.example.filmopedia.databinding.FragmentHomeBinding
import com.example.filmopedia.model.MyAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Home_Fragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: MyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_, container, false)

        var page: Int = 1

        getRecycler(page)

        binding.imgNext.setOnClickListener() {
            page++
            binding.page.text = page.toString()
            getRecycler(page)
        }
        binding.nextBtn.setOnClickListener() {
            page++
            binding.page.text = page.toString()
            getRecycler(page)
        }


        binding.prevBtn.setOnClickListener() {
            if (page > 1) {
                page--
                binding.page.text = page.toString()
                getRecycler(page)
            }
        }
        binding.prevBtn.setOnClickListener() {
            if (page > 1) {
                page--
                binding.page.text = page.toString()
                getRecycler(page)
            }
        }





        return binding.root
    }

    fun getRecycler(page: Int) {

        val retrofitbuilder = Retrofit.Builder()
            .baseUrl("https://moviesdatabase.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesInterface::class.java)


        val retrofitData = retrofitbuilder.getMoviesList(page, "top_boxoffice_200")

        retrofitData.enqueue(object : Callback<MovieResponse?> {

            override fun onResponse(
                call: Call<MovieResponse?>,
                response: Response<MovieResponse?>
            ) {
                var responsebody = response.body()
                val movieList = responsebody?.results!!

                if (movieList != null) {
                    adapter = MyAdapter(context!!, movieList)
                    binding.rvHomeContainer.adapter = adapter

                    binding.rvHomeContainer.layoutManager = GridLayoutManager(context!!, 2)
                }
            }

            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {
                Log.i("RetroFail", "Failure")
            }
        })
    }
}