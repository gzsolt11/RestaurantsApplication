package com.example.restaurant.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurant.R
import com.example.restaurant.data.responses.Restaurant
import com.example.restaurant.databinding.FragmentMainScreenBinding
import kotlinx.android.synthetic.main.restaurant_item.view.*

class RestaurantAdapter: RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    private var restaurantList = emptyList<Restaurant>()

    inner class RestaurantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.restaurant_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val currentItem = restaurantList[position]
        holder.itemView.apply{
            Glide.with(this).load(currentItem.image_url).into(restaurantImageView)
            titleTextView.text = currentItem.name
            adressTextView.text = currentItem.address
            priceTextView.text = currentItem.price.toString()
        }


    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    fun setData(restaurants: List<Restaurant>){
        restaurantList = restaurants
        notifyDataSetChanged()
    }
}