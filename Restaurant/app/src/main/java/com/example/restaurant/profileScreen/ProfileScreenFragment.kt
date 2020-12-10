package com.example.restaurant.profileScreen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.restaurant.MainActivity
import com.example.restaurant.R
import com.example.restaurant.adapters.RestaurantAdapter
import com.example.restaurant.data.Resource
import com.example.restaurant.data.User.User
import com.example.restaurant.data.favouriteEntity.Favourite
import com.example.restaurant.data.imageEntity.ProfileImage
import com.example.restaurant.data.imageEntity.RestaurantImage
import com.example.restaurant.data.viewmodels.*
import com.example.restaurant.databinding.FragmentProfileScreenBinding
import com.example.restaurant.mainScreen.MainScreenFragmentArgs
import kotlinx.coroutines.launch


class ProfileScreenFragment : Fragment() {

    private lateinit var binding: FragmentProfileScreenBinding
    lateinit var viewModel: RestaurantViewModel
    lateinit var sp: SharedPreferences
    lateinit var userViewModel: UserViewModel
    lateinit var profileImageViewModel: ProfileImageViewModel
    private val args by navArgs<MainScreenFragmentArgs>()
    private lateinit var userList: List<User>
    private var currentUser: User? = null
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var restaurantImageViewModel: RestaurantImageViewModel
    private lateinit var favouriteViewModel: FavouriteViewModel
    private var favouriteList = emptyList<Favourite>()
    private var favouriteRestaurantIds = ArrayList<Int>()

    private val CAMERA_REQUEST_CODE = 1
    private val READ_REQUEST_CODE = 2
    private val WRITE_REQUEST_CODE = 3
    private val CAMERA_INTENT_CODE = 4
    private val GALERY_INTENT_CODE = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = this.requireContext().getSharedPreferences("userid", Context.MODE_PRIVATE)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileScreenBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        profileImageViewModel = ViewModelProvider(this).get(ProfileImageViewModel::class.java)
        restaurantImageViewModel= ViewModelProvider(this).get(RestaurantImageViewModel::class.java)
        favouriteViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)

        setUpRecyclerView(args.user)

        // if the user is logged in then creates view else takes back to login screen
        if(args.user != null){
            currentUser = args.user
            binding.userNameTextView.text = getString(R.string.whole_name, currentUser?.lastName, currentUser?.firstName)
            binding.adressTextView.text = currentUser?.address
            binding.phoneNumberTextView.text =currentUser?.phoneNumber
            binding.emailTextView.text = currentUser?.email

            profileImageViewModel.readProfileImageById(currentUser!!.id).observe(this.viewLifecycleOwner, {

                Log.v("UploadedImage", it.toString())
                if(!it.isEmpty()) {
                    Glide.with(this.requireContext())
                            .asBitmap()
                            .load(it[0].photo)
                            .circleCrop()
                            .into(binding.profilePictureImageView)
                }
            })

        } else{
            val action = ProfileScreenFragmentDirections.actionProfileScreenFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        // setting up navigation
        (activity as MainActivity).bottomNavigation.setOnNavigationItemSelectedListener {
            if(it.title.toString() == "Home"){
                val action = ProfileScreenFragmentDirections.actionProfileScreenFragmentToMainScreenFragment(args.user)
                findNavController().navigate(action)
            }

            false
        }


        // clicking on profile picture to upload
        binding.profilePictureImageView.setOnClickListener{
            Toast.makeText(this.requireContext(), "Clicked", Toast.LENGTH_SHORT).show()

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

        viewModel = (activity as MainActivity).viewModel
        // set the data for the recyclerview in the profile screen
        viewModel.restaurants.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let{restaurantsResponse ->
                        restaurantAdapter.setData(restaurantsResponse.restaurants)
                    }
                }
                is Resource.Error ->{
                    response.message?.let{ message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
            }
        })
        //set images for the restaurants
        restaurantImageViewModel.readAllRestaurantImages.observe(viewLifecycleOwner, Observer {
            restaurantAdapter.setImageData(it)
        })

        // show only the favourited images
        if(args.user != null){
            favouriteViewModel.readFavouriteByUserId(args.user!!.id).observe(this.viewLifecycleOwner, {
                restaurantAdapter.setFavouriteData2(it)
                favouriteList = it
            })
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.logoutButton.setOnClickListener{
            Navigation.findNavController(this.requireView()).navigate(R.id.action_profileScreenFragment_to_mainScreenFragment)

        }
    }


    /**
     * checks for permission and ask for it if it is not granted
     */
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
    /**
     * checks for permission and ask for it if it is not granted
     */
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

    /**
     * checks for permission and ask for it if it is not granted
     */
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

    /**
     * Handles the results of the permissions, call intents
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
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

    /***
     * on the results of the intent do certain things
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            CAMERA_INTENT_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    //binding.profilePictureImageView.setImageBitmap(data.extras?.get("data") as Bitmap)
                    Glide.with(this.requireContext())
                            .asBitmap()
                            .load(data.extras?.get("data") as Bitmap)
                            .circleCrop()
                            .into(binding.profilePictureImageView)

                    lifecycleScope.launch{
                        val profileImage = ProfileImage(currentUser!!.id,currentUser!!.id,data.extras?.get("data") as Bitmap)
                        profileImageViewModel.addProfileImage(profileImage)
                    }

                }
            }
            GALERY_INTENT_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val imageuri = data.data
                    Log.d("IMAGE",data.data.toString())
                    val inputStream = imageuri?.let { activity?.contentResolver?.openInputStream(it) }
                    var bitmap = BitmapFactory.decodeStream(inputStream)
                    Glide.with(this.requireContext())
                            .asBitmap()
                            .load(bitmap)
                            .circleCrop()
                            .into(binding.profilePictureImageView)
                    val profileImage = ProfileImage(0,currentUser!!.id,bitmap)
                    profileImageViewModel.addProfileImage(profileImage)
                }
            }
        }
    }


    private fun setUpRecyclerView(user: User?){
        restaurantAdapter = RestaurantAdapter(user,false)
        binding.restaurantRecyclerView.adapter = restaurantAdapter
        binding.restaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }


}