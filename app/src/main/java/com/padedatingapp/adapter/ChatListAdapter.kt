package com.padedatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flexhelp.model.chat_history.DataItem
import com.padedatingapp.databinding.ItemChatBinding
import com.padedatingapp.databinding.ItemChatOtherUserBinding
import com.padedatingapp.databinding.ItemMessageListBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.ui.main.fragments.ChatFragment
import com.padedatingapp.ui.main.fragments.MessagesFragment

class ChatListAdapter(private val listener: OnItemClickListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class MyMessagesViewHolder(private val binding: ItemChatOtherUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(model: DataItem) {
            binding.apply {
            }
        }
    }

    inner class OtherUserMessagesViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(model: DataItem) {
            binding.apply {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val binding =
                    ItemChatOtherUserBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return MyMessagesViewHolder(binding)
            }
            else -> {
                val binding =
                    ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return OtherUserMessagesViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            if (holder is MyMessagesViewHolder)
                holder.bind(currentItem)
            else if (holder is OtherUserMessagesViewHolder) holder.bind(currentItem)
        }
    }

    companion object {
        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: DataItem)
    }

}