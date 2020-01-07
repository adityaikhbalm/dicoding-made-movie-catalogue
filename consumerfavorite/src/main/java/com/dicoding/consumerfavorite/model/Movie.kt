package com.dicoding.consumerfavorite.model

data class Movie(
    var id: Long = 0,
    var name: String?,
    var title: String?,
    var firstAirDate: String?,
    var overview: String?,
    var originalLanguage: String?,
    var posterPath: String?,
    var releaseDate: String?,
    var type: String
)