package com.dicoding.made.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dicoding.made.R
import com.dicoding.made.ui.base.BaseActivity
import com.dicoding.made.ui.base.ViewPagerAdapter
import com.dicoding.made.ui.favorite.category.CategoryFavoriteFragment
import com.dicoding.made.ui.main.search.SearchActivity
import com.dicoding.made.ui.setting.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private var flag = 0

    override fun setView(): Int = R.layout.activity_main

    override fun activityCreated(savedInstance: Bundle?) {
        topMenu()
        bottomMenu()
        setupViewPager()
    }

    private fun topMenu() {
        setSupportActionBar(main_toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_settings)
    }

    private fun bottomMenu() {
        bottom_menu.setOnNavigationItemSelectedListener { item ->
            selectedMenu()
            when (item.itemId) {
                R.id.movieFragment -> {
                    fragment_layout.setCurrentItem(0,false)
                    flag = 0
                }
                R.id.favoriteFragment -> {
                    fragment_layout.setCurrentItem(1,false)
                    flag = 1
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun selectedMenu() {
        val drawable = intArrayOf(R.drawable.ic_anim_movie,R.drawable.ic_anim_favorite)
        val drawable2 = intArrayOf(R.drawable.ic_anim_movie2,R.drawable.ic_anim_favorite2)
        val item = intArrayOf(R.id.movieFragment,R.id.favoriteFragment)

        for (i in 0 until 2) {
            val drawableFinal = if (i != flag) drawable[i] else drawable2[i]
            bottom_menu.menu.findItem(item[i]).icon = ContextCompat.getDrawable(this,drawableFinal)
        }
    }

    private fun setupViewPager() {
        val fragments = listOf<Fragment>(
            CategoryFragment(),
            CategoryFavoriteFragment()
        )
        fragment_layout.adapter = ViewPagerAdapter(fragments,supportFragmentManager)
        fragment_layout.offscreenPageLimit = fragments.size
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.search_menu -> {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu_icon,menu)

        return true
    }
}
