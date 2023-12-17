package com.example.filmopedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.example.filmopedia.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        auth = Firebase.auth

        binding.tvSignUp.setOnClickListener(){
            val i = Intent(this, SignUpActivity::class.java)
            startActivity(i)
        }

        binding.btnSignIn.setOnClickListener(){
            login()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // already user
            GoHome()
        }
    }

    private fun login() {

        val email = binding.emailIn.text.toString()
        val password = binding.passwordIn.text.toString()


        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAGY", "signInWithEmail:success")
                    val user = auth.currentUser
//                    updateUI(user)
                    GoHome()

                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAGY", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
//                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        TODO("Not yet implemented")
    }



    private fun GoHome() {
//        Toast.makeText(this, "GoHome", Toast.LENGTH_SHORT).show()
        val i = Intent(this, FragmentActivity::class.java)
        startActivity(i)
    }
}