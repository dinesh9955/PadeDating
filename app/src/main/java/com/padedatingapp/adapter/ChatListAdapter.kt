package com.padedatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.padedatingapp.R
import com.padedatingapp.databinding.ItemChatBinding
import com.padedatingapp.databinding.ItemChatOtherUserBinding
import com.padedatingapp.model.chat.ChatUsersData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ChatListAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ChatUsersData, RecyclerView.ViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {

    private val LAYOUT_ONE = 0
    private val LAYOUT_TWO = 1

    lateinit var myId: String;


    inner class MyMessagesViewHolder(private val binding: ItemChatOtherUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

        }

        fun bind(model: ChatUsersData) {
            binding.apply {
                tvMessage.text = model.modMsg
                val options = RequestOptions()
                options.centerCrop()
                options.placeholder(R.drawable.user_circle_1179465)
                Glide.with(binding.root).load(model.sentBy.image)
                        .apply(options).into(ivUserImage)

                val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                val date: Date = dateFormat.parse(model.sentAt)
                val formatter: DateFormat = SimpleDateFormat("HH:mm a")
                val dateStr: String = formatter.format(date)
                tvTime.text = dateStr.toUpperCase()
            }
        }
    }

    inner class OtherUserMessagesViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(model: ChatUsersData) {
            binding.apply {
                tvMessage.text = model.modMsg
                val options = RequestOptions()
                options.centerCrop()
                options.placeholder(R.drawable.user_circle_1179465)
                Glide.with(binding.root).load(model.sentBy.image)
                        .apply(options).into(ivUserImage)

                val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                val date: Date = dateFormat.parse(model.sentAt)
                val formatter: DateFormat = SimpleDateFormat("HH:mm a")
                val dateStr: String = formatter.format(date)
                tvTime.text = dateStr.toUpperCase()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        //return position % 2
        //return position % 2 * 2;
//        if(position==0)
//            return LAYOUT_ONE;
//        else
//            return LAYOUT_TWO;

        if(getItem(position).sentBy._id.equals(myId)){
            return LAYOUT_ONE;
        }else{
            return LAYOUT_TWO;
        }

    }


    override fun getItem(position: Int): ChatUsersData {
        return super.getItem(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        when (viewType) {
            0 -> {
                val binding =
                    ItemChatOtherUserBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return MyMessagesViewHolder(binding)
            }

            else -> {

                val binding =
                    ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return OtherUserMessagesViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

//        if(holder.getItemViewType() == LAYOUT_ONE)
//        {
//            val currentItem = getItem(position)
//            (holder as MyMessagesViewHolder).bind(currentItem)
//        }
//
//        if(holder.getItemViewType() == LAYOUT_TWO)
//        {
//            val currentItem = getItem(position)
//            (holder as OtherUserMessagesViewHolder).bind(currentItem)
//        }

        val currentItem = getItem(position)
        if (currentItem != null) {
            if (holder is MyMessagesViewHolder)  holder.bind(currentItem)
            else if (holder is OtherUserMessagesViewHolder) holder.bind(currentItem)
        }
    }

    companion object {

        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ChatUsersData>() {
            override fun areItemsTheSame(
                oldItem: ChatUsersData,
                newItem: ChatUsersData
            ): Boolean {
                return true
            }

            override fun areContentsTheSame(
                oldItem: ChatUsersData,
                newItem: ChatUsersData
            ): Boolean {
                return true
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: ChatUsersData)
    }



    fun updateList(myId2: String) {
        myId = myId2
        notifyDataSetChanged()
    }

}