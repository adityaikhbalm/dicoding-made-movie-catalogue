package com.dicoding.made.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.made.data.local.model.Movie
import com.dicoding.made.data.remote.repository.MovieRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    lateinit var fetchFavorite: LiveData<PagedList<Movie>>

    private val config by lazy {
        PagedList.Config.Builder()
            .setInitialLoadSizeHint(10)
            .setPageSize(5)
            .setEnablePlaceholders(false)
            .build()
    }

    fun favoriteMovies(type: String) {
        val data = LivePagedListBuilder(movieRepository.getAllFavoriteType(type), config).build()
        fetchFavorite = data
    }

    fun deleteMovie(movie: Movie?) {
        viewModelScope.launch { movieRepository.deleteMovie(movie) }
    }
}