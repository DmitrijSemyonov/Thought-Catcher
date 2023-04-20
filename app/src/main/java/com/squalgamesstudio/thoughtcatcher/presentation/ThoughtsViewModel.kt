package com.squalgamesstudio.thoughtcatcher.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.squalgamesstudio.thoughtcatcher.database.ThoughtDatabase
import com.squalgamesstudio.thoughtcatcher.entity.ThoughtEntity
import com.squalgamesstudio.thoughtcatcher.repository.ThoughtRepository
import com.squalgamesstudio.thoughtcatcher.repository.ThoughtRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.random.Random

class ThoughtsViewModel(context: Context, val thoughtRepository: ThoughtRepository): ViewModel(){

    private val _dataLoading = MutableLiveData(true)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _thoughts = MutableLiveData<List<ThoughtEntity>>()
    val thoughts = _thoughts

    private val _randomThought = MutableLiveData<ThoughtEntity>()
    val randomThought = _randomThought

    fun getThoughts(){
        viewModelScope.launch{
            _dataLoading.postValue(true)
            val thoughtsFlow = thoughtRepository.getThoughts()
            thoughtsFlow.collect {
                thoughts.value = it
                _dataLoading.postValue(false)
            }
        }
    }

    fun deleteThought(thoughtEntity: ThoughtEntity){
        viewModelScope.launch{
            _dataLoading.postValue(true)
            thoughtRepository.deleteThought(thoughtEntity)
            _dataLoading.postValue(false)
        }
    }
    fun addThought(thoughtEntity: ThoughtEntity){
        viewModelScope.launch{
            _dataLoading.postValue(true)
            thoughtRepository.insert(thoughtEntity)
            _dataLoading.postValue(false)
        }
    }
    fun leafThoughts(){
        viewModelScope.launch {
            while (true){
                delay(4250)
                val length = thoughts.value?.count()
                val randI = length?.let { Random.nextInt(0, it) }
                randomThought.value = randI?.let { thoughts.value?.get(it) }
            }
        }
    }

    class ThoughtsViewModelFactory(val context1: Context,
                                   val thoughtRepository: ThoughtRepository): ViewModelProvider.NewInstanceFactory(){

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ThoughtsViewModel(context1, thoughtRepository) as T
        }
    }
}