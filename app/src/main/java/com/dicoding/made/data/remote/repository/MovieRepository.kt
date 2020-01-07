package com.dicoding.made.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.made.data.local.model.MovieDetails
import com.dicoding.made.data.local.MoviesLocalDataSource
import com.dicoding.made.data.local.model.Movie
import com.dicoding.made.data.local.model.MovieResponse
import com.dicoding.made.data.remote.network.ResponseHandler
import com.dicoding.made.data.remote.network.*
import com.dicoding.made.data.remote.service.MovieApiService
import com.dicoding.made.utils.Constant
import kotlinx.coroutines.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MovieRepository(private val mApiService: MovieApiService,
                      private val responseHandler: ResponseHandler,
                      private val mLocalDataSource: MoviesLocalDataSource,
                      private val connect: Connectivity) {

    private val data = MediatorLiveData<Resource<MovieDetails>>()

    fun fetchDetailMovie(id: String,language: String,type: String): LiveData<Resource<MovieDetails>> {
        val result = loadFromDb(id)
        data.addSource(result) { db ->
            if (shouldFetch(db,language)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val fetch = fetchFromNetwork(id,language,type)
                    data.postValue(fetch)
                    if (db != null) saveMovie(fetch.data?.detail,type)
                }
            }
            else {
                if (db == null) data.value = responseHandler.handleException(null)
                else {
                    data.removeSource(result)
                    data.value = responseHandler.handleSuccess(db)
                }
            }
        }
        return data
    }

    private suspend fun fetchFromNetwork(id: String,language: String,type: String): Resource<MovieDetails> {
        return try {
            val data = mApiService.getDetailMovieAsync(type,id,language,Constant.detail_append_to_response)
            val movie = MovieDetails(data,data.creditsResponse?.cast,data.trailersResponse?.trailers,data.similarResponse?.similar)
            return responseHandler.handleSuccess(movie)
        } catch (e: IOException) {
            responseHandler.handleException(e)
        }
    }

    private fun loadFromDb(id: String): LiveData<MovieDetails> = mLocalDataSource.getMovie(id.toLong())

    private fun shouldFetch(data: MovieDetails?, language: String)=
        (data == null || data.detail?.language != language) && connect.isInternetAvailable()

    fun getAllFavoriteType(type: String)=
        mLocalDataSource.getAllFavoriteType(type)

    fun getAllFavoriteMovies()=
        mLocalDataSource.getAllFavoriteMovies()

    suspend fun saveMovie(movie: Movie?,type: String)=
        mLocalDataSource.saveMovie(movie,type)

    suspend fun deleteMovie(movie: Movie?)=
        mLocalDataSource.deleteMovie(movie)

    suspend fun getRelease(language: String): MovieResponse {
        val now = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return mApiService.getReleaseAsync(language,now,now)
    }
}