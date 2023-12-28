package com.example.filmopedia.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmopedia.R
import com.example.filmopedia.data.MoviesData


class MyAdapter(var context: Context, var movieList: List<MoviesData>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

//    lateinit var binding: RecyclerViewholderBinding


    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val imPoster: ImageView
        val tvTitle: TextView
        val imBookMark: ImageView

        init{
            imPoster = itemView.findViewById(R.id.imPoster)
            imBookMark = itemView.findViewById(R.id.imBookmark)
            tvTitle = itemView.findViewById(R.id.tvTitle)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val v: View = LayoutInflater.from(context).inflate(R.layout.recycler_viewholder , parent,false)

        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return movieList.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = movieList.get(position)
        holder.tvTitle.text = item.titleText.text.toString()
//        Glide.with(context).load(item.primaryImage.url).into(holder.imPoster)

    }

//    inner class MyViewHolder( val binding: RecyclerViewholderBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//
////        val movieData: MoviesData = movieList[position]
////        holder.bind(movieData)
//
//        holder.binding.setVariable(BR.moviedata , getItemId(position))
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//
//        binding = RecyclerViewholderBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
////        val v: View = LayoutInflater.from(context).inflate(R.layout.recycler_viewholder , parent,false)
//
//        return MyViewHolder(binding)
//    }
//
//
//
//    override fun getItemCount(): Int {
//        return movieList.count()
//    }

}