package com.dicoding.made.ui.main.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.made.R
import com.dicoding.made.data.local.model.Search
import com.dicoding.made.data.remote.network.NetworkState
import com.dicoding.made.ui.base.BaseActivity
import com.dicoding.made.ui.favorite.FavoritePagedAdapter
import com.dicoding.made.utils.ConvertAny.convertColor
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity() {

    private var searchText: String? = ""
    private var searchType: String = "movie"
    private lateinit var searchView: SearchView
    private lateinit var search: EditText
    private val searchViewModel: SearchViewModel by viewModel()
    private var searchAdapter: FavoritePagedAdapter? = null
    private var focus: Boolean = true
    private var searchData: Search? = null
    private var resumeData: Boolean = false
    private var changeType: Boolean = false

    override fun setView(): Int = R.layout.activity_search

    override fun activityCreated(savedInstance: Bundle?) {
        setContent()
    }

    override fun onResume() {
        super.onResume()
        setRecycler()
        searchAdapter?.submitList(searchData?.data)
        resumeData = true
    }

    override fun onPause() {
        super.onPause()
        resumeData = false
    }

    private fun setRecycler() {
        val type = if (changeType) "movie" else "tv"
        searchAdapter = FavoritePagedAdapter(type)

        with(search_recycler) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }

    private fun setContent() {
        searchViewModel.initialNetworkState().observe(this, Observer {
            showLoading(it == NetworkState.LOADING)
            if (it.msg != null) {
                if (it.msg == R.string.not_found) {
                    message_not_found.isVisible = true
                    message_not_found.setText(it.msg)
                } else showSnackBar(it.msg)
            }
        })
        searchViewModel.searchDataList().observe(this, Observer { data ->
            searchAdapter?.submitList(data) {
                searchData = Search(data)
            }
        })
        searchViewModel.currentNetworkState().observe(this, Observer {
            searchAdapter?.setNetworkState(it)
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchText = searchView.query.toString()
        outState.putString("search",searchText)
        outState.putString("type",searchType)
        outState.putBoolean("focus",focus)
        outState.putBoolean("changeType",changeType)
        if (resumeData) outState.putParcelable("searchData",searchData)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString("search")
        savedInstanceState.getString("type")?.let {
            searchType = it
        }
        focus = savedInstanceState.getBoolean("focus")
        changeType = savedInstanceState.getBoolean("changeType")
        if (resumeData) searchData = savedInstanceState.getParcelable("searchData")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.popular_movies -> {
                item.isChecked = !item.isChecked
                search.setHint(R.string.search_movie)
                searchType = "movie"
                true
            }
            R.id.now_playing -> {
                item.isChecked = !item.isChecked
                search.setHint(R.string.search_tv)
                searchType = "tv"
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        menuInflater.inflate(R.menu.search_category_menu,menu)

        val searchMenuItem = menu?.findItem(R.id.search_menu)
        searchView = searchMenuItem?.actionView as SearchView
        val sortMenu = menu.findItem(R.id.sort_category)
        search = searchView.findViewById(androidx.appcompat.R.id.search_src_text)

        searchView.maxWidth = Integer.MAX_VALUE
        search.setTextColor(this.convertColor(R.color.medium_white))
        search.setHintTextColor(this.convertColor(R.color.low_white))
        search.hint = getString(R.string.search_movie)
        searchMenuItem.expandActionView()
        if (!focus) searchView.clearFocus()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchShowControl(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        search.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            focus = hasFocus
        }

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                onBackPressed()
                return true
            }
        })
        searchView.setQuery(searchText,false)
        if (searchType == "movie") {
            search.setHint(R.string.search_movie)
            sortMenu.subMenu.getItem(0).isChecked = true
        }
        else {
            search.setHint(R.string.search_tv)
            sortMenu.subMenu.getItem(1).isChecked = true
        }

        return true
    }

    private fun showLoading(loading: Boolean) {
        search_loading.isVisible = loading
        if (loading) message_not_found.isVisible = false
    }

    private fun searchShowControl(query: String) {
        changeType = searchType == "movie"
        setRecycler()
        searchView.clearFocus()
        searchViewModel.searchData(arrayOf(searchType,query))
    }
}
