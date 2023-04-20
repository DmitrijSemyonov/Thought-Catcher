package com.squalgamesstudio.thoughtcatcher.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.squalgamesstudio.thoughtcatcher.R

class MainActivity : AppCompatActivity() {

    private lateinit var buttonResources: Button
    private lateinit var buttonCatchThoughts: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeValues()
    }

    fun initializeValues(){
        buttonResources = findViewById(R.id.buttonResources)
        buttonCatchThoughts = findViewById(R.id.buttonCatchThoughts)
        buttonResources.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, ResourcesActivity::class.java)
            startActivity(intent)

            /*supportFragmentManager.commit {
                val resFragment = ResourcesFragment()
                replace(R.id.container, resFragment)
            }*/
        })

        buttonCatchThoughts.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CatchThoughtsActivity::class.java)
            startActivity(intent)
        })
    }
}