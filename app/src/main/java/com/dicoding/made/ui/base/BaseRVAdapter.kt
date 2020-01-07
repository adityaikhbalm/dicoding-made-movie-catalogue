package com.dicoding.made.ui.base

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRVAdapter : RecyclerView.Adapter<BaseRVAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(cvHolder(parent,viewType))

    override fun getItemCount(): Int = itemCount()

    override fun onBindViewHolder(holder: Holder, position: Int) = bvHolder(holder,position)

    abstract fun itemCount(): Int
    abstract fun cvHolder(parent: ViewGroup, viewType: Int): View
    abstract fun bvHolder(holder: Holder, position: Int)

    inner class Holder(internal val view: View) : RecyclerView.ViewHolder(view)
}