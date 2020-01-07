package com.dicoding.made.ui.main

import androidx.fragment.app.Fragment
import com.dicoding.made.R
import com.dicoding.made.ui.base.BaseFragment
import com.dicoding.made.ui.base.ViewPagerAdapter
import com.dicoding.made.ui.main.movie.MovieFragment
import kotlinx.android.synthetic.main.fragment_movie_category.*

class CategoryFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_movie_category

    override fun setupFragment() {
        setupViewPager()
        buttonCategory()
    }

    private fun setupViewPager() {
        val fragments = listOf<Fragment>(
            MovieFragment.newInstance("movie"),
            MovieFragment.newInstance("tv")
        )
        category_viewpager.adapter = ViewPagerAdapter(fragments,childFragmentManager)
    }

    private fun buttonCategory() {
        rg_category.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rb_movie -> category_viewpager.setCurrentItem(0,false)
                R.id.rb_tv -> category_viewpager.setCurrentItem(1,false)
            }
        }
    }
}