package com.squalgamesstudio.thoughtcatcher

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squalgamesstudio.thoughtcatcher.entity.ThoughtEntity
import com.squalgamesstudio.thoughtcatcher.presentation.ThoughtAdapter
import com.squalgamesstudio.thoughtcatcher.presentation.ThoughtsViewModel
import java.io.BufferedReader

class ResourcesFragment : Fragment() {

    private val thoughtsViewModel by lazy { ViewModelProvider(this)[ThoughtsViewModel::class.java] }

    private lateinit var thoughtAdapter: ThoughtAdapter

    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thoughtAdapter = ThoughtAdapter(requireContext(), object:ThoughtAdapter.ActionClickListener{
            override fun delete(thought: ThoughtEntity) {
                thoughtsViewModel.deleteThought(thought)
            }
        })

        thoughtsViewModel.getThoughts()

        if(thoughtsViewModel.thoughts.value == null || thoughtsViewModel.thoughts.value?.count() == 0){

            var inputStream = resources.openRawResource(R.raw.thoughts)
            val reader = BufferedReader(inputStream.reader())
            try {
                var line = reader.readLine()
                while (line != null) {
                    thoughtsViewModel.addThought(ThoughtEntity(line))
                    line = reader.readLine()
                }
            }
            catch(e: Exception) {
                reader.close()
            }
            finally {
                reader.close()
            }
            thoughtsViewModel.getThoughts()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = requireActivity().findViewById(R.id.progressBar)
        recyclerView = requireActivity().findViewById(R.id.recyclerView)

        thoughtsViewModel.thoughts.observe(viewLifecycleOwner,{
            thoughtAdapter.submitApdate(it)
        })
        thoughtsViewModel.dataLoading.observe(viewLifecycleOwner,{ loading ->
            when(loading){
                true -> progressBar.visibility = View.VISIBLE
                false -> progressBar.visibility = View.GONE
            }
        })
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = thoughtAdapter
        }
    }
}