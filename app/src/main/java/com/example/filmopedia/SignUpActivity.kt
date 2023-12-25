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
import java.util.regex.Matcher
import java.util.regex.Pattern

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

        binding.tvSignIn.setOnClickListener(){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }

        binding.btnSignUp.setOnClickListener() {
            val email = binding.emailUp.text.toString()

            if (emailValidator(email)){
                if (Check()){
                    //
                }

                else{ CreateUser() }
            }

            else{
                binding.emailUp.error = "Enter valid Email ID"
            }
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
                    binding.tvError.setText("Something went wrong. Check you Internet Connection")
                }
            }
    }

    private fun Check(): Boolean{
        val name = binding.username.text.toString()

        val email = binding.emailUp.text.toString()

        val pass = binding.passwordUp.text.toString()

        var condition = false

        if (name == ""){
            binding.username.error = "This is Required"
            condition = true
        }

        if (email == ""){
            binding.emailUp.error = "This is Required"
            condition = true
        }

        if (pass ==""){
            binding.passwordUp.error = "This is Required"
            condition = true
        }

        return condition
    }

    public fun emailValidator(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN: String =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }
}