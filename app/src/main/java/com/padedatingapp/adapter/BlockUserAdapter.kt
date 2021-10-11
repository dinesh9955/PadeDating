package com.padedatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.padedatingapp.R
import com.padedatingapp.databinding.ItemBlockUserListBinding
import com.padedatingapp.model.blockUser.Data


class BlockUserAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Data, BlockUserAdapter.BlockUsersViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class BlockUsersViewHolder(private val binding: ItemBlockUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.tvBlockUsers.setOnClickListener {
                listener.onItemClick(getItem(adapterPosition))
            }
        }

        fun bind(model: Data) {
            binding.apply {
                val options = RequestOptions()
                options.centerCrop()
                options.placeholder(R.drawable.user_circle_1179465)
                Glide.with(binding.root).load(model.image)
                    .apply(options).into(ivUserPic)
                    tvName.text = model.firstName + " " + model.lastName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockUsersViewHolder {
        val binding =
            ItemBlockUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlockUsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlockUsersViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {
        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(
                oldItem: Data,
                newItem: Data
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(
                oldItem: Data,
                newItem: Data
            ): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: Data)
    }

}