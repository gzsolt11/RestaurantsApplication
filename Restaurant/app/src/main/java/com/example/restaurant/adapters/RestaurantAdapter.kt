package com.example.restaurant.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurant.R
import com.example.restaurant.data.User.User
import com.example.restaurant.data.imageEntity.RestaurantImage
import com.example.restaurant.data.restaurantEntityAndResponse.Restaurant
import com.example.restaurant.data.viewmodels.RestaurantImageViewModel
import com.example.restaurant.data.viewmodels.UserViewModel
import com.example.restaurant.mainScreen.MainScreenFragmentDirections
import kotlinx.android.synthetic.main.restaurant_item.view.*

class RestaurantAdapter(var user: User?): RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    private var restaurantList = emptyList<Restaurant>()
    private var restaurantImageList = emptyList<RestaurantImage>()

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
            var position2 = 0
            var kapott = false
            for(i in 0..restaurantImageList.size-1){
                if(restaurantImageList[i].restaurantId == currentItem.id){
                    position2 = 0
                    if(user != null && restaurantImageList[i].userId == user!!.id){
                        if(restaurantImageList[i].id > restaurantImageList[position2].id) {
                            position2 = i
                            kapott = true
                        }
                    }else{
                        position2 = i
                        kapott = true
                    }
                }
            }
            if(!kapott) {
                Glide.with(this)
                    .load(currentItem.image_url)
                    .placeholder(R.drawable.restaurant_placeholder)
                    .centerCrop()
                    .into(restaurantImageView)
            } else{
                Glide.with(this)
                    .load(restaurantImageList[position2].photo)
                    .placeholder(R.drawable.restaurant_placeholder)
                    .centerCrop()
                    .into(restaurantImageView)
            }

            titleTextView.text = currentItem.name
            adressTextView.text = currentItem.address
            priceTextView.text = currentItem.price.toString()


        }

        holder.itemView.restaurantItem.setOnClickListener{
            Toast.makeText(it.context,currentItem.name, Toast.LENGTH_SHORT).show()
            val action = MainScreenFragmentDirections.actionMainScreenFragmentToDetailScreenFragment(currentItem,user)
            holder.itemView.findNavController().navigate(action)
        }


    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    fun setData(restaurants: List<Restaurant>){
        restaurantList = restaurants
        notifyDataSetChanged()
    }

    fun setImageData(restaurantImage: List<RestaurantImage>){
        restaurantImageList = restaurantImage
        notifyDataSetChanged()
    }

}