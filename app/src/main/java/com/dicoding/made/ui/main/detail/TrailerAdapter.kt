package com.dicoding.made.ui.main.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dicoding.made.R
import com.dicoding.made.data.local.model.Trailer
import com.dicoding.made.ui.base.BaseRVAdapter
import com.dicoding.made.utils.Constant
import kotlinx.android.synthetic.main.item_trailer.view.*

class TrailerAdapter(private val context: Context, private val data: List<Trailer>) : BaseRVAdapter() {

    override fun getItemViewType(position: Int) = position

    override fun itemCount(): Int = data.size

    override fun cvHolder(parent: ViewGroup, viewType: Int): View {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_trailer,parent,false)
        v.detail_youtube_trailer.setOnClickListener {
            val appIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("vnd.youtube:" + data[viewType].key)
            )
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(Constant.url_youtube + data[viewType].key)
            )
            if (appIntent.resolveActivity(context.packageManager) != null) context.startActivity(appIntent)
            else context.startActivity(webIntent)
        }

        return v
    }

    override fun bvHolder(holder: Holder, position: Int) {
        with(holder.view) {
            Glide.with(context)
                .load(Constant.image_url_youtube + data[position].key + Constant.image_url_youtube_end)
                .placeholder(R.drawable.logo)
                .into(detail_image_trailer)
            detail_trailer_name.text = data[position].name
        }
    }
}