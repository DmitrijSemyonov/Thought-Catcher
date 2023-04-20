package com.squalgamesstudio.thoughtcatcher.database

import androidx.room.*
import com.squalgamesstudio.thoughtcatcher.entity.ThoughtEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThoughtDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveThought(thought: ThoughtEntity)

    @Query("SELECT * FROM thought")
    fun getSaveThoughts(): Flow<List<ThoughtEntity>>

    @Delete
    suspend fun deleteThought(thought: ThoughtEntity)
}