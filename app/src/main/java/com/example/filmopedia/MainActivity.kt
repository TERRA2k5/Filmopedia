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
import java.util.regex.Matcher
import java.util.regex.Pattern

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
            finish()
            startActivity(i)
        }

        binding.forget.setOnClickListener(){
            val i = Intent(this, ForgetPassword::class.java)
            startActivity(i)
        }

        binding.btnSignIn.setOnClickListener(){

            val email = binding.emailIn.text.toString()
            if (emailValidator(email)){
                if (Check()){
                }

                else{ login() }
            }

            else{
                binding.emailIn.error = "Enter valid Email ID"
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

    private fun login() {

        val email = binding.emailIn.text.toString()
        val password = binding.passwordIn.text.toString()


        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAGY", "signInWithEmail:success")
                    val user = auth.currentUser
//                    Toast.makeText(this, "${user?.email.toString()}", Toast.LENGTH_SHORT).show()
                    GoHome()

                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAGY", "signInWithEmail:failure", task.exception)
                    binding.tvError.setText("Email or Password entered is incorrect")
                }
            }
    }




    private fun GoHome() {
//        Toast.makeText(this, "GoHome", Toast.LENGTH_SHORT).show()
        val i = Intent(this, FragmentActivity::class.java)
        finish()
        startActivity(i)
    }

    private fun Check(): Boolean{

        val email = binding.emailIn.text.toString()

        val pass = binding.passwordIn.text.toString()

        var condition = false


        if (email == ""){
            binding.emailIn.error = "This is Required"
            condition = true
        }

        if (pass ==""){
            binding.passwordIn.error = "This is Required"
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