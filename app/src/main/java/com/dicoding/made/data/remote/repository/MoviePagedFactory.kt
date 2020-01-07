package com.dicoding.made.data.remote.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.dicoding.made.data.local.model.Movie

class MoviePagedFactory(private val moviePagedRepository: MoviePagedRepository) : DataSource.Factory<Int, Movie>() {

    val mutableDataSource: MutableLiveData<MoviePagedRepository> = MutableLiveData()
    private lateinit var movieDataSource: MoviePagedRepository

    fun searchData(value: String) {
        moviePagedRepository.type = value
    }

    override fun create(): DataSource<Int, Movie> {
        movieDataSource = moviePagedRepository
        mutableDataSource.postValue(movieDataSource)
        return moviePagedRepository
    }
}