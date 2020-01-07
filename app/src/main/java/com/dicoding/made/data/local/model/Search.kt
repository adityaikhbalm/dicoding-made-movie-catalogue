package com.dicoding.made.data.local.model

import android.os.Parcelable
import androidx.paging.PagedList
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Search(val data: @RawValue PagedList<Movie>?) : Parcelable