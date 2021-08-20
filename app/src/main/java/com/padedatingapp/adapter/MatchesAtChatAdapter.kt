package com.padedatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.padedatingapp.R
import com.padedatingapp.databinding.ItemMatchesAtChatBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.model.chat.ChatUsersData

class MatchesAtChatAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ChatUsersData, MatchesAtChatAdapter.MatchesViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {

    lateinit var myId: String;

    inner class MatchesViewHolder(private val binding: ItemMatchesAtChatBinding) :
        RecyclerView.ViewHolder(binding.root) {



        init {
             binding.root.setOnClickListener {
                 listener.onItemClick(getItem(adapterPosition))
             }
        }

        fun bind(model: ChatUsersData) {
            binding.apply {
                if(!model.sentBy._id.equals(myId)){
                    Glide.with(binding.root).load(model.sentBy.image)
                            .into(ivPhoto)
                }
                if(!model.sentTo._id.equals(myId)){
                    Glide.with(binding.root).load(model.sentTo.image)
                            .into(ivPhoto)
                }


//                Glide.with(binding.root).load(model.sentTo.image)
//                        .apply(RequestOptions().placeholder(R.drawable.user_place_holder)).into(ivPhoto)
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



    fun updateList(myId2: String) {
        myId = myId2
        notifyDataSetChanged()
    }



}