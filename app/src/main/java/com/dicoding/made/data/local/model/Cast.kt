package com.dicoding.made.data.local.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cast",
    foreignKeys = [ForeignKey(entity = Movie::class,
        parentColumns = ["id"],
        childColumns = ["movieId"],
        onDelete = CASCADE,
        onUpdate = CASCADE)],
    indices = [Index(value = ["movieId"])])

data class Cast(
    @PrimaryKey
    @SerializedName("credit_id")
    var id: String,

    var movieId: Long = 0,

    var name: String?,

    @SerializedName("profile_path")
    var profilePath: String?
) {
    constructor() : this("0",0,null,null)
}