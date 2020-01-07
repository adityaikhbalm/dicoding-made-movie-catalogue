package com.dicoding.made.ui.main.movie

import android.os.Bundle
import android.os.Parcelable
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.made.MovieApplication
import com.dicoding.made.R
import com.dicoding.made.data.remote.network.NetworkState
import com.dicoding.made.data.remote.network.Status
import com.dicoding.made.ui.base.BaseFragment
import com.dicoding.made.ui.base.ItemOffsetDecoration
import com.dicoding.made.utils.ConvertAny.calculateNoOfColumns
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : BaseFragment() {

    companion object {
        fun newInstance(type: String) = MovieFragment().apply{
            arguments = Bundle().apply{
                putString("type", type)
            }
        }
    }

    private val movieViewModel: MovieViewModel by viewModel()
    private lateinit var moviesAdapter: MoviePagedAdapter
    private val type by lazy { arguments?.getString("type") ?: "movie" }
    private var refresh = true
    private var position: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = savedInstanceState?.getParcelable("position")
        if (savedInstanceState == null) movieViewModel.searchData(type)
        moviesAdapter = MoviePagedAdapter(type)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("position", (movie_recycler.layoutManager as LinearLayoutManager).onSaveInstanceState())
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    override fun getLayoutId(): Int = R.layout.fragment_movie

    override fun setupFragment() {
        setupRecyclerView()
        initSwipeToRefresh()
    }

    private fun fetchData()= this.launch {
        movieViewModel.initialNetworkState().observe(this@MovieFragment,
            Observer {
                showLoading(it == NetworkState.LOADING)
                if (it.msg != null)
                    if (it.msg == R.string.not_found) {
                        message_not_found.isVisible = true
                        message_not_found.setText(it.msg)
                    }
                    else showSnackBarFragment(it.msg)
            }
        )
        movieViewModel.fetchData.observe(this@MovieFragment,
            Observer {
                moviesAdapter.submitList(it) {
                    (movie_recycler.layoutManager as LinearLayoutManager).onRestoreInstanceState(position)
                }
            }
        )
        movieViewModel.currentNetworkState().observe(this@MovieFragment,
            Observer {
                moviesAdapter.setNetworkState(it)
                refresh = it.status != Status.LOADING
            }
        )
    }

    private fun setupRecyclerView() {
        with(movie_recycler) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context,calculateNoOfColumns(130f))
            addItemDecoration(ItemOffsetDecoration())
            adapter = moviesAdapter
        }
    }

    private fun showLoading(loading: Boolean) {
        movie_swipe_refresh.isRefreshing = loading
        if (loading) message_not_found.isVisible = false
    }

    private fun initSwipeToRefresh() {
        movie_swipe_refresh.setOnRefreshListener {
            movieViewModel.retry()
            if (refresh || MovieApplication.refreshMovie) showLoading(false)
        }
    }
}