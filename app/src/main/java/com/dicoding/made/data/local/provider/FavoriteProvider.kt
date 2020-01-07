package com.dicoding.made.data.local.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.sqlite.db.SimpleSQLiteQuery
import com.dicoding.made.BuildConfig
import com.dicoding.made.data.local.MoviesDatabase

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = BuildConfig.APPLICATION_ID
        private const val BASE_PATH = "favorite"
        private const val FAVORITE = 1
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY,BASE_PATH,FAVORITE)
        }
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        if (context == null) return null
        val cursor = when(uriMatcher.match(uri)) {
            FAVORITE -> {
                val query = SimpleSQLiteQuery("SELECT * FROM movie $sortOrder")
                val repository = MoviesDatabase.invoke(context!!)
                repository.moviesDAO().getAllFavoriteMovieCursor(query)
            }
            else -> throw IllegalArgumentException("Unknown Uri")
        }
        cursor.setNotificationUri(context?.contentResolver,uri)
        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return when(uriMatcher.match(uri)){
            FAVORITE -> "vnd.android.cursor.dir/$AUTHORITY.$BASE_PATH"
            else -> throw IllegalArgumentException("Unknown Uri $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}
