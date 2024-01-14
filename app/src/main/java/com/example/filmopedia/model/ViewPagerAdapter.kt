package com.example.filmopedia.model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.filmopedia.Home_Fragment
import com.example.filmopedia.Search_Fragment
import com.example.filmopedia.WatchList_Fragment

class ViewPagerAdapter(fragmentActivity: FragmentManager) :
    FragmentPagerAdapter(fragmentActivity) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return Home_Fragment()
            }
            1 -> {
                return Search_Fragment()
            }
            2 -> {
                return WatchList_Fragment()
            }
            else -> {
                return Home_Fragment()
            }
        }
    }
}