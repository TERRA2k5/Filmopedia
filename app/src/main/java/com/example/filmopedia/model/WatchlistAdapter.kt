package com.example.filmopedia.model

import android.content.Context
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
import com.google.firebase.database.FirebaseDatabase

class WatchlistAdapter(var context: Context, var watchlist: ArrayList<WatchListData>) :
    RecyclerView.Adapter<WatchlistAdapter.MyViewHolder>() {

    val myRef = FirebaseDatabase.getInstance().getReference("WatchList")


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imPoster: ImageView
        val tvTitle: TextView
        val bookMark: CheckBox
        val tvYear: TextView


        init {
            imPoster = itemView.findViewById(R.id.imPoster)
            bookMark = itemView.findViewById(R.id.Bookmark)
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
        return watchlist.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = watchlist[position]
        holder.tvTitle.setText(item.title.toString())

        if (item.year != null) {
            holder.tvYear.text = "Year: ${item.year}"
        }

        if (item.url != null) {
            Glide.with(context).load(item.url).into(holder.imPoster)
        }

        holder.bookMark.isChecked = true

        holder.bookMark.setOnCheckedChangeListener { checkBox, isChecked ->
            val id = WatchListData(item.imdbID, item.title, item.url)

            if (isChecked) {
                Toast.makeText(context, "checked on ${item.title}", Toast.LENGTH_SHORT)
                    .show()
                myRef.child(item.imdbID.toString()).setValue(id)

            } else {
                Toast.makeText(context, "unchecked on ${item.title}", Toast.LENGTH_SHORT)
                    .show()
                myRef.child(item.imdbID.toString()).removeValue()
            }
        }
    }

}