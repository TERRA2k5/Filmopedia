package com.example.filmopedia.model

import android.content.Context
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


class SearchAdapter(var context: Context, var movieList: List<MoviesData>) :
    RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

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
    }

}