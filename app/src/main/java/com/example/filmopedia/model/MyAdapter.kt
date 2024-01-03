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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.database.values


class MyAdapter(var context: Context, var movieList: List<MoviesData>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    val myRef = FirebaseDatabase.getInstance().getReference("WatchList")
//    lateinit var snapshot: DataSnapshot

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imPoster: ImageView
        val tvTitle: TextView
        val BookMark: CheckBox


        init {
            imPoster = itemView.findViewById(R.id.imPoster)
            BookMark = itemView.findViewById(R.id.Bookmark)
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

        val item = movieList.get(position)
        holder.tvTitle.text = item.titleText.text

        if (item.primaryImage != null) {
            Glide.with(context).load(item.primaryImage.url).into(holder.imPoster)
        }

//        if (listOf(myRef).listIterator().hasNext()){
//            Log.i("TAGY" , listOf(myRef.child(item.id)).listIterator().next().key.toString())
//
//        }
//        val base = "https://filmopedia-ff2e4-default-rtdb.firebaseio.com/WatchList/"
//        if (listOf(myRef).listIterator().next().toString() == base + item.id){
//            Toast.makeText(context, item.id, Toast.LENGTH_SHORT).show()
//            holder.BookMark!!.isChecked = true
//        }



        holder.BookMark.setOnCheckedChangeListener { checkBox, isChecked ->
            val id = WatchListData(item.id , item.titleText.text, item.primaryImage.url)

            if (isChecked) {
                Toast.makeText(context, "checked on ${item.titleText.text}", Toast.LENGTH_SHORT)
                    .show()
                myRef.child(item.id).setValue(id)

            } else {
                Toast.makeText(context, "unchecked on ${item.titleText.text}", Toast.LENGTH_SHORT)
                    .show()
                myRef.child(item.id).removeValue()
            }
        }


    }
}
