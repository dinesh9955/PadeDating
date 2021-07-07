package com.padedatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.padedatingapp.R
import com.padedatingapp.databinding.ItemBuyCreditsBinding
import com.padedatingapp.model.DummyModel

class RechargeCoinAdapter(private val listener: OnItemClickListener) :
    ListAdapter<DummyModel, RechargeCoinAdapter.UploadItemViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class UploadItemViewHolder(private val binding: ItemBuyCreditsBinding) :

        RecyclerView.ViewHolder(binding.root) {
        val cvBuyCredit = binding.cvBuyCredit
        val tvCreditAmount = binding.tvCreditAmount
        val tvCredits = binding.tvCredits

        init {

        }

        fun bind(model: DummyModel) {
            if (adapterPosition == 4) {
                cvBuyCredit.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.recharge_coin_gradient
                )

                tvCreditAmount.setTextColor( ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorWhite
                ))

                tvCredits.setTextColor( ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorWhite
                ))

            } else {
                cvBuyCredit.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorWhite
                    )
                )


                tvCreditAmount.setTextColor( ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorAccent
                ))

                tvCredits.setTextColor( ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorBlack
                ))


            }
            binding.apply {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadItemViewHolder {
        val binding =
            ItemBuyCreditsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UploadItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UploadItemViewHolder, position: Int) {
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