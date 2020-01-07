package com.dicoding.consumerfavorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.consumerfavorite.model.Movie
import com.dicoding.consumerfavorite.utils.Constant
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoritePagedAdapter :
    PagedListAdapter<Movie, FavoritePagedAdapter.Holder>(ItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite,parent,false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val movie = getItem(position) ?: return
        with(holder.view) {
            Glide.with(context)
                .load(Constant.IMAGE_URL + Constant.image_size_185 + movie.posterPath)
                .centerCrop()
                .into(favorite_poster)
            var overview = movie.overview.toString()
            if (overview.isEmpty()) overview = context.getString(R.string.no_translations)
            favorite_overview.text = overview
            val dateMovie =
                if (movie.releaseDate?.isNotEmpty() == true)
                    " (" + (movie.releaseDate?.substring(0,4)) + ")"
                else ""
            val dateTv =
                if (movie.firstAirDate?.isNotEmpty() == true)
                    " (" + (movie.firstAirDate?.substring(0,4)) + ")"
                else ""
            val title =
                if (movie.type == "movie") movie.title + dateMovie
                else movie.name + dateTv
            favorite_title.text = title
        }
    }

    inner class Holder(internal val view: View) : RecyclerView.ViewHolder(view)

    companion object {
        val ItemCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
        }
    }
}