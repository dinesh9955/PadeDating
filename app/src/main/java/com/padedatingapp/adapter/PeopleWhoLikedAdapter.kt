package com.padedatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.padedatingapp.databinding.ItemWhoLikedBinding
import com.padedatingapp.model.DummyModel

class PeopleWhoLikedAdapter(private val listener: OnItemClickListener) :
    ListAdapter<DummyModel, PeopleWhoLikedAdapter.UploadItemViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class UploadItemViewHolder(private val binding: ItemWhoLikedBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadItemViewHolder {
        val binding =
            ItemWhoLikedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UploadItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UploadItemViewHolder, position: Int) {
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