package com.squalgamesstudio.thoughtcatcher.repository

import androidx.lifecycle.MutableLiveData
import com.squalgamesstudio.thoughtcatcher.database.ThoughtDao
import com.squalgamesstudio.thoughtcatcher.database.ThoughtDatabase
import com.squalgamesstudio.thoughtcatcher.entity.ThoughtEntity
import kotlinx.coroutines.flow.Flow

class ThoughtRepositoryImpl(private val thoughtDao: ThoughtDao) : ThoughtRepository {


    override suspend fun getThoughts() : Flow<List<ThoughtEntity>> {
        return thoughtDao.getSaveThoughts()
    }

    override suspend fun insert(thoughtEntity: ThoughtEntity) {
        thoughtDao.saveThought(thoughtEntity)
    }

    override suspend fun deleteThought(thoughtEntity: ThoughtEntity) {
        thoughtDao.deleteThought(thoughtEntity)
    }
}