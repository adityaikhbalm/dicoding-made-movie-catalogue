package com.dicoding.made.ui.main.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.made.BuildConfig
import com.dicoding.made.R
import com.dicoding.made.data.local.model.Cast
import com.dicoding.made.ui.base.BaseRVAdapter
import com.dicoding.made.utils.Constant
import kotlinx.android.synthetic.main.item_cast.view.*

class CastAdapter(private val data: List<Cast>) : BaseRVAdapter() {

    override fun itemCount(): Int = data.size

    override fun cvHolder(parent: ViewGroup, viewType: Int): View =
        LayoutInflater.from(parent.context).inflate(R.layout.item_cast,parent,false)

    override fun bvHolder(holder: Holder, position: Int) {
        with(holder.view) {
            Glide.with(context)
                .load(BuildConfig.IMAGE_URL + Constant.image_size_185 + data[position].profilePath)
                .centerCrop()
                .placeholder(R.drawable.logo)
                .apply(RequestOptions.circleCropTransform())
                .into(detail_image_cast)
            detail_cast_name.text = data[position].name
        }
    }
}