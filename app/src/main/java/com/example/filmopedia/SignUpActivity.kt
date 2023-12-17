package com.example.filmopedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.example.filmopedia.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        database = Firebase.database.reference
        auth = Firebase.auth

        binding.btnSignUp.setOnClickListener() {
//            Check()
            CreateUser()
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

    private fun GoHome() {
        val i = Intent(this, FragmentActivity::class.java)
        startActivity(i)
    }

    fun CreateUser() {

        val email = binding.emailUp.text.toString()

        val password = binding.passwordUp.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    auth.signOut()
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAGY", "createUserWithEmail:success")
                    val user = auth.currentUser
//                    updateUI(user)
                    GoHome()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAGY", "createUserWithEmail:failure", task.exception)
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

//    private fun Check(){
//        val email = binding.emailUp.text.toString()
//
//        val pass = binding.passwordUp.text.toString()
//
//        if (email == ""){
//            binding.emailUp.error = "This is Required"
//        }
//
//        if (pass ==""){
//            binding.passwordUp.error = "This is Required"
//        }
//    }
}