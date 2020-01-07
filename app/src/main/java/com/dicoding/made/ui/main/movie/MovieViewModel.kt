package com.dicoding.made.ui.main.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.made.data.local.model.Movie
import com.dicoding.made.data.remote.repository.MoviePagedFactory

class MovieViewModel(private val moviePagedFactory: MoviePagedFactory) : ViewModel() {

    lateinit var fetchData: LiveData<PagedList<Movie>>

    private val config by lazy {
        PagedList.Config.Builder()
            .setInitialLoadSizeHint(1)
            .setPageSize(1)
            .setEnablePlaceholders(false)
            .build()
    }

    fun retry() = moviePagedFactory.mutableDataSource.value?.retryAllFailed()

    fun searchData(value: String) {
        moviePagedFactory.searchData(value)
        fetchData = LivePagedListBuilder(moviePagedFactory, config).build()
    }

    fun currentNetworkState()=
        Transformations.switchMap(moviePagedFactory.mutableDataSource) {
            it.networkState
        }

    fun initialNetworkState()=
        Transformations.switchMap(moviePagedFactory.mutableDataSource) {
            it.initialLoad
        }

    override fun onCleared() {
        super.onCleared()
        moviePagedFactory.mutableDataSource.value?.clearCoroutineJobs()
    }
}