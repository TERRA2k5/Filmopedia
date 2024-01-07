package com.example.filmopedia.model

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmopedia.R
import com.example.filmopedia.data.MoviesData
import com.example.filmopedia.data.WatchListData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.database.values
import java.lang.StringBuilder
import kotlin.math.log


class MyAdapter(
    var context: Context,
    var movieList: List<MoviesData>,
    var watchlist: ArrayList<WatchListData>
) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    private lateinit var auth: FirebaseAuth
    var i: Int = 0


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imPoster: ImageView
        val tvTitle: TextView
        val tvYear: TextView
        val BookMark: CheckBox


        init {
            imPoster = itemView.findViewById(R.id.imPoster)
            BookMark = itemView.findViewById(R.id.Bookmark)
            tvYear = itemView.findViewById(R.id.tvYear)
            tvTitle = itemView.findViewById(R.id.tvTitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val v: View =
            LayoutInflater.from(context).inflate(R.layout.recycler_viewholder, parent, false)

        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return movieList.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = movieList[position]

        if (item.titleText != null) {
            holder.tvTitle.text = item.titleText.text
        }
//        Log.i("TAAGY", item.titleText.text)
        if (item.releaseYear != null) {
            holder.tvYear.text = "Year: ${item.releaseYear.year}"

        }
        if (item.primaryImage != null) {
            Glide.with(context).load(item.primaryImage.url).into(holder.imPoster)
        }


        /** checking if alreadv added **/

        while (i < watchlist.count()){
            if (watchlist[i].imdbID.toString() == item.id){
                holder.BookMark.isChecked = true
//                Toast.makeText(context, "${item.titleText.text}", Toast.LENGTH_SHORT).show()
            }
            i++
        }

        i = 0

        /******************************/

        holder.BookMark.setOnCheckedChangeListener { checkBox, isChecked ->
            val data = WatchListData(
                item.id,
                item.titleText.text,
                item.primaryImage.url,
                item.releaseYear.year
            )

            auth = Firebase.auth

            var email = auth.currentUser?.email.toString()


//            Toast.makeText(context, "$email", Toast.LENGTH_SHORT).show()
            email = email.replace(".", "")
            email = email.replace("[", "")
            email = email.replace("]", "")
            email = email.replace("#", "")
            email = email.replace("$", "")



            val myRef = FirebaseDatabase.getInstance().getReference(email)



            /** adding to realtime database **/

            if (isChecked) {
                Toast.makeText(context, "Added to WatchList", Toast.LENGTH_SHORT)
                    .show()
                myRef.child(item.id).setValue(data)

            } else {
                Toast.makeText(context, "Removed from WatchList", Toast.LENGTH_SHORT)
                    .show()
                myRef.child(item.id).removeValue()
            }
        }
//        Toast.makeText(context, "${watchlist.count()}", Toast.LENGTH_SHORT).show()


    }
}
