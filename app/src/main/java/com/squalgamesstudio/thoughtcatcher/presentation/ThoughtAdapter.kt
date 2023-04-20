package com.squalgamesstudio.thoughtcatcher.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squalgamesstudio.thoughtcatcher.R
import com.squalgamesstudio.thoughtcatcher.entity.ThoughtEntity

class ThoughtAdapter(private val context: Context,
                     private val listener : ActionClickListener) : RecyclerView.Adapter<ThoughtAdapter.ViewHolder>() {

    private val thoughts: ArrayList<ThoughtEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_thought, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = thoughts[position].thought

        holder.deleteButton.setOnClickListener{
            listener.delete(thoughts[position])
        }
    }

    override fun getItemCount(): Int {
        return thoughts.count()
    }

    fun submitApdate(update: List<ThoughtEntity>){
        val callback = BooksDiffCallback(thoughts, update)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)

        thoughts.clear()
        thoughts.addAll(update)

        diffResult.dispatchUpdatesTo(this)
    }
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById(R.id.textView)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
    }
    class BooksDiffCallback(
        private val oldThought: List<ThoughtEntity>,
        private val newThought: List<ThoughtEntity>
    ): DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldThought.size
        }
        override fun getNewListSize(): Int {
            return newThought.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldThought[oldItemPosition].thought.equals(newThought[newItemPosition].thought)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldThought[oldItemPosition].thought.equals(newThought[newItemPosition].thought)
        }
    }
    interface  ActionClickListener{
        fun delete(thought: ThoughtEntity)
    }
}