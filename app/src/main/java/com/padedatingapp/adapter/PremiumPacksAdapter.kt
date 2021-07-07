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
import com.padedatingapp.databinding.ItemPremiumPackstBinding
import com.padedatingapp.model.DummyModel

class PremiumPacksAdapter(private val listener: OnItemClickListener) :
    ListAdapter<DummyModel, PremiumPacksAdapter.PremiumPacksViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class PremiumPacksViewHolder(private val binding: ItemPremiumPackstBinding) :

        RecyclerView.ViewHolder(binding.root) {

        var clPacks = binding.clItemPacks

        init {
             binding.root.setOnClickListener {
                 listener.onItemClick(getItem(adapterPosition))
             }
        }

        fun bind(model: DummyModel) {
            binding.apply {
                this.clItemPacks.background = getDrawable(binding.root.context, adapterPosition)!!
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PremiumPacksViewHolder {
        val binding =
            ItemPremiumPackstBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun getDrawable(context: Context, pos: Int): Drawable? =
        when (pos % 5) {
            0 -> ContextCompat.getDrawable(context, R.drawable.pattern_bg_saphire)
            1 -> ContextCompat.getDrawable(context, R.drawable.pattern_bg_golden)
            2 -> ContextCompat.getDrawable(context, R.drawable.pattern_bg_purple)
            3 ->   ContextCompat.getDrawable(context, R.drawable.pattern_bg_blue)
            else ->  ContextCompat.getDrawable(context, R.drawable.pattern_bg_green)
        }
}