package com.example.filmopedia

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.filmopedia.databinding.ActivityProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    var image_uri: Uri? = null
    var storage = Firebase.storage
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        auth = Firebase.auth

        var email = auth.currentUser?.email.toString()

        email = email.replace(".", "")
        email = email.replace("@", "")


        val storageReference = storage.reference.child("Profiles").child(email)

        storageReference.downloadUrl.addOnSuccessListener {task ->

            Glide.with(this).load(task.toString()).into(binding.profileImg)
        }

        binding.profileCard.setOnClickListener() {

            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to change Profile Picture ?")

            builder.setPositiveButton("Yes") {

                    dailog, which ->
//
//                var i = Intent()
//                i.action = Intent.ACTION_GET_CONTENT
//                i.type = "image/*"
//                startActivityForResult(i, 1)
                var openGallary: Intent = Intent(Intent.ACTION_PICK , Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(openGallary , 1)

            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }

            val alertDialog = builder.create()
            // Show the Alert Dialog box
            alertDialog.show()


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                image_uri = data?.data
                binding.profileImg.setImageURI(image_uri)
                UploadImage(image_uri)
            }
        }
    }

    fun UploadImage(image: Uri?) {
        if (image == null) return

        auth = Firebase.auth

        var email = auth.currentUser?.email.toString()
//        Toast.makeText(this, email, Toast.LENGTH_SHORT).show()

        email = email.replace(".", "")
        email = email.replace("@", "")


        val storageReference = storage.reference.child("Profiles").child(email)

        storageReference.putFile(image).addOnCompleteListener{
            if (it.isSuccessful){
                storageReference.downloadUrl.addOnSuccessListener {

                    Toast.makeText(this, "Profile Updated.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

