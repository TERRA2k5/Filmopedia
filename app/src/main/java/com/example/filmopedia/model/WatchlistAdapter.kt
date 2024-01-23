package com.example.filmopedia.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmopedia.R
import com.example.filmopedia.WatchList_Fragment
import com.example.filmopedia.data.MoviesData
import com.example.filmopedia.data.WatchListData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WatchlistAdapter(var context: Context ) :
    RecyclerView.Adapter<WatchlistAdapter.MyViewHolder>() {

    private lateinit var auth: FirebaseAuth

    private lateinit var mListerner: onClickListener

    private var watchList: List<WatchListData> = emptyList()

    lateinit var dbRef : DatabaseReference



    /******************************/
    interface onClickListener{
        fun onClick(position: Int)
    }

    fun setOnClickListener(listener: onClickListener){

        mListerner = listener

    }
    /************************************/


    inner class MyViewHolder(itemView: View , listener: onClickListener) : RecyclerView.ViewHolder(itemView) {

        val imPoster: ImageView
        val tvTitle: TextView
        val bookMark: CheckBox
        val tvYear: TextView


        init {
            imPoster = itemView.findViewById(R.id.imPoster)
            bookMark = itemView.findViewById(R.id.Bookmark)
            tvYear = itemView.findViewById(R.id.tvYear)

            tvTitle = itemView.findViewById(R.id.tvTitle)

            itemView.setOnClickListener(){

                listener.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val v: View =
            LayoutInflater.from(context).inflate(R.layout.recycler_viewholder, parent, false)

        return MyViewHolder(v , mListerner )
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = differ.currentList[position]
        holder.tvTitle.setText(item.title.toString())

        if (item.year != null) {
            holder.tvYear.text = "Year: ${item.year}"
        }

        if (item.url != null) {
            Glide.with(context).load(item.url).into(holder.imPoster)
        }

        holder.bookMark.isChecked = true

        holder.bookMark.setOnCheckedChangeListener { checkBox, isChecked ->

            val data: WatchListData

            var image_url: String? = null
            var movie_title: String? = null
            var imdb: String? = null
            var release: Int?= null


            if (item.url != null){
                image_url = item.url.toString()
            }

            if (item.title != null){
                movie_title = item.title.toString()
            }

            if (item.imdbID != null){
                imdb = item.imdbID.toString()
            }

            if (item.year != null){
                release = item.year.hashCode()
            }


            data = WatchListData(
                imdb,
                movie_title,
                image_url,
                release
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
                myRef.child(item.imdbID.toString()).setValue(data)

            } else {
                Toast.makeText(context, "Removed from WatchList", Toast.LENGTH_SHORT)
                    .show()
                myRef.child(item.imdbID.toString()).removeValue()

                dbRef = FirebaseDatabase.getInstance().getReference(email)

                /***************MAKING NEW LIST OF DiffUtil **************/
                dbRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val newlist = mutableListOf<WatchListData>()


                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                val data = i.getValue(WatchListData::class.java)
                                newlist.add(data!!)
                            }
                        }

                        differ.submitList(newlist)

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
    }

    /**********************/
    private val differ_callBack = object: DiffUtil.ItemCallback<WatchListData>(){
        override fun areItemsTheSame(oldItem: WatchListData, newItem: WatchListData): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: WatchListData, newItem: WatchListData): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this , differ_callBack)

}