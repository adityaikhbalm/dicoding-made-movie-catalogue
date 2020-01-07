package com.dicoding.made.data.remote.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.dicoding.made.MovieApplication
import com.dicoding.made.R
import com.dicoding.made.data.local.model.Movie
import com.dicoding.made.data.remote.network.NetworkState
import com.dicoding.made.data.remote.service.MovieApiService
import com.dicoding.made.utils.AppPreferences
import com.dicoding.made.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class MoviePagedRepository(private val context: Context,
                           private val movieApiService: MovieApiService) : PageKeyedDataSource<Int, Movie>() {

    private val completableJob by lazy { Job() }
    private val coroutineScope  by lazy { CoroutineScope(Dispatchers.IO + completableJob) }
    private var retry: (() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()
    lateinit var type: String

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        coroutineScope.launch(Dispatchers.IO) {
            AppPreferences.init(context)
            networkState.postValue(NetworkState.LOADING)
            initialLoad.postValue(NetworkState.LOADING)
            try {
                val data = movieApiService.getMovieAsync(type,AppPreferences.language,Constant.page)
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                if (data.results.isNotEmpty())
                    callback.onResult(data.results,null,Constant.page+1)
                else {
                    retry = {
                        loadInitial(params, callback)
                    }
                    if (data.page == null || data.total_pages == null) NetworkState.error(R.string.timeout)
                    else if (data.total_pages == 0) NetworkState.error(R.string.not_found)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error(R.string.no_internet)
                initialLoad.postValue(error)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        coroutineScope.launch(Dispatchers.IO) {
            networkState.postValue(NetworkState.LOADING)
            initialLoad.postValue(NetworkState.LOADING)
            try {
                val data = movieApiService.getMovieAsync(type,AppPreferences.language,params.key)
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                if (data.results.isNotEmpty()) {
                    retry = null
                    callback.onResult(data.results,params.key.inc())
                    MovieApplication.refreshMovie = false
                }
                else {
                    retry = {
                        loadAfter(params, callback)
                    }
                    if (data.page == null || data.total_pages == null) NetworkState.error(R.string.timeout)
                    else if (data.total_pages == 0) NetworkState.error(R.string.not_found)
                    MovieApplication.refreshMovie = true
                }
            } catch (e: IOException) {
                e.printStackTrace()
                retry = {
                    loadAfter(params, callback)
                }
                val error = NetworkState.error(R.string.no_internet)
                initialLoad.postValue(error)
                MovieApplication.refreshMovie = true
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {}

    fun clearCoroutineJobs() {
        completableJob.cancel()
    }
}