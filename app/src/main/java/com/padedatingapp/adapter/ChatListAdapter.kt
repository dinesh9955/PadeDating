package com.padedatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.padedatingapp.databinding.ItemChatBinding
import com.padedatingapp.databinding.ItemChatOtherUserBinding
import com.padedatingapp.model.chat.ChatUsersData

class ChatListAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ChatUsersData, RecyclerView.ViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {

    private val LAYOUT_ONE = 0
    private val LAYOUT_TWO = 1

    inner class MyMessagesViewHolder(private val binding: ItemChatOtherUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

        }

        fun bind(model: ChatUsersData) {
            binding.apply {
                tvMessage.text = model.modMsg
            }
        }
    }

    inner class OtherUserMessagesViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(model: ChatUsersData) {
            binding.apply {
                tvMessage.text = model.modMsg
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        //return position % 2
        if(position==0)
            return LAYOUT_ONE;
        else
            return LAYOUT_TWO;
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



        if(holder.getItemViewType() == LAYOUT_ONE)
        {
            val currentItem = getItem(position)
            (holder as MyMessagesViewHolder).bind(currentItem)
        }

        if(holder.getItemViewType() == LAYOUT_TWO)
        {
            val currentItem = getItem(position)
            (holder as OtherUserMessagesViewHolder).bind(currentItem)
        }

//        val currentItem = getItem(position)
//        if (currentItem != null) {
//            if (holder is MyMessagesViewHolder)  holder.bind(currentItem)
//            else if (holder is OtherUserMessagesViewHolder) holder.bind(currentItem)
//        }
    }

    companion object {
        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ChatUsersData>() {
            override fun areItemsTheSame(
                oldItem: ChatUsersData,
                newItem: ChatUsersData
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(
                oldItem: ChatUsersData,
                newItem: ChatUsersData
            ): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: ChatUsersData)
    }

}