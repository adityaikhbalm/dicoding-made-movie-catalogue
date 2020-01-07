package com.dicoding.made.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class MovieDetails (
    @Embedded
    var detail: Movie?,

    @Relation(parentColumn = "id", entityColumn = "movieId")
    var castList: List<Cast>?,

    @Relation(parentColumn = "id", entityColumn = "movieId")
    var trailerList: List<Trailer>?,

    @Relation(parentColumn = "id", entityColumn = "movieId")
    var similarList: List<Similar>?
)