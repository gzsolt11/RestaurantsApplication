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
import com.example.restaurant.data.favouriteEntity.Favourite
import com.example.restaurant.data.imageEntity.RestaurantImage
import com.example.restaurant.data.restaurantEntityAndResponse.Restaurant
import com.example.restaurant.data.viewmodels.RestaurantImageViewModel
import com.example.restaurant.data.viewmodels.UserViewModel
import com.example.restaurant.mainScreen.MainScreenFragmentDirections
import com.example.restaurant.profileScreen.ProfileScreenFragmentDirections
import kotlinx.android.synthetic.main.restaurant_item.view.*

class RestaurantAdapter(var user: User?, var isMainScreen: Boolean): RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    private var restaurantList = emptyList<Restaurant>()
    private var allRestaurant = emptyList<Restaurant>()
    private var restaurantImageList = emptyList<RestaurantImage>()
    private var favouirtedRestaurantList = emptyList<Favourite>()
    var isFavourites = false

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
            var positionUserPic = 0
            var positionNonUserPic = 0
            var kapott = false
            // megkeresi a felhasznalo kepet ha felotltott a bizonyos vendeglohoz es azt rakja elore ha nem kapott akkor a legujabb kepet
            for(i in 0..restaurantImageList.size-1){
                if(restaurantImageList[i].restaurantId == currentItem.id){
                    if(user != null ){
                        if(restaurantImageList[i].userId == user!!.id) {
                            positionUserPic = i
                            kapott = true
                        }else{
                            positionNonUserPic = i
                            kapott = true
                        }
                    }else{
                        positionNonUserPic = i
                        kapott = true
                    }
                }
            }
            if(!kapott) {
                Glide.with(this)
                    .load(currentItem.image_url)
                    .placeholder(R.drawable.restaurant_placeholder)
                        .circleCrop()
                    .into(restaurantImageView)
            } else{
                var position2 = 0
                if(positionUserPic == 0){
                    position2 = positionNonUserPic
                } else{
                    position2 = positionUserPic
                }
                Glide.with(this)
                    .load(restaurantImageList[position2].photo)
                    .placeholder(R.drawable.restaurant_placeholder)
                        .circleCrop()
                    .into(restaurantImageView)
            }
            // ha be van csillagozva akkor sargara rakja a csillagot
            if(user != null) {
                for (element in favouirtedRestaurantList) {
                    if (element.userId == user!!.id && element.restaurantId == currentItem.id){
                        favouriteImageView.setImageResource(R.drawable.ic_yellow_star)
                        break
                    } else{
                        favouriteImageView.setImageResource(R.drawable.ic_white_star)
                    }
                }
            }

            titleTextView.text = currentItem.name
            adressTextView.text = currentItem.address
            priceTextView.text = currentItem.price.toString()


        }
        // navigation
        holder.itemView.restaurantItem.setOnClickListener{
            if(isMainScreen){
                val action = MainScreenFragmentDirections.actionMainScreenFragmentToDetailScreenFragment(currentItem,user)
                holder.itemView.findNavController().navigate(action)
            } else{
                val action = ProfileScreenFragmentDirections.actionProfileScreenFragmentToDetailScreenFragment(currentItem,user)
                holder.itemView.findNavController().navigate(action)
            }
        }


    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    fun setData(restaurants: List<Restaurant>){
        restaurantList = restaurants
        allRestaurant = restaurants
        notifyDataSetChanged()
    }

    fun setImageData(restaurantImage: List<RestaurantImage>){
        restaurantImageList = restaurantImage
        notifyDataSetChanged()
    }

    fun setFavouriteData(favourites: List<Favourite>){
        favouirtedRestaurantList = favourites
        notifyDataSetChanged()
    }

    fun setFavouriteData2(favourites: List<Favourite>){
        favouirtedRestaurantList = favourites
        val favouriteRestaurantIds = favouirtedRestaurantList.map{ it -> it.restaurantId}
        restaurantList = restaurantList.filter{ it -> favouriteRestaurantIds.contains(it.id)}
        notifyDataSetChanged()
    }

    fun changeToFavourites(){
        val favouriteRestaurantIds = favouirtedRestaurantList.map{ it -> it.restaurantId}
        restaurantList = restaurantList.filter{ it -> favouriteRestaurantIds.contains(it.id)}
        isFavourites = true
        notifyDataSetChanged()
    }

    fun changeBackFromFavourites(){
        restaurantList = allRestaurant
        isFavourites = false
        notifyDataSetChanged()
    }

}