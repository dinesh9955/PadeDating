package com.padedatingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.padedatingapp.R
import com.padedatingapp.databinding.ItemUploadPhotoBinding
import com.padedatingapp.model.ImageModel
import com.stfalcon.imageviewer.StfalconImageViewer

class UploadImageAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ImageModel, UploadImageAdapter.UploadItemViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class UploadItemViewHolder(private val binding: ItemUploadPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

              binding.root.setOnClickListener {
                  if(getItem(adapterPosition).source == "add"){
                      listener.onItemClick(getItem(adapterPosition),"add")
                  }
                  else if(getItem(adapterPosition).type == "image"){
                     showImageInFullScreen(binding.root.context,getItem(adapterPosition).source,binding.ivPhoto)
                  }

                  else if (getItem(adapterPosition).type == "video"){
                      listener.onItemClick(getItem(adapterPosition),"show_in_video_player")
                  }
              }

            binding.ivRemove.setOnClickListener {
                if(getItem(adapterPosition).source != "add"){
                    listener.onItemClick(getItem(adapterPosition),"remove")
                }
            }

        }

        fun bind(model: ImageModel) {
            binding.apply {
                if (model.source == "add") {
                    binding.ivPhoto.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.root.context,
                            R.drawable.ic_add_image
                        )
                    )
                    binding.ivRemove.isVisible = false
                }
                else{
                    binding.ivRemove.isVisible = true
                    Glide.with( binding.root.context).load(model.source).into(binding.ivPhoto)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadItemViewHolder {
        val binding =
            ItemUploadPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UploadItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UploadItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {
        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ImageModel>() {
            override fun areItemsTheSame(
                oldItem: ImageModel,
                newItem: ImageModel
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(
                oldItem: ImageModel,
                newItem: ImageModel
            ): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: ImageModel,actionType:String)
    }


    private fun showImageInFullScreen(ctx: Context, url: String, imageView: ImageView) {
        var images = arrayListOf(url)
        StfalconImageViewer.Builder(ctx, images) { view, image ->
            Glide.with(ctx).load(image).into(view)
        }.allowZooming(true)
            .allowSwipeToDismiss(true).withTransitionFrom(imageView).show()
    }


}