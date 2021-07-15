package com.padedatingapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.padedatingapp.R
import com.padedatingapp.databinding.ItemMessageListBinding
import com.padedatingapp.model.chat.ChatUsersData
import com.padedatingapp.ui.main.fragments.MessagesFragment
import java.text.SimpleDateFormat
import java.util.*

class MessagesListAdapter(private val listener: MessagesFragment) :
    ListAdapter<ChatUsersData, MessagesListAdapter.MessagesViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {


    inner class MessagesViewHolder(private val binding: ItemMessageListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        init {
            binding.root.setOnClickListener {
                listener.onItemClick(getItem(adapterPosition))
            }
        }

        fun bind(model: ChatUsersData) {

            Log.e(TAG, "modelA "+model.sentTo)

            binding.apply {
                Glide.with(binding.root).load(model.sentTo.image)
                        .apply(RequestOptions().placeholder(R.drawable.user_place_holder)).into(ivUserPic)
                tvName.text = model.sentTo.firstName + " " + model.sentTo.lastName
                tvLastMessage.text = model.modMsg

                val date = model.sentAt
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val outputFormat = SimpleDateFormat("hh:mm:ss")
                val parsedDate: Date = inputFormat.parse(date)
                val formattedDate: String = outputFormat.format(parsedDate)
                tvTime.text = formattedDate

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


            var TAG = "MessagesListAdapter"

    }

    interface OnItemClickListener {
        fun onItemClick(model: ChatUsersData)
    }

}