package com.dicoding.made.data.local.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.dicoding.made.data.local.model.Movie
import com.dicoding.made.data.local.model.MovieDetails

@Dao
interface MoviesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovie(movie: Movie)

    @Transaction
    @Query("SELECT * FROM movie WHERE id = :movieId")
    fun getMovie(movieId: Long): LiveData<MovieDetails>

    @Delete
    suspend fun deleteMovie(movie: Movie?)

    @Query("SELECT * FROM movie WHERE type = :type")
    fun getAllFavoriteType(type: String): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie")
    fun getAllFavoriteMovies(): List<Movie>

    @RawQuery
    fun getAllFavoriteMovieCursor(sort: SupportSQLiteQuery): Cursor
}
