package com.padedatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.padedatingapp.databinding.ItemMessageListBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.ui.main.fragments.MessagesFragment

class MessagesListAdapter(private val listener: MessagesFragment) :
    ListAdapter<DummyModel, MessagesListAdapter.MessagesViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class MessagesViewHolder(private val binding: ItemMessageListBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val binding =
            ItemMessageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
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