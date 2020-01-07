package com.dicoding.made.data.local.model

import com.google.gson.annotations.SerializedName

data class SimilarResponse (
    @SerializedName("results")
    var similar: List<Similar>?
)