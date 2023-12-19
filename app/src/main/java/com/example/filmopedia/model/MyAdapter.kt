package com.example.filmopedia.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmopedia.R
import com.example.filmopedia.data.MoviesData
import com.squareup.picasso.Picasso

class MyAdapter(val movieList: List<MoviesData>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    inner class MyViewHolder(item: View): RecyclerView.ViewHolder(item){

        var movieName: TextView
        var movieImg: ImageView
        var watchlist: ImageView
        init {
            movieImg = item.findViewById(R.id.imPoster)
            movieName = item.findViewById(R.id.tvTitle)
            watchlist = item.findViewById(R.id.imWatchlist)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context ).inflate(R.layout.recycler_layout , parent,false)

        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }
    override fun getItemCount(): Int {
        return movieList.count()
    }

}

