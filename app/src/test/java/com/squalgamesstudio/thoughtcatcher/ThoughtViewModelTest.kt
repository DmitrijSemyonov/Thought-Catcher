package com.squalgamesstudio.thoughtcatcher

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squalgamesstudio.thoughtcatcher.presentation.ThoughtsViewModel
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squalgamesstudio.thoughtcatcher.database.ThoughtDatabase
import com.squalgamesstudio.thoughtcatcher.entity.ThoughtEntity
import org.hamcrest.CoreMatchers
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ThoughtViewModelTest {

    private lateinit var thoughtsViewModel: ThoughtsViewModel

    @Before
    fun setupThoughtViewModel(){
        val db = ThoughtDatabase.getDatabase(ApplicationProvider.getApplicationContext())
        thoughtsViewModel = ThoughtsViewModel(ApplicationProvider.getApplicationContext(), FakeThoughtRepository())
    }
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testGetThoughts(){
        thoughtsViewModel.getThoughts()

    val thoughts = thoughtsViewModel.thoughts.value

    assertThat(thoughts, CoreMatchers.not(CoreMatchers.nullValue()))
    }

    @Test
    fun testAddThought(){
        val thought = ThoughtEntity("5")
        thoughtsViewModel.addThought(thought)
        thoughtsViewModel.getThoughts()

        var isContains = false
        thoughtsViewModel.thoughts.value?.let { isContains = it.contains(thought) }
        assert(isContains)
    }

    @Test
    fun testDeleteThought(){
        val thought = ThoughtEntity("5")
        thoughtsViewModel.addThought(thought)
        thoughtsViewModel.deleteThought(thought)
        thoughtsViewModel.getThoughts()

        var isContains = true
        thoughtsViewModel.thoughts.value?.let { isContains = it.contains(thought) }
        assert(!isContains)
    }
}