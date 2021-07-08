package com.padedatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.padedatingapp.databinding.ItemMeetMeBinding

import com.padedatingapp.model.MeetMeData

class SwipeableCardAdapter(internal var list: ArrayList<MeetMeData>, private val listener: OnItemClickListener) :
    ListAdapter<MeetMeData, SwipeableCardAdapter.MatchesViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class MatchesViewHolder(private val binding: ItemMeetMeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
             binding.root.setOnClickListener {
                 listener.onItemClick(getItem(adapterPosition))
             }
        }

        fun bind(model: MeetMeData) {
            binding.apply {
            }
        }
    }


    override fun getItemCount(): Int {
        return list?.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val binding =
            ItemMeetMeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    fun updateData(arrayList2: ArrayList<MeetMeData>) {
        list = arrayList2
        notifyDataSetChanged()
    }

    companion object {
        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<MeetMeData>() {
            override fun areItemsTheSame(
                oldItem: MeetMeData,
                newItem: MeetMeData
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(
                oldItem: MeetMeData,
                newItem: MeetMeData
            ): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: MeetMeData)
    }

}