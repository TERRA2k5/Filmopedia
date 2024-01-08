package com.example.filmopedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.filmopedia.data.MovieResponse
import com.example.filmopedia.data.MoviesData

import com.example.filmopedia.databinding.ActivityFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference


class FragmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentBinding
    private lateinit var auth: FirebaseAuth


//    override fun onBackPressed() {
//        super.onBackPressed()
//        // Close the app when the back button is pressed
//        finish()
//    }



    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fragment)
        auth = Firebase.auth

        ReplaceFrag(Home_Fragment())
//        finish()


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
        fragTrans.addToBackStack(null)
        fragTrans.replace(R.id.container, fragment)
        fragTrans.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        menuInflater.inflate(R.menu.top_menu , menu)

        return super.onCreateOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout ) {

            auth = Firebase.auth

            auth.signOut()


            val i: Intent = Intent(this , MainActivity::class.java)
            finish()
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)

    }


}