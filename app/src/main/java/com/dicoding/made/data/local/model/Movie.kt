package com.dicoding.made.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    var id: Long = 0,

    @SerializedName("backdrop_path")
    var backdropPath: String?,

    @SerializedName("episode_run_time")
    var episodeRunTime: List<String>?,

    @SerializedName("first_air_date")
    var firstAirDate: String?,

    var genres: List<Genre>?,

    var name: String?,

    @SerializedName("original_language")
    var originalLanguage: String?,

    var overview: String?,

    @SerializedName("poster_path")
    var posterPath: String?,

    @SerializedName("release_date")
    var releaseDate: String?,

    var runtime: String?,

    var title: String?,

    @SerializedName("vote_average")
    var voteAverage: Double,

    @SerializedName("vote_count")
    var voteCount: Int,

    @Ignore
    @SerializedName("credits")
    var creditsResponse: CreditsResponse?,

    @Ignore
    @SerializedName("videos")
    var trailersResponse: TrailersResponse?,

    @Ignore
    @SerializedName("similar")
    var similarResponse: SimilarResponse?,

    var isFavorite: Boolean,

    var type: String,

    var language: String
) {
    constructor() : this(0,null,null,null,null,null,null,null,null,null,null,null,0.0,0,null,null,null,false,"movie","en")
}