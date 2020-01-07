package com.dicoding.made.ui.favorite.category

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class CategoryPagerAdapter(
    private val title: List<String>,
    private val fragment: List<Fragment>,
    fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getCount(): Int {
        return fragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}