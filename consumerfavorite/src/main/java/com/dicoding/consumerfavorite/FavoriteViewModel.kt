package com.dicoding.consumerfavorite

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.consumerfavorite.data.FavoritePagedFactory

class FavoriteViewModel(private val contentResolver: ContentResolver) : ViewModel() {

    private val config by lazy {
        PagedList.Config.Builder()
            .setInitialLoadSizeHint(10)
            .setPageSize(5)
            .setEnablePlaceholders(false)
            .build()
    }
    val fetchData by lazy {
        LivePagedListBuilder(FavoritePagedFactory(contentResolver), config).build()
    }
}