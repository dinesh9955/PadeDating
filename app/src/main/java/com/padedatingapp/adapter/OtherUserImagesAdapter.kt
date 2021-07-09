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
import com.padedatingapp.databinding.ItemOtherUserImagesBinding
import com.padedatingapp.model.DocImage
import com.padedatingapp.model.DummyModel

class OtherUserImagesAdapter(private val listener: OnItemClickListener) :
    ListAdapter<DocImage, OtherUserImagesAdapter.MatchesViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class MatchesViewHolder(private val binding: ItemOtherUserImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
             binding.root.setOnClickListener {
                 listener.onItemClick(getItem(adapterPosition))
             }
        }

        fun bind(model: DocImage) {
            binding.apply {
                Glide.with(binding.root).load(model.source)
                        .apply(RequestOptions().placeholder(R.drawable.user_place_holder)).into(ivPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val binding =
            ItemOtherUserImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {
        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<DocImage>() {
            override fun areItemsTheSame(
                oldItem: DocImage,
                newItem: DocImage
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(
                oldItem: DocImage,
                newItem: DocImage
            ): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: DocImage)
    }

}