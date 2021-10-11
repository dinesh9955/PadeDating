package com.padedatingapp.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.padedatingapp.R
import com.padedatingapp.databinding.ItemPremiumPackstBinding
//import com.padedatingapp.model.DummyModel
import com.padedatingapp.model.plans.Doc

class PremiumPacksAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Doc, PremiumPacksAdapter.PremiumPacksViewHolder>(
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

        fun bind(model: Doc) {
            binding.apply {
                this.clItemPacks.background = getDrawable(binding.root.context, adapterPosition)!!
                tvPackName.text = model.name
                tvPackValidity.text = ""+model.type + " bundle"
                tvPackValidity.visibility = View.VISIBLE

                tvPackPrice.text = model.price.units +" "+ model.price.amount
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
        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Doc>() {
            override fun areItemsTheSame(
                oldItem: Doc,
                newItem: Doc
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(
                oldItem: Doc,
                newItem: Doc
            ): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: Doc)
    }

    fun getDrawable(context: Context, pos: Int): Drawable? =
        when (pos % 5) {
            0 -> ContextCompat.getDrawable(context, R.drawable.pattern_bg_saphire)
            1 -> ContextCompat.getDrawable(context, R.drawable.pattern_bg_golden)
            2 -> ContextCompat.getDrawable(context, R.drawable.pattern_bg_purple)
            3 ->   ContextCompat.getDrawable(context, R.drawable.pattern_bg_blue)
            else ->  ContextCompat.getDrawable(context, R.drawable.pattern_bg_green)
        }


    fun updateData(arrayList2: ArrayList<Doc>) {
       // alName = arrayList2
        notifyDataSetChanged()
    }




}