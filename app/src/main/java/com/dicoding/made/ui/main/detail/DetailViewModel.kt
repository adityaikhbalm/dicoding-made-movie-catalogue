package com.dicoding.made.ui.main.detail

import androidx.lifecycle.*
import com.dicoding.made.data.local.model.Movie
import com.dicoding.made.data.local.model.MovieDetails
import com.dicoding.made.data.remote.network.Resource
import com.dicoding.made.data.remote.repository.MovieRepository
import kotlinx.coroutines.*

class DetailViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    lateinit var fetchDetailMovie: LiveData<Resource<MovieDetails>>

    fun detailMovie(value: Array<String>) {
        Resource.loading(null)
        fetchDetailMovie = movieRepository.fetchDetailMovie(value[0],value[1],value[2])
    }

    fun saveMovie(movie: Movie?,type: String) {
        viewModelScope.launch {
            movieRepository.saveMovie(movie,type)
        }
    }

    fun deleteMovie(movie: Movie?) {
        viewModelScope.launch {
            movieRepository.deleteMovie(movie)
        }
    }
}