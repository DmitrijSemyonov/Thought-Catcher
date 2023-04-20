package com.squalgamesstudio.thoughtcatcher

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.squalgamesstudio.thoughtcatcher.database.ThoughtDao
import com.squalgamesstudio.thoughtcatcher.database.ThoughtDatabase
import com.squalgamesstudio.thoughtcatcher.repository.ThoughtRepository
import com.squalgamesstudio.thoughtcatcher.repository.ThoughtRepositoryImpl

object ServiceLocator {
    @Volatile
    var thoughtRepository: ThoughtRepository? = null
        @VisibleForTesting set

    fun provideToughtRepository(context: Context): ThoughtRepository {
        synchronized(this) {
            val db = ThoughtDatabase.getDatabase(context)
            return thoughtRepository ?: createThoughtRepository(db.thoughtDao())
        }
    }

    fun createThoughtRepository(thoughtDao: ThoughtDao) : ThoughtRepository{
        thoughtRepository = ThoughtRepositoryImpl(thoughtDao)
        return thoughtRepository as ThoughtRepositoryImpl
    }
}