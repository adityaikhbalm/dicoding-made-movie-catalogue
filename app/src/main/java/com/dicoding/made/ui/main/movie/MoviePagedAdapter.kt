package com.dicoding.made.ui.main.movie

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviePagedAdapter(private val type: String?) :
    PagedListAdapter<Movie, MoviePagedAdapter.Holder>(ItemCallback) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_movie,parent,false)
        val position = Holder(v)
        v.detail_movies.setOnClickListener {
            val intent = Intent(v.context, DetailMovieActivity::class.java)
            intent.putExtra("detail",getItem(position.adapterPosition)?.id)
            intent.putExtra("type",type)
            v.context.startActivity(intent)
        }
        return position
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        item?.let {
            with(holder.view) {
                Glide.with(context)
                    .load(BuildConfig.IMAGE_URL + Constant.image_size_185 + it.posterPath)
                    .centerCrop()
                    .into(poster_movies)
                rating_movies.text = it.voteAverage.toString()
                language_movies.text = it.originalLanguage?.capitalize()
                val dateMovie =
                    if (it.releaseDate?.isNotEmpty() == true)
                        " (" + (it.releaseDate?.substring(0,4)) + ")"
                    else ""
                val dateTv =
                    if (it.firstAirDate?.isNotEmpty() == true)
                        " (" + (it.firstAirDate?.substring(0,4)) + ")"
                    else ""
                val title =
                    if (type == "movie") it.title + dateMovie
                    else it.name + dateTv
                title_movies.text = title
            }
        }
    }

    inner class Holder(internal val view: View) : RecyclerView.ViewHolder(view)

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