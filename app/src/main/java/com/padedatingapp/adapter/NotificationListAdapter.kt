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
import com.padedatingapp.databinding.*
import com.padedatingapp.model.DummyModel
import com.padedatingapp.ui.main.fragments.ChatFragment
import com.padedatingapp.ui.main.fragments.MessagesFragment

class NotificationListAdapter(private val listener: OnItemClickListener) :
    ListAdapter<DummyModel, RecyclerView.ViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class PackNotiViewHolder(private val binding: ItemNotificationPacksBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(model: DummyModel) {
            binding.apply {
            }
        }
    }

    inner class NormalNotiViewHolder(private val binding: ItemNotificationNormalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(model: DummyModel) {
            binding.apply {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if(position< 3) 0
        else
            1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val binding =
                    ItemNotificationPacksBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return PackNotiViewHolder(binding)
            }
            else -> {
                val binding =
                    ItemNotificationNormalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return NormalNotiViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            if (holder is PackNotiViewHolder)
                holder.bind(currentItem)
            else if (holder is NormalNotiViewHolder) holder.bind(currentItem)
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