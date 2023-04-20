package com.squalgamesstudio.thoughtcatcher.presentation

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squalgamesstudio.thoughtcatcher.R
import com.squalgamesstudio.thoughtcatcher.ServiceLocator
import com.squalgamesstudio.thoughtcatcher.database.ThoughtDatabase
import com.squalgamesstudio.thoughtcatcher.entity.ThoughtEntity
import com.squalgamesstudio.thoughtcatcher.repository.ThoughtRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import kotlin.random.Random

class CatchThoughtsActivity : AppCompatActivity() {

    private val thoughtsViewModel : ThoughtsViewModel by viewModels {
        ThoughtsViewModel.ThoughtsViewModelFactory(this, ServiceLocator.provideToughtRepository(this)) }

    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catch_thoughts)

        textView = findViewById(R.id.textThought)

        thoughtsViewModel.getThoughts()

        val sharedPreferences = getSharedPreferences("APP_PREFERENCCES", MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)

        if(isFirstLaunch){
            sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
            Log.i("FirstLaunch", " true")

            var inputStream = resources.openRawResource(R.raw.thoughts)
            val reader = BufferedReader(inputStream.reader())
            try {
                var line = reader.readLine()
                while (line != null && line.isNotEmpty()) {
                    val entity = ThoughtEntity(line)
                    thoughtsViewModel.addThought( entity)
                    line = reader.readLine()
                }
            }
            catch(e: Exception) {
                reader.close()
            }
            finally {
                reader.close()
            }
        }
        thoughtsViewModel.randomThought.observe(this,{
            textView.text = thoughtsViewModel.randomThought.value?.thought ?: "Catching..."
        })

        thoughtsViewModel.getThoughts()
        thoughtsViewModel.leafThoughts()
    }
}