package com.example.filmopedia

// FRAGMENT CLASS

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmopedia.data.MovieResponse
import com.example.filmopedia.data.WatchListData
import com.example.filmopedia.databinding.FragmentHomeBinding
import com.example.filmopedia.model.MyAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Home_Fragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: MyAdapter
//    lateinit var watchlist: ArrayList<WatchListData>

    private lateinit var database: DatabaseReference
//    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_, container, false)

        database = Firebase.database.reference

        var page: Int = 1
        var list: String? = null


        val sort = resources.getStringArray(R.array.Sort)
        if (binding.btnSort != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, sort
            )
            binding.btnSort.adapter = adapter
        }




        val genre = resources.getStringArray(R.array.Genre)
        if (binding.btnFilter != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, genre
            )
            binding.btnFilter.adapter = adapter
        }





        var genreOption: String? = null



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
                }
                else{
                    genreOption = null
                }
                binding.btnSort.setSelection(0)

                getRecycler(page , list, "year.decr" , genreOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }












        var sortOption: String? = null



        binding.btnSort.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (sort[position].toString() == "Popular"){
                    sortOption = "year.decr"
                    list = "top_boxoffice_200"
                    binding.progressBar2.visibility = View.VISIBLE
                    getRecycler(page, list , sortOption , genreOption)                }

                if (sort[position].toString() == "Latest First") {
                    sortOption = "year.decr"
                    list = null
                    binding.progressBar2.visibility = View.VISIBLE
                    getRecycler(page, list , sortOption , genreOption)

                } else if (sort[position].toString() == "Old First") {
                    sortOption = "year.incr"
                    list=null
                    binding.progressBar2.visibility = View.VISIBLE
                    getRecycler(page, list , sortOption, genreOption)

                } else {
                    sortOption = "year.decr"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }










        getRecycler(page, "top_boxoffice_200" , "year.decr", null)



        binding.imgNext.setOnClickListener() {
            if (page < 20) {
                binding.progressBar2.visibility = View.VISIBLE
                page++
                binding.page.text = page.toString()
                getRecycler(page,list, sortOption, genreOption)
            }

        }
        binding.nextBtn.setOnClickListener() {
            if (page < 20) {
                binding.progressBar2.visibility = View.VISIBLE

                page++
                binding.page.text = page.toString()
                getRecycler(page,list, sortOption, genreOption)
            }
        }


        binding.prevBtn.setOnClickListener() {
            if (page > 1) {
                binding.progressBar2.visibility = View.VISIBLE

                page--
                binding.page.text = page.toString()
                getRecycler(page,list, sortOption, genreOption)
            }
        }
        binding.prevBtn.setOnClickListener() {
            if (page > 1) {
                binding.progressBar2.visibility = View.VISIBLE

                page--
                binding.page.text = page.toString()
                getRecycler(page,list, sortOption, genreOption)
            }
        }

        return binding.root
    }

    fun getRecycler(page: Int, list: String? ,  sorting: String?, genreOption: String?) {

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
                var responsebody = response.body()
//                watchlist = arrayListOf<WatchListData>()

                val movieList = responsebody?.results!!
                binding.progressBar2.visibility = View.GONE

                adapter = MyAdapter(context!!, movieList , /* watchlist */)
                binding.rvHomeContainer.adapter = adapter

                binding.rvHomeContainer.layoutManager = GridLayoutManager(context!!, 2)

            }

            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {
                Log.i("RetroFail", "Failure")
            }
        })
    }
}