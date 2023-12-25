package com.example.filmopedia.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmopedia.R
import com.example.filmopedia.data.MoviesData
import com.squareup.picasso.Picasso

class MyAdapter(var context: Context, var movieList: List<MoviesData>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    inner class MyViewHolder(item: View , context: Context): RecyclerView.ViewHolder(item){

        var movieName: TextView
        var movieImg: ImageView
        var watchlist: ImageView
        init {
            movieImg = item.findViewById(R.id.imPoster)
            movieName = item.findViewById(R.id.tvTitle)
            watchlist = item.findViewById(R.id.imBookmark)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.recycler_viewholder , parent,false)

        return MyViewHolder(v , context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val movieTitle: String = movieList.get(position).titleText.text.toString()


    }
    override fun getItemCount(): Int {
        return movieList.count()
    }

}