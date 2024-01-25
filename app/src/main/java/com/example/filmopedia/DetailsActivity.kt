package com.example.filmopedia

import android.content.Intent
import android.net.Uri
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

    /*******/
    var movie_title: String? = null
    var image_url: String? = null
    var imdb: String? = null
    var release: Int?= null
    /*******/

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_details)

        val getData: Bundle? = intent.extras

        binding.checkWatchlist.isClickable = false
        val title = getData?.get("title")
        val url = getData?.get("url")
        val id = getData?.get("id")
        val year = getData?.get("year")



        binding.tvDetails.setOnClickListener(){

            if(id != null) {
                var i = Intent(Intent.ACTION_VIEW)
                i.setData(Uri.parse("https://www.imdb.com/title/" + id.toString()))
                startActivity(i)
            }
            else{
                Toast.makeText(this, "Could not fetch details.", Toast.LENGTH_SHORT).show()
            }

        }


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
                binding.checkWatchlist.isClickable = true

                if (response.body()?.results != null && response.body()?.results?.averageRating != null ) {
                    binding.rating.setText(response.body()?.results?.averageRating.toString())
                }

                if (title != null){
                    binding.movieTitle.setText(title.toString())
                }

                if (url != null){
                    Glide.with(baseContext).load(url.toString()).into(binding.poster)

                }
                if (year != null){
                    binding.year.setText("Release Year : ${year.toString()}")
                }

                if (id != null){
                    binding.imdb.setText("IMDB ID: ${id}")
                }

                auth = Firebase.auth
                var email = auth.currentUser?.email.toString()
//                Toast.makeText(this@DetailsActivity, email.toString(), Toast.LENGTH_SHORT).show()

                email = email.replace(".", "")
                email = email.replace("[", "")
                email = email.replace("]", "")
                email = email.replace("#", "")

                val watchlist = arrayListOf<WatchListData?>()

                val data: WatchListData


                if (url != null){
                    image_url = url.toString()
                }

                if (title.toString() != null){
                    movie_title = title.toString()
                }

                if (id.toString() != null){
                    imdb = id.toString()
                }

                if (year != null){
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
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })

                binding.checkWatchlist.setOnCheckedChangeListener{checkbox , isChecked ->

                    if (isChecked) {
//                        Toast.makeText(this@DetailsActivity, "Added to WatchList", Toast.LENGTH_SHORT)
//                            .show()
                        dbRef.child(id.toString()).setValue(data)

                    } else {
//                        Toast.makeText(this@DetailsActivity, "Removed from WatchList", Toast.LENGTH_SHORT)
//                            .show()
                        dbRef.child(id.toString()).removeValue()
                    }
                }
            }


            override fun onFailure(call: Call<RatingData?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        binding.btnShare.setOnClickListener(){

            if (movie_title != null){
                var i = Intent(Intent.ACTION_SEND)
                i.setType("text/plain")
                i.putExtra(Intent.EXTRA_SUBJECT, "Check out $movie_title on IMDB")
                i.putExtra(
                    Intent.EXTRA_TEXT,
                    "Found a amazing movie ${movie_title} on Filmopedia!! \n https://www.imdb.com/title/${id.toString()}"
                )
                startActivity(i)
            }
        }

    }
}