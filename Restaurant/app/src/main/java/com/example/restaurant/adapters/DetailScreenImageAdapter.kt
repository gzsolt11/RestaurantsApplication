package com.example.restaurant.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurant.R
import com.example.restaurant.data.imageEntity.RestaurantImage
import kotlinx.android.synthetic.main.detail_image_item.view.*

class DetailScreenImageAdapter(): RecyclerView.Adapter<DetailScreenImageAdapter.ImageViewHolder>() {

    private var imageList = emptyList<RestaurantImage>()

    inner class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.detail_image_item,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = imageList[position]
        holder.itemView.apply{
            Glide.with(this)
                .load(currentItem.photo)
                .placeholder(R.drawable.restaurant_placeholder)
                .centerCrop()
                .into(imageItem)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }


    fun setData(images: List<RestaurantImage>){
        imageList = images
        notifyDataSetChanged()
    }
}