package com.example.filmopedia.CallBack

import androidx.recyclerview.widget.DiffUtil
import com.example.filmopedia.data.WatchListData

class MyDiffUtil(
    private val oldList: List<WatchListData>,
    private val newList: List<WatchListData>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].imdbID == newList[newItemPosition].imdbID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
