package com.padedatingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.padedatingapp.R
import com.padedatingapp.databinding.ItemWhoLikedBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.model.MeetMeData
import com.padedatingapp.ui.main.fragments.MeetMeFragmentDirections
import kotlinx.android.synthetic.main.fragment_profile_other_user.*

class PeopleWhoLikedAdapter(private val listener: OnItemClickListener, private val listener2: OnItemClickListenerData) :
    ListAdapter<MeetMeData, PeopleWhoLikedAdapter.UploadItemViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class UploadItemViewHolder(private val binding: ItemWhoLikedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onItemClick(getItem(adapterPosition))
            }
        }

        fun bind(model: MeetMeData) {
            binding.apply {
                Glide.with(binding.root).load(model.image)
                        .apply(RequestOptions().placeholder(R.drawable.user_place_holder)).into(ivUserPic)

                tvName.text = model.firstName+" "+model.lastName+", "+model.age
                tvProfileType.text = model.work
            }


            binding.ivSeeMore.setOnClickListener(View.OnClickListener {
               // findNavController().navigate(MeetMeFragmentDirections.actionToOtherProfile(model))
                listener2.onItemClickData(getItem(adapterPosition))
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadItemViewHolder {
        val binding =
            ItemWhoLikedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UploadItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UploadItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
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

    interface OnItemClickListenerData {
        fun onItemClickData(model: MeetMeData)
    }





}