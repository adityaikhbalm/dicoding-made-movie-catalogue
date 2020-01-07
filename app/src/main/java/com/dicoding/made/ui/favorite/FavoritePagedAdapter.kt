package com.dicoding.made.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.made.BuildConfig
import com.dicoding.made.R
import com.dicoding.made.data.local.model.Movie
import com.dicoding.made.data.remote.network.NetworkState
import com.dicoding.made.ui.main.detail.DetailMovieActivity
import com.dicoding.made.utils.Constant
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoritePagedAdapter(private val type: String?) :
    PagedListAdapter<Movie, FavoritePagedAdapter.Holder>(ItemCallback) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite,parent,false)
        val position = Holder(v)
        v.detail_favorite.setOnClickListener {
            val intent = Intent(v.context, DetailMovieActivity::class.java)
            intent.putExtra("detail",getItem(position.adapterPosition)?.id)
            intent.putExtra("type",type)
            v.context.startActivity(intent)
        }
        return position
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return
        holder.bindTo(item)
    }

    inner class Holder(internal val view: View) : RecyclerView.ViewHolder(view) {
        var favorite: Movie? = null

        fun bindTo(movie: Movie) {
            favorite = movie
            with(view) {
                Glide.with(context)
                    .load(BuildConfig.IMAGE_URL + Constant.image_size_185 + movie.posterPath)
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
                    if (type == "movie") movie.title + dateMovie
                    else movie.name + dateTv
                favorite_title.text = title
            }
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) notifyItemRemoved(super.getItemCount())
            else notifyItemInserted(super.getItemCount())
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        val ItemCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
        }
    }
}