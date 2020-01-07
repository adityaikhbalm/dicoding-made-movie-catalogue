package com.dicoding.made.ui.favorite.category

import androidx.fragment.app.Fragment
import com.dicoding.made.R
import com.dicoding.made.ui.base.BaseFragment
import com.dicoding.made.ui.favorite.FavoriteFragment
import kotlinx.android.synthetic.main.fragment_category_favorite.*

class CategoryFavoriteFragment : BaseFragment() {

    override fun getLayoutId(): Int  = R.layout.fragment_category_favorite

    override fun setupFragment() {
        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        val fragments = listOf<Fragment>(
            FavoriteFragment.newInstance("movie"),
            FavoriteFragment.newInstance("tv")
        )
        val title = listOf(
            getString(R.string.category_movie),
            getString(R.string.category_tv_show)
        )
        category_favorite_viewpager.adapter = CategoryPagerAdapter(title,fragments,childFragmentManager)
    }

    private fun setupTabLayout() = tab_favorites.setupWithViewPager(category_favorite_viewpager)
}