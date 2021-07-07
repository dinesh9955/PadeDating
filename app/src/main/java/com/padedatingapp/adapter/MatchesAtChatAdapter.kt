package com.padedatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.padedatingapp.databinding.ItemMatchesAtChatBinding
import com.padedatingapp.model.DummyModel

class MatchesAtChatAdapter(private val listener: OnItemClickListener) :
    ListAdapter<DummyModel, MatchesAtChatAdapter.MatchesViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class MatchesViewHolder(private val binding: ItemMatchesAtChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
             binding.root.setOnClickListener {
                 listener.onItemClick(getItem(adapterPosition))
             }
        }

        fun bind(model: DummyModel) {
            binding.apply {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val binding =
            ItemMatchesAtChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {
        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<DummyModel>() {
            override fun areItemsTheSame(
                oldItem: DummyModel,
                newItem: DummyModel
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(
                oldItem: DummyModel,
                newItem: DummyModel
            ): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: DummyModel)
    }

}