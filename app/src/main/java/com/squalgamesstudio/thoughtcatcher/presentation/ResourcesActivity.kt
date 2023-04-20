package com.squalgamesstudio.thoughtcatcher.presentation

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squalgamesstudio.thoughtcatcher.R
import com.squalgamesstudio.thoughtcatcher.ServiceLocator
import com.squalgamesstudio.thoughtcatcher.database.ThoughtDatabase
import com.squalgamesstudio.thoughtcatcher.entity.ThoughtEntity
import com.squalgamesstudio.thoughtcatcher.repository.FakeThoughtRepository
import com.squalgamesstudio.thoughtcatcher.repository.ThoughtRepository
import com.squalgamesstudio.thoughtcatcher.repository.ThoughtRepositoryImpl
import java.io.BufferedReader

class ResourcesActivity : AppCompatActivity() {

    private val thoughtsViewModel : ThoughtsViewModel by viewModels {
        ThoughtsViewModel.ThoughtsViewModelFactory(this, ServiceLocator.provideToughtRepository(this)) }

    private lateinit var thoughtAdapter: ThoughtAdapter

    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resources)

        thoughtAdapter = ThoughtAdapter(this, object:ThoughtAdapter.ActionClickListener{
            override fun delete(thought: ThoughtEntity) {
                thoughtsViewModel.deleteThought(thought)
            }
        })

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

        progressBar = findViewById(R.id.progressBar1)
        recyclerView = findViewById(R.id.recyclerView1)
        floatingActionButton = findViewById(R.id.floatingActionButton1)

        floatingActionButton.setOnClickListener(View.OnClickListener {
            val dialog: Dialog = Dialog(this)
            dialog.setContentView(R.layout.add_thought)

            val buttonEnter = dialog.findViewById<Button>(R.id.buttonEnter)
            val editText = dialog.findViewById<EditText>(R.id.editText)

            buttonEnter.setOnClickListener(View.OnClickListener {
                if(editText.text.toString().isNotEmpty()){
                    thoughtsViewModel.addThought(ThoughtEntity(editText.text.toString()))
                }
                dialog.dismiss()
            })

            dialog.show()
        })

        thoughtsViewModel.thoughts.observe(this,{
            thoughtAdapter.submitApdate(it)
        })
        thoughtsViewModel.dataLoading.observe(this,{ loading ->
            when(loading){
                true -> progressBar.visibility = View.VISIBLE
                false -> progressBar.visibility = View.GONE
            }
        })
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = thoughtAdapter
        }
    }
}