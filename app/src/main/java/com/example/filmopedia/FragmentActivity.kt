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
import com.example.filmopedia.databinding.ActivityFragmentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class FragmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_fragment)
        auth = Firebase.auth

        binding.bottomNav.setOnItemSelectedListener{

            when(it.itemId){

                R.id.home -> {
                    ReplaceFrag(Home_Fragment())
                }

                R.id.watchlist ->{
                    ReplaceFrag()
                }
            }
            true

        }

    }

    private fun ReplaceFrag(fragment: Fragment){

        val fragTrans = supportFragmentManager.beginTransaction()

        fragTrans.replace(R.id.container , fragment)
        fragTrans.commit()
    }


}