package com.squalgamesstudio.thoughtcatcher.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.squalgamesstudio.thoughtcatcher.entity.ThoughtEntity
import com.squalgamesstudio.thoughtcatcher.repository.ThoughtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect

class FakeThoughtRepository(): ThoughtRepository {
    val arrayList = arrayListOf<ThoughtEntity>(ThoughtEntity("1"),ThoughtEntity("2"),ThoughtEntity("3"))
    private val mLD : MutableLiveData<List<ThoughtEntity>> = MutableLiveData<List<ThoughtEntity>>()

    var flow: Flow<List<ThoughtEntity>> = (
            listOf(arrayList.toList(), arrayList.toList())
            ).asFlow()

    override suspend fun getThoughts(): Flow<List<ThoughtEntity>> {
        flow.collect{
            mLD.value = it
        }
        return mLD.asFlow()
    }

    override suspend fun insert(thoughtEntity: ThoughtEntity) {
        arrayList.add(thoughtEntity)

        flow = (
                listOf(arrayList.toList(), arrayList.toList())
                ).asFlow()
        flow.collect{
            mLD.value = it
        }
    }

    override suspend fun deleteThought(thoughtEntity: ThoughtEntity) {
        arrayList.remove(thoughtEntity)
        flow = (
                listOf(arrayList.toList(), arrayList.toList())
                ).asFlow()
        flow.collect{
            mLD.value = it
        }
    }

}