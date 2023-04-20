package com.squalgamesstudio.thoughtcatcher

import com.squalgamesstudio.thoughtcatcher.entity.ThoughtEntity
import com.squalgamesstudio.thoughtcatcher.repository.ThoughtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class FakeThoughtRepository(): ThoughtRepository {
    val arrayList = arrayListOf<ThoughtEntity>(ThoughtEntity("1"),ThoughtEntity("2"),ThoughtEntity("3"))

    override suspend fun getThoughts(): Flow<List<ThoughtEntity>> {

        return (
                listOf(arrayList.toList(), arrayList.toList())
                ).asFlow()
    }

    override suspend fun insert(thoughtEntity: ThoughtEntity) {
        arrayList.add(thoughtEntity)
    }

    override suspend fun deleteThought(thoughtEntity: ThoughtEntity) {
        arrayList.remove(thoughtEntity)
    }

}