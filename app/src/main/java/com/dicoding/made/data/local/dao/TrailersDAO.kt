package com.dicoding.made.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.dicoding.made.data.local.model.Trailer

@Dao
interface TrailersDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllTrailers(trailerList: List<Trailer>)
}