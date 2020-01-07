package com.dicoding.consumerfavorite.data

import android.content.ContentResolver
import androidx.paging.DataSource
import com.dicoding.consumerfavorite.model.Movie

class FavoritePagedFactory(private val contentResolver: ContentResolver) :
    DataSource.Factory<Int, Movie>() {

    override fun create(): DataSource<Int, Movie> {
        return FavoritePagedRepository(contentResolver)
    }
}