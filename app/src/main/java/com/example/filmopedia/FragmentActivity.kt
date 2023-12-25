package com.example.filmopedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmopedia.data.MovieResponse
import com.example.filmopedia.data.MoviesData
import com.example.filmopedia.databinding.ActivityFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class FragmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fragment)
        auth = Firebase.auth

        ReplaceFrag(Home_Fragment())

        val retrofitService = ServiceAPI.getInstance().create(MoviesInterface::class.java)

        GlobalScope.launch {
            val result = retrofitService.getMoviesList()
            val  list = result.body()?.results?.listIterator()
            if (list != null){

                while (list.hasNext()){
                    val listItem = list.next()
                    Log.i("TAGY" , listItem.titleText.text.toString())
                }


            }
            // Checking the results

        }
        binding.bottomNav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.home -> {

                    ReplaceFrag(Home_Fragment())
                }

                R.id.watchlist -> {
                    ReplaceFrag(WatchList_Fragment())
                }

                R.id.search -> {
                    ReplaceFrag(Search_Fragment())
                }
            }
            true

        }

    }

    private fun ReplaceFrag(fragment: Fragment) {

        val fragTrans = supportFragmentManager.beginTransaction()

        fragTrans.replace(R.id.container, fragment)
        fragTrans.commit()
    }


}