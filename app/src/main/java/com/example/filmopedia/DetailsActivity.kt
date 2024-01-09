package com.example.filmopedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.filmopedia.data.RatingData
import com.example.filmopedia.data.WatchListData
import com.example.filmopedia.databinding.ActivityDetailsBinding
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

class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding
    lateinit var dbRef: DatabaseReference
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_details)

        val getData: Bundle? = intent.extras

        val title = getData?.get("title")
        val url = getData?.get("url")
        val id = getData?.get("id")
        val year = getData?.get("year")






        val retrofitbuilder = Retrofit.Builder()
            .baseUrl("https://moviesdatabase.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RatingInterface::class.java)


        val retrofitData =
            retrofitbuilder.getRating(id.toString())

        retrofitData.enqueue(object : Callback<RatingData?> {
            override fun onResponse(call: Call<RatingData?>, response: Response<RatingData?>) {
//                Log.i("TAGY" , response.body()?.results?.averageRating.toString())

                binding.progressBar.visibility = View.GONE
                if (response.body()?.results != null && response.body()?.results?.averageRating != null ) {
                    binding.rating.setText(response.body()?.results?.averageRating.toString())
                }

                if (title != null){
                    binding.movieTitle.setText(title.toString())
                }

                if (url != null){
                    Glide.with(this@DetailsActivity).load(url).into(binding.poster)

                }
                if (year != null){
                    binding.year.setText("Release Year : ${year}")
                }

                if (id != null){
                    binding.imdb.setText("IMDB ID: ${id}")
                }

                auth = Firebase.auth
                var email = auth.currentUser?.email.toString()

                email = email.replace(".", "")
                email = email.replace("[", "")
                email = email.replace("]", "")
                email = email.replace("#", "")

                val watchlist = arrayListOf<WatchListData?>()

                val data: WatchListData

                var image_url: String? = null
                var movie_title: String? = null
                var imdb: String? = null
                var release: Int?= null


                if (url != null){
                    image_url = url.toString()
                }

                if (movie_title != null){
                    movie_title = title.toString()
                }

                if (imdb != null){
                    imdb = id.toString()
                }

                if (release != null){
                    release = year.hashCode()
                }


                data = WatchListData(
                    imdb,
                    movie_title,
                    image_url,
                    release
                )

                dbRef = FirebaseDatabase.getInstance().getReference(email)

                dbRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        watchlist?.clear()

                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                val data = i.getValue(WatchListData::class.java)
                                watchlist?.add(data!!)
                            }

                            var i =0

                            while (i < watchlist.count()){
                                if (watchlist[i]?.imdbID.toString() == id.toString()){
                                    binding.checkWatchlist.isChecked = true
                                }
                                i++
                            }

                            binding.checkWatchlist.setOnCheckedChangeListener{checkbox , isChecked ->

                                if (isChecked) {
                                    Toast.makeText(this@DetailsActivity, "Added to WatchList", Toast.LENGTH_SHORT)
                                        .show()
                                    dbRef.child(id.toString()).setValue(data)

                                } else {
                                    Toast.makeText(this@DetailsActivity, "Removed from WatchList", Toast.LENGTH_SHORT)
                                        .show()
                                    dbRef.child(id.toString()).removeValue()
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }


            override fun onFailure(call: Call<RatingData?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })


    }
}