package com.dicoding.made.data.local.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.gson.annotations.SerializedName

@Entity(tableName = "trailer",
    foreignKeys = [ForeignKey(entity = Movie::class,
        parentColumns = ["id"],
        childColumns = ["movieId"],
        onDelete = CASCADE,
        onUpdate = CASCADE)],
    indices = [Index(value = ["movieId"])])

data class Trailer(
    @PrimaryKey
    var id: String,

    var movieId: Long = 0,

    var key: String?,

    var name: String?,

    var site: String?
) {
    constructor() : this("0",0,null,null,null)
}