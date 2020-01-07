package com.dicoding.made.data.local.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "detail",
    foreignKeys = [ForeignKey(entity = Movie::class,
        parentColumns = ["id"],
        childColumns = ["movieId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["movieId"])])

data class Similar(
    @PrimaryKey
    var id: Long = 0,

    var movieId: Long = 0,

    var name: String?,

    var title: String?,

    @SerializedName("first_air_date")
    var firstAirDate: String?,

    @SerializedName("original_language")
    var originalLanguage: String?,

    @SerializedName("poster_path")
    var posterPath: String?,

    @SerializedName("release_date")
    var releaseDate: String?,

    @SerializedName("vote_average")
    var voteAverage: Double
) {
    constructor() : this(0,0,null,null,null,null,null,null,0.0)
}