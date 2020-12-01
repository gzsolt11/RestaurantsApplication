package com.example.restaurant.detailScreen

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.restaurant.MainActivity
import com.example.restaurant.R
import com.example.restaurant.adapters.DetailScreenImageAdapter
import com.example.restaurant.data.favouriteEntity.Favourite
import com.example.restaurant.data.imageEntity.RestaurantImage
import com.example.restaurant.data.viewmodels.FavouriteViewModel
import com.example.restaurant.data.viewmodels.ProfileImageViewModel
import com.example.restaurant.data.viewmodels.RestaurantImageViewModel
import com.example.restaurant.data.viewmodels.RestaurantViewModel
import com.example.restaurant.databinding.FragmentDetailScreenBinding
import kotlinx.android.synthetic.main.detail_image_item.view.*
import kotlinx.coroutines.launch


class DetailScreenFragment : Fragment() {

    private lateinit var binding: FragmentDetailScreenBinding
    lateinit var viewModel: RestaurantViewModel
    lateinit var restaurantImageViewModel: RestaurantImageViewModel
    private val args by navArgs<DetailScreenFragmentArgs>()
    private val CALL_PHONE_REQUEST = 1
    private val CAMERA_REQUEST_CODE = 2
    private val READ_REQUEST_CODE = 3
    private val WRITE_REQUEST_CODE = 4
    private val CAMERA_INTENT_CODE = 5
    private val GALERY_INTENT_CODE = 6
    private lateinit var detailScreenAdapter: DetailScreenImageAdapter
    private lateinit var favouriteViewModel: FavouriteViewModel
    private var isFavourited = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailScreenBinding.inflate(inflater, container, false)
        favouriteViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)

        binding.dropDownImageView.setOnClickListener{
            if(binding.detailsConstraintLayout.visibility == View.GONE){
                binding.detailsConstraintLayout.visibility = View.VISIBLE
            } else{
                binding.detailsConstraintLayout.visibility = View.GONE
            }
        }

        (activity as MainActivity).bottomNavigation.setOnNavigationItemSelectedListener {
            if(it.title.toString() == "Profile"){
                val action = DetailScreenFragmentDirections.actionDetailScreenFragmentToProfileScreenFragment(args.user)
                findNavController().navigate(action)
            }
            if(it.title.toString() == "Home"){
                val action = DetailScreenFragmentDirections.actionDetailScreenFragmentToMainScreenFragment(args.user)
                findNavController().navigate(action)
            }

            false
        }

        viewModel = (activity as MainActivity).viewModel
        restaurantImageViewModel = ViewModelProvider(this).get(RestaurantImageViewModel::class.java)

        binding.restaurantNameTextView.text = args.restaurant?.name
        binding.adressTextView.text = args.restaurant?.address
        binding.cityTextView.text = args.restaurant?.city
        binding.stateTextView.text = args.restaurant?.state
        binding.areaTextView.text = args.restaurant?.area
        binding.postalCodeTextView.text = args.restaurant?.postal_code
        binding.countryCodeTextView.text = args.restaurant?.country
        binding.phoneNumberTextView.text = args.restaurant?.phone
        binding.priceTextView.text = args.restaurant?.price.toString()
        Glide.with(this)
                .load(args.restaurant?.image_url)
                .placeholder(R.drawable.restaurant_placeholder)
                .centerCrop()
                .into(binding.baseRestaurantImage)

        binding.callNowImageView.setOnClickListener{
            checkCallPermissions()
        }

        if(args.user != null) {
            favouriteViewModel.readFavouriteById(args.user!!.id, args.restaurant!!.id).observe(this.viewLifecycleOwner, {

                Log.v("FAVOURITES",it.toString())
                if (it.isEmpty()) {
                    binding.setAsFavouriteImageView.setImageResource(R.drawable.ic_white_star)
                } else {
                    binding.setAsFavouriteImageView.setImageResource(R.drawable.ic_yellow_star)
                    isFavourited = true
                }
            })

            binding.setAsFavouriteImageView.setOnClickListener {
                if (isFavourited) {
                    binding.setAsFavouriteImageView.setImageResource(R.drawable.ic_white_star)
                    favouriteViewModel.deleteFavourite(Favourite(args.user!!.id, args.restaurant!!.id))
                } else {
                    binding.setAsFavouriteImageView.setImageResource(R.drawable.ic_yellow_star)
                    favouriteViewModel.addFavourite(Favourite(args.user!!.id, args.restaurant!!.id))
                }

            }
        }

        binding.uploadButton.setOnClickListener{

            checkWriteStoragePermissions()

            val dialogBuilder = AlertDialog.Builder(this.requireContext())

            dialogBuilder.setMessage("How do you want to upload image ?")
                .setCancelable(true)
                .setPositiveButton("Camera", DialogInterface.OnClickListener { dialog, id ->
                    checkCameraPermissions()
                })
                .setNegativeButton("Galery", DialogInterface.OnClickListener { dialog, id ->
                    checkReadStoragePermissions()
                })


            val alert = dialogBuilder.create()
            alert.setTitle("Uploading image")
            alert.show()

        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        restaurantImageViewModel.readRestaurantImageById(args.restaurant!!.id).observe(this.viewLifecycleOwner, {

            if(it.isNotEmpty()) {
                detailScreenAdapter.setData(it)
                binding.restaurantImagesRecyclerView.visibility = View.VISIBLE
                binding.baseRestaurantImage.visibility = View.INVISIBLE
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun checkCallPermissions() {
        val permission = ContextCompat.checkSelfPermission(this.requireContext(),
            Manifest.permission.CALL_PHONE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("DetailScreenFragment", "Permission to call denied")
            requestPermissions(
                arrayOf(Manifest.permission.CALL_PHONE),
                CALL_PHONE_REQUEST)
        }else{
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + args.restaurant?.phone))
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(intent, CALL_PHONE_REQUEST)
            }
        }
    }


    private fun checkCameraPermissions() {
        val permission = ContextCompat.checkSelfPermission(this.requireContext(),
            Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("ProfileScreen", "Permission to camera is not granted")
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE)
        }else{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(intent, CAMERA_INTENT_CODE)
            }
        }
    }

    private fun checkReadStoragePermissions() {
        val permission = ContextCompat.checkSelfPermission(this.requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("ProfileScreen", "Permission to read external storage denied")
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_REQUEST_CODE)
        }else{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, GALERY_INTENT_CODE)
        }
    }

    private fun checkWriteStoragePermissions() {
        val permission = ContextCompat.checkSelfPermission(this.requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("ProfileScreen", "Permission to write external storage denied")
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_REQUEST_CODE)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            CALL_PHONE_REQUEST -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("DetailScreenFragment", "Permission to call denied by user")
                } else {
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + args.restaurant?.phone))
                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivityForResult(intent, CALL_PHONE_REQUEST)
                    }
                    Log.i("DetailScreenFragment", "Permission to call granted by user")
                }
            }

            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("ProfileScreen", "Camera Permission has been denied by user")
                } else {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivityForResult(intent, CAMERA_INTENT_CODE)
                    }
                    Log.i("ProfileScreen", "Camera Permission has been granted by user")
                }
            }

            READ_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("ProfileScreen", "Read Storage permission has been denied by user")
                } else {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/*"
                    startActivityForResult(intent, GALERY_INTENT_CODE)
                    Log.i("ProfileScreen", "Read Storage permission has been granted by user")
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            CAMERA_INTENT_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    var userId = -1
                    if(args.user != null){
                        userId = args.user!!.id
                    }
                    lifecycleScope.launch{
                        val restaurantImage = RestaurantImage(0,args.restaurant!!.id,userId,data.extras?.get("data") as Bitmap)
                        restaurantImageViewModel.addRestaurantImage(restaurantImage)
                    }

                }
            }
            GALERY_INTENT_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val imageuri = data.data
                    val inputStream = imageuri?.let { activity?.contentResolver?.openInputStream(it) }
                    var bitmap = BitmapFactory.decodeStream(inputStream)
                    var userId = -1
                    if(args.user != null){
                        userId = args.user!!.id
                    }
                    val restaurantImage = RestaurantImage(0,args.restaurant!!.id, userId,bitmap)
                    restaurantImageViewModel.addRestaurantImage(restaurantImage)
                }
            }
        }
    }


    private fun setUpRecyclerView(){
        detailScreenAdapter = DetailScreenImageAdapter()
        binding.restaurantImagesRecyclerView.adapter = detailScreenAdapter
        binding.restaurantImagesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)

    }

}