package com.dicoding.consumerfavorite.data

import android.content.ContentResolver
import android.net.Uri
import androidx.paging.PositionalDataSource
import com.dicoding.consumerfavorite.model.Movie
import com.dicoding.consumerfavorite.model.MovieHelper
import com.dicoding.consumerfavorite.utils.Constant

class FavoritePagedRepository(private val contentResolver: ContentResolver) :
    PositionalDataSource<Movie>() {

    companion object {
        private val CONTENT_URI = Uri.Builder()
            .scheme("content")
            .authority(Constant.AUTHORITY)
            .appendPath(Constant.BASE_PATH)
            .build()
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Movie>) {
        callback.onResult(getFavorite(params.requestedLoadSize, params.requestedStartPosition),0)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Movie>) {
        callback.onResult(getFavorite(params.loadSize, params.startPosition))
    }

    private fun getFavorite(limit: Int, offset: Int): MutableList<Movie> {
        val cursor = contentResolver.query(
            CONTENT_URI,
            null,
            null,
            null,
            "LIMIT $limit OFFSET $offset"
        )
        return MovieHelper.cursorToList(cursor)
    }
}