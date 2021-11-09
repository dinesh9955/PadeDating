package com.padedatingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.padedatingapp.R
import com.padedatingapp.model.loyalityModel.Data
import com.padedatingapp.model.loyalityModel.DataX

import kotlinx.android.synthetic.main.loyality_items.view.*

class LoyalityPointAdapter(val context: Context): RecyclerView.Adapter<LoyalityPointAdapter.myholder>() {

    var myserviceData=ArrayList<com.padedatingapp.model.loyalityModel.DataX>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoyalityPointAdapter.myholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.loyality_items,parent,false)

        return myholder(view)
    }

    override fun onBindViewHolder(holder: LoyalityPointAdapter.myholder, position: Int) {
       holder.tvPoint.text=myserviceData[position].Points.toString() +" Points Gained for Completing "+myserviceData[position].type
    }

    override fun getItemCount(): Int {
        return myserviceData.size
    }

    class myholder(itemView: View):RecyclerView.ViewHolder(itemView){

        val tvPoint=itemView.tvPoints
    }

    fun updateList(serviceData:ArrayList<DataX>){
        myserviceData.clear()
        myserviceData=serviceData
        notifyDataSetChanged()



    }

}
//        ListAdapter<DataX, LoyalityPointAdapter.LoyalityViewHolder>(
//                DELIVERY_ITEM_COMPARATOR
//        ) {
//    var arrayList=ArrayList<DataX>()
//    inner class LoyalityViewHolder(private val binding: LoyalityItemsBinding) :
//            RecyclerView.ViewHolder(binding.root) {
////        init {
////            binding.tvBlockUsers.setOnClickListener {
////                listener.onItemClick(getItem(adapterPosition))
////            }
////        }
//
//        fun bind(model: com.padedatingapp.model.loyalityModel.Data) {
//            binding.apply {
//                val options = RequestOptions()
//                options.centerCrop()
////                options.placeholder(R.drawable.user_circle_1179465)
////                Glide.with(binding.root).load(model.image)
////                        .apply(options).into(ivUserPic)
//                tvPoints.text = model.data[0].Points.toString()
//            }
//        }
//
//        fun bind(model: Data) {
//
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoyalityViewHolder {
//        val binding =
//                LoyalityItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return LoyalityViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: LoyalityViewHolder, position: Int) {
//        val currentItem = getItem(position)
//        if (currentItem != null) {
//            holder.bind(currentItem)
//        }
//    }
//
//    companion object {
//        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Data>() {
//            override fun areItemsTheSame(
//                    oldItem: Data,
//                    newItem: Data
//            ): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun areContentsTheSame(
//                    oldItem: Data,
//                    newItem: Data
//            ): Boolean {
//                TODO("Not yet implemented")
//            }
//        }
//    }
//
//    interface OnItemClickListener {
//        fun onItemClick(model: Data)
//    }
//}