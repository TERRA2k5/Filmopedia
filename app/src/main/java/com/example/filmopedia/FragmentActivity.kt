package com.example.filmopedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

import com.example.filmopedia.databinding.ActivityFragmentBinding
import com.example.filmopedia.model.ViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class FragmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentBinding
    private lateinit var auth: FirebaseAuth

    lateinit var viewPager: ViewPager

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()

            finishAffinity()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fragment)
        auth = Firebase.auth

//        ReplaceFrag(Home_Fragment())


        /*****************************************/

        viewPager = findViewById(R.id.container)
        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNav)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    this.viewPager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.search -> {
                    this.viewPager.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.watchlist -> {
                    this.viewPager.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                bottomNavigation.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }


    /*****************************************/

//    private fun ReplaceFrag(fragment: Fragment) {
//
//        val fragTrans = supportFragmentManager.beginTransaction()
//
//        fragTrans.addToBackStack(null)
//        fragTrans.replace(R.id.container, fragment)
//        fragTrans.commit()
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        menuInflater.inflate(R.menu.top_menu, menu)

        return super.onCreateOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout) {

            auth = Firebase.auth

            auth.signOut()


            val i: Intent = Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(i)

        }


        else if (item.itemId == R.id.profile){

            val i: Intent = Intent(this, ProfileActivity::class.java)
//            finish()
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)

    }


}