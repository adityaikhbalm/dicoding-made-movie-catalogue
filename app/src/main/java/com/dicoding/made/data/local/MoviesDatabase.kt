package com.dicoding.made.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dicoding.made.data.local.dao.CastsDAO
import com.dicoding.made.data.local.dao.MoviesDAO
import com.dicoding.made.data.local.dao.SimilarDAO
import com.dicoding.made.data.local.dao.TrailersDAO
import com.dicoding.made.data.local.model.*

@Database(
    entities = [Movie::class, Trailer::class, Cast::class, Similar::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun moviesDAO(): MoviesDAO
    abstract fun trailersDAO(): TrailersDAO
    abstract fun castsDAO(): CastsDAO
    abstract fun similarDAO(): SimilarDAO

    companion object {
        @Volatile
        private var instance: MoviesDatabase? = null

        operator fun invoke(context: Context) =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                MoviesDatabase::class.java, "movie")
                .build()
    }
}