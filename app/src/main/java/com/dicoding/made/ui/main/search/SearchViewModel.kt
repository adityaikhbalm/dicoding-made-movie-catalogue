package com.dicoding.made.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.made.data.remote.repository.SearchPagedFactory

class SearchViewModel(private val searchPagedFactory: SearchPagedFactory) : ViewModel() {

    private var key = MutableLiveData<Array<String>>()

    fun searchData(value: Array<String>) {
        searchPagedFactory.searchData(value)
        key.value = value
    }

    fun searchDataList()= Transformations.switchMap(key) {
        val config =
            PagedList.Config.Builder()
                .setInitialLoadSizeHint(1)
                .setPageSize(1)
                .setEnablePlaceholders(false)
                .build()
        LivePagedListBuilder(searchPagedFactory, config).build()
    }

    fun currentNetworkState()= Transformations.switchMap(searchPagedFactory.mutableDataSource) {
        it.networkState
    }

    fun initialNetworkState()= Transformations.switchMap(searchPagedFactory.mutableDataSource) {
        it.initialLoad
    }

    override fun onCleared() {
        super.onCleared()
        searchPagedFactory.mutableDataSource.value?.clearCoroutineJobs()
    }
}