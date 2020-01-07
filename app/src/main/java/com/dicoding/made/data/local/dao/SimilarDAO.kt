package com.dicoding.made.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.dicoding.made.data.local.model.Similar

@Dao
interface SimilarDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllSimilar(similarList: List<Similar>)
}