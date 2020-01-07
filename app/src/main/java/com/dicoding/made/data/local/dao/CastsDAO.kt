package com.dicoding.made.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.dicoding.made.data.local.model.Cast

@Dao
interface CastsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllCasts(castList: List<Cast>)
}