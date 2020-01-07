package com.dicoding.consumerfavorite.model

import android.database.Cursor
import com.dicoding.consumerfavorite.utils.Constant

class MovieHelper {

    companion object {
        fun cursorToList(cursor: Cursor?): MutableList<Movie> {
            val movies = mutableListOf<Movie>()

            if (cursor != null) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val movie = Movie(
                        cursor.getLong(cursor.getColumnIndexOrThrow(Constant._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constant.NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constant.TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constant.FIRST_AIR_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constant.OVERVIEW)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constant.ORIGINAL_LANGUAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constant.POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constant.RELEASE_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constant.TYPE))
                    )
                    movies.add(movie)
                    cursor.moveToNext()
                }
                cursor.close()
            }

            return movies
        }
    }
}