package com.squalgamesstudio.thoughtcatcher.repository

import androidx.lifecycle.MutableLiveData
import com.squalgamesstudio.thoughtcatcher.entity.ThoughtEntity
import kotlinx.coroutines.flow.Flow

interface ThoughtRepository {

    suspend fun getThoughts(): Flow<List<ThoughtEntity>>

    suspend fun insert(thoughtEntity: ThoughtEntity)

    suspend fun deleteThought(thoughtEntity: ThoughtEntity)
}