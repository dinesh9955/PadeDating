package com.padedatingapp.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.padedatingapp.R
import com.padedatingapp.databinding.ItemGiftCardsBinding
import com.padedatingapp.model.DummyModel

class GiftCardsAdapter(private val listener: OnItemClickListener) :
    ListAdapter<DummyModel, GiftCardsAdapter.PremiumPacksViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class PremiumPacksViewHolder(private val binding: ItemGiftCardsBinding) :

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PremiumPacksViewHolder {
        val binding =
            ItemGiftCardsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PremiumPacksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PremiumPacksViewHolder, position: Int) {
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