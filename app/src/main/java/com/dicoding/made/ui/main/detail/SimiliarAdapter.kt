package com.dicoding.made.ui.main.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dicoding.made.BuildConfig
import com.dicoding.made.R
import com.dicoding.made.data.local.model.Similar
import com.dicoding.made.ui.base.BaseRVAdapter
import com.dicoding.made.utils.Constant
import kotlinx.android.synthetic.main.item_movie.view.*

class SimiliarAdapter(
    private val type: String,
    private val context: Context,
    private val data: List<Similar>) : BaseRVAdapter() {

    override fun getItemViewType(position: Int) = position

    override fun itemCount(): Int = data.size

    override fun cvHolder(parent: ViewGroup, viewType: Int): View {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_similiar,parent,false)
        v.detail_movies.setOnClickListener { (context as DetailMovieActivity).getMovie(data[viewType].id) }
        return v
    }

    @SuppressLint("DefaultLocale")
    override fun bvHolder(holder: Holder, position: Int) {
        with(holder.view) {
            Glide.with(context)
                .load(BuildConfig.IMAGE_URL + Constant.image_size_185 + data[position].posterPath)
                .centerCrop()
                .into(poster_movies)
            rating_movies.text = data[position].voteAverage.toString()
            language_movies.text = data[position].originalLanguage?.capitalize()
            val title =
                if (type == "movie") data[position].title + " (" + (data[position].releaseDate?.substring(0,4)) + ")"
                else data[position].name + " (" + (data[position].firstAirDate?.substring(0,4)) + ")"
            title_movies.text = title
        }
    }
}

interface DetailMovieCallback {
    fun getMovie(id: Long)
}