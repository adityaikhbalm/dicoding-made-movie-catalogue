package com.dicoding.made.data.local.model

data class MovieResponse(
    val page: Int?,
    val total_pages: Int?,
    val results: List<Movie>
)