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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.padedatingapp.R
import com.padedatingapp.databinding.*
import com.padedatingapp.model.DummyModel
import com.padedatingapp.model.notification.Doc
import com.padedatingapp.ui.main.fragments.ChatFragment
import com.padedatingapp.ui.main.fragments.MessagesFragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NotificationListAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Doc, RecyclerView.ViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class PackNotiViewHolder(private val binding: ItemNotificationPacksBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(model: Doc) {
            binding.apply {
            }
        }
    }

    inner class NormalNotiViewHolder(private val binding: ItemNotificationNormalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(model: Doc) {
            binding.apply {
                if(model.data.type.equals("TEXT_CHAT")){
                    tvName.text = model.data.sentBy.firstName +" " +model.data.sentBy.lastName
                    val options = RequestOptions()
                    options.centerCrop()
                    options.placeholder(R.drawable.user_circle_1179465)
                    Glide.with(binding.root).load(model.data.sentBy.image)
                            .apply(options).into(ivUserPic)
                    tvMentions.text = model.title
                    tvNotiDesc.text = model.message
                    tvNotiDesc.visibility = View.VISIBLE
                }else if(model.data.type.equals("AUDIO_CALL")){
                    tvName.text = model.data.user2.firstName +" " +model.data.user2.lastName
                    val options = RequestOptions()
                    options.centerCrop()
                    options.placeholder(R.drawable.user_circle_1179465)
                    Glide.with(binding.root).load(model.data.user2.image)
                            .apply(options).into(ivUserPic)
                    tvMentions.text = model.title
                    tvNotiDesc.visibility = View.GONE
                }else if(model.data.type.equals("VIDEO_CALL")){
                    tvName.text = model.data.user2.firstName +" " +model.data.user2.lastName
                    val options = RequestOptions()
                    options.centerCrop()
                    options.placeholder(R.drawable.user_circle_1179465)
                    Glide.with(binding.root).load(model.data.user2.image)
                            .apply(options).into(ivUserPic)
                    tvMentions.text = model.title
                    tvNotiDesc.visibility = View.GONE
                }

                val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                val date: Date = dateFormat.parse(model.sentAt)
                val formatter: DateFormat = SimpleDateFormat("HH:mm a")
                val dateStr: String = formatter.format(date)
                tvTime.text = dateStr.toUpperCase()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

//        return if(position< 3) 0
//        else
//            1
        return 1
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