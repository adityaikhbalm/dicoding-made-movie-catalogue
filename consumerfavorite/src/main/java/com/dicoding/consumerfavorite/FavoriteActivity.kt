package com.dicoding.consumerfavorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_favorite.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : AppCompatActivity() {

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val adapterFavorite = FavoritePagedAdapter()

        with(favorite_recycler) {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = adapterFavorite
        }

        favoriteViewModel.fetchData.observe(this, Observer { favorite ->
            if (favorite.isEmpty()) {
                no_favorite.isVisible = true
                favorite_recycler.isVisible = false
            }
            else {
                no_favorite.isVisible = false
                favorite_recycler.isVisible = true
            }
            adapterFavorite.submitList(favorite)
        })
    }
}
