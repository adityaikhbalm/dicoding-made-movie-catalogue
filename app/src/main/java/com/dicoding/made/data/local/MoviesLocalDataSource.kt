package com.dicoding.made.data.local

import android.content.Context
import com.dicoding.made.data.local.model.*
import com.dicoding.made.utils.AppPreferences

class MoviesLocalDataSource(private val context: Context,private val mDatabase: MoviesDatabase) {

    suspend fun saveMovie(movie: Movie?, type: String) {
        AppPreferences.init(context)
        movie?.let {
            it.type = type
            it.language = AppPreferences.language
            it.isFavorite = true
            mDatabase.moviesDAO().upsertMovie(it)
            insertCastList(it.creditsResponse?.cast, it.id)
            insertTrailers(it.trailersResponse?.trailers, it.id)
            insertSimilar(it.similarResponse?.similar, it.id)
        }
    }

    suspend fun deleteMovie(movie: Movie?) =
        mDatabase.moviesDAO().deleteMovie(movie)

    private suspend fun insertCastList(cast: List<Cast>?,movieId: Long) {
        cast?.let {
            it.forEach{ c ->
                c.movieId = movieId
            }
            mDatabase.castsDAO().upsertAllCasts(it)
        }

    }

    private suspend fun insertTrailers(trailers: List<Trailer>?,movieId: Long) {
        trailers?.let {
            it.forEach{ t ->
                t.movieId = movieId
            }
            mDatabase.trailersDAO().upsertAllTrailers(it)
        }
    }

    private suspend fun insertSimilar(similar: List<Similar>?,movieId: Long) {
        similar?.let {
            it.forEach{ s ->
                s.movieId = movieId
            }
            mDatabase.similarDAO().upsertAllSimilar(it)
        }
    }

    fun getMovie(movieId: Long)=
        mDatabase.moviesDAO().getMovie(movieId)

    fun getAllFavoriteType(type: String)=
        mDatabase.moviesDAO().getAllFavoriteType(type)

    fun getAllFavoriteMovies()=
        mDatabase.moviesDAO().getAllFavoriteMovies()
}