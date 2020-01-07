package com.dicoding.made.ui.favorite

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.made.R
import com.dicoding.made.ui.base.BaseFragment
import com.dicoding.made.ui.widget.FavoriteWidget
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : BaseFragment() {

    companion object {
        fun newInstance(type: String) = FavoriteFragment().apply{
            arguments = Bundle().apply{
                putString("type", type)
            }
        }
    }

    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private lateinit var favoriteAdapter: FavoritePagedAdapter
    private val type by lazy { arguments?.getString("type") ?: "movie" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) favoriteViewModel.favoriteMovies(type)
        favoriteAdapter = FavoritePagedAdapter(type)
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData()= this.launch {
        favoriteViewModel.fetchFavorite.observe(this@FavoriteFragment, Observer { favorite ->
            if (favorite.isEmpty()) {
                no_favorite.isVisible = true
                favorite_recycler.isVisible = false
            }
            else {
                no_favorite.isVisible = false
                favorite_recycler.isVisible = true
            }
            favoriteAdapter.submitList(favorite)
        })
    }

    override fun getLayoutId(): Int  = R.layout.fragment_favorite

    override fun setupFragment() {
        config()
        initSwipeToDelete()
    }

    private fun config() {
        with(favorite_recycler) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }
    }

    private fun initSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView,
                                          viewHolder: RecyclerView.ViewHolder): Int =
                makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder as FavoritePagedAdapter.Holder).favorite?.let {
                    favoriteViewModel.deleteMovie(it)
                }
                refreshWidget()
            }
        }).attachToRecyclerView(favorite_recycler)
    }

    private fun refreshWidget() {
        val intent = Intent(context, FavoriteWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(
            context?.let {
                ComponentName(
                    it,
                    FavoriteWidget::class.java
                )
            }
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context?.sendBroadcast(intent)
    }
}