package com.dicoding.made.ui.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.dicoding.made.BuildConfig
import com.dicoding.made.R
import com.dicoding.made.data.local.model.Movie
import com.dicoding.made.data.remote.repository.MovieRepository
import com.dicoding.made.utils.Constant
import org.koin.core.KoinComponent
import org.koin.core.inject

internal class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory, KoinComponent {

    private lateinit var movie: List<Movie>
    private val movieRepository: MovieRepository by inject()

    override fun onCreate() {
        movie = ArrayList()
    }

    override fun onDataSetChanged() {
        if (movie.isNotEmpty()) movie = ArrayList()

        movie = movieRepository.getAllFavoriteMovies()
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.favorite_widget_item)
        try {
            val bitmap = Glide.with(context.applicationContext)
                .asBitmap()
                .load(BuildConfig.IMAGE_URL + Constant.image_size_500 + movie[position].backdropPath)
                .submit(512,512)
                .get()

            rv.setImageViewBitmap(R.id.widget_banner,bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rv
    }

    override fun onDestroy() {}

    override fun getCount(): Int = movie.size

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}