package com.dicoding.made.data.remote.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.dicoding.made.data.local.model.Movie

class SearchPagedFactory(private val searchPagedRepository: SearchPagedRepository) : DataSource.Factory<Int, Movie>() {

    val mutableDataSource: MutableLiveData<SearchPagedRepository> = MutableLiveData()
    private lateinit var searchDataSource: SearchPagedRepository

    fun searchData(value: Array<String>) {
        searchPagedRepository.type = value[0]
        searchPagedRepository.query = value[1]
    }

    override fun create(): DataSource<Int, Movie> {
        searchDataSource = searchPagedRepository
        mutableDataSource.postValue(searchDataSource)
        return searchPagedRepository
    }
}