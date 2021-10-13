package com.padedatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.padedatingapp.R
import com.padedatingapp.databinding.ItemMatchesAtChatBinding
import com.padedatingapp.model.MeetMeData


class MatchesAtChatAdapter(private val listener: OnItemClickListener) :
    ListAdapter<MeetMeData, MatchesAtChatAdapter.MatchesViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {

    lateinit var myId: String;

    inner class MatchesViewHolder(private val binding: ItemMatchesAtChatBinding) :
        RecyclerView.ViewHolder(binding.root) {



        init {
             binding.root.setOnClickListener {
                 listener.onItemClickMatch(getItem(adapterPosition))
             }
        }

        fun bind(model: MeetMeData) {
            binding.apply {
//                val options = RequestOptions()
//                options.centerCrop()
//                    .transforms(RoundedCorners(20))
//                  //  .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
//                options.placeholder(R.drawable.user_circle_1179465)
//                Glide.with(binding.root).load(model.image)
//                    .apply(options)
//                    .into(ivPhoto)

                Glide.with(binding.root)
                    .load(model.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.user_circle_1179465)
                    .transform(CenterCrop(), RoundedCorners(20))
                    .into(ivPhoto)

//                GlideApp.with(context)
//                    .load(glideUrl)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.drawable.ic_placeholder)
//                    .transforms(CenterCrop(), RoundedCorners(1))
//                    .into(holder.imageView)
//                if(!model.sentBy._id.equals(myId)){
//                    Glide.with(binding.root).load(model.sentBy.image)
//                        .apply(options)
//                            .into(ivPhoto)
//                }
//                if(!model.sentTo._id.equals(myId)){
//                    Glide.with(binding.root).load(model.sentTo.image)
//                        .apply(options)
//                            .into(ivPhoto)
//                }


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
        fun onItemClickMatch(model: MeetMeData)
    }



    fun updateList(myId2: String) {
        myId = myId2
        notifyDataSetChanged()
    }



}