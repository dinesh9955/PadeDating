package com.padedatingapp.ui.main.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.birimo.birimosports.utils.SharedPref
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.adapter.MeetMeAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentMeetMeBinding
import com.padedatingapp.model.*
import com.padedatingapp.sockets.AppSocketListener
import com.padedatingapp.sockets.SocketUrls
//import com.padedatingapp.ui.MeetMeFragmentDirections
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.utils.setMarqueText
import com.padedatingapp.vm.MeetMeVM
import com.yuyakaido.android.cardstackview.*
import io.socket.emitter.Emitter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class MeetMeFragment : DataBindingFragment<FragmentMeetMeBinding>(), CardStackListener,
        MeetMeAdapter.OnItemClickListener {

    var placeLat : Double = 0.0
    var placeLng : Double = 0.0

    lateinit var locationStatus: Emitter.Listener

    private lateinit var userObject : UserModel

    private lateinit var meetMeData : MeetMeData

    private lateinit var dialog: Dialog
    private lateinit var adapter: MeetMeAdapter
    var list = ArrayList<MeetMeData>()
    private lateinit var manager: CardStackLayoutManager
    private val sharedPref by inject<SharedPref>()


    private val meetMeVM by inject<MeetMeVM>()
    private var progressDialog: CustomProgressDialog? = null

    companion object {
        private const val SELECT_ADDRESS_REQUEST_CODE = 1003
        var TAG = "MeetMeFragment"
    }



    override fun layoutId(): Int = R.layout.fragment_meet_me
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = meetMeVM
        viewBinding.lifecycleOwner = this

        initComponents()
        initObservables()
        setUserData()

    }

    private fun setUserData() {
        if (sharedPref.getString("address", "en") != "") {
            viewBinding.tvMyLocationHome.text = sharedPref.getString("address", "en")
        } else {
            viewBinding.tvMyLocationHome.text = userObject.address
        }
        setMarqueText(viewBinding.tvMyLocationHome)
    }

    private fun initComponents() {

        Log.e(TAG, "initComponents")

        userObject =
                Gson().fromJson(
                        sharedPref.getString(AppConstants.USER_OBJECT, "en"),
                        UserModel::class.java
                )

        meetMeVM.token = sharedPref.getString(AppConstants.USER_TOKEN, "en")

        viewBinding.tvMyLocationHome.setOnClickListener {
            showAddressOverlay()
        }
        viewBinding.tvMyMatchesTab.setOnClickListener {
            findNavController().navigate(R.id.action_to_matches)
        }

        viewBinding.ivNoti.setOnClickListener {
            findNavController().navigate(R.id.action_to_notif)
        }

        viewBinding.ivFilters.setOnClickListener {
            findNavController().navigate(MeetMeFragmentDirections.actionToFilters())
        }


//        repeat(10) {
//            list.add(DummyModel())
//        }

      //  if(list.size == 0){
            adapter =  MeetMeAdapter(requireActivity(), this)
            viewBinding.cStack.adapter = adapter
      //  }




//        val linearLayoutManager1 = LinearLayoutManager(requireContext())
//        linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
//        viewBinding.cStack1!!.layoutManager = linearLayoutManager1
//        adapter =  MeetMeAdapter(requireActivity(), this)
//        viewBinding.cStack1.adapter = adapter


       // adapter.updateData(list)
        //adapter.notifyDataSetChanged()

        initializeCard()


        meetMeVM.callProfileApi(""+userObject._id)



    }




    private fun initializeSockets() {
        Log.e(TAG, "initializeSockets")


        locationStatus = Emitter.Listener { args ->
            activity?.runOnUiThread {
                val data: JSONObject = args[0] as JSONObject

                Log.e("locationStatus ", "message $data")
            }
        }



        callListerners()

    }


    private fun callListerners() {
        AppSocketListener.getInstance().addOnHandler(SocketUrls.LOCATION, locationStatus)

        val json = JSONObject()
        json.put("lat", ""+userObject.latitude)
        json.put("long", ""+userObject.longitude)

        AppSocketListener.getInstance().emit(SocketUrls.LOCATION, json)

    }







    private fun showAddressOverlay() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = object : Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        ) {}
            .setHint("Address")
            .build(requireContext())
        startActivityForResult(intent, SELECT_ADDRESS_REQUEST_CODE)
    }

    private fun initializeCard() {
        manager = CardStackLayoutManager(requireContext(), this)
        manager?.setStackFrom(StackFrom.None)
        manager?.setVisibleCount(10)
        manager?.setTranslationInterval(8.0f)
        manager?.setScaleInterval(0.95f)
        manager?.setSwipeThreshold(0.3f)
        manager?.setMaxDegree(20.0f)
        manager?.setDirections(Direction.HORIZONTAL)
        manager?.setCanScrollHorizontal(true)
        manager?.setCanScrollVertical(true)
        manager?.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager?.setOverlayInterpolator(LinearInterpolator())
        viewBinding.cStack.layoutManager = manager
        viewBinding.cStack.itemAnimator.apply {

            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun dislike() {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Left)
            .setDuration(Duration.Slow.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager?.setSwipeAnimationSetting(setting)
        viewBinding.cStack.swipe()
    }

    private fun like() {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setDuration(Duration.Slow.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager?.setSwipeAnimationSetting(setting)
        viewBinding.cStack.swipe()
    }


    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }




    private fun showCongratsPopup(chatIDModel: ChatIDModel) = try {
        if (::dialog.isInitialized && dialog.isShowing)
            dialog.cancel()
        dialog = Dialog(requireActivity(), R.style.dialog_style)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setDimAmount(0f)
        dialog.setCancelable(true)
        val dialogView = layoutInflater.inflate(R.layout.bottomsheet_perfect_match, null)
        val btnSendMessage = dialogView.findViewById<Button>(R.id.btnSendMessage)
        val btnKeepExploring = dialogView.findViewById<Button>(R.id.btnKeepExploring)
        val ivBack = dialogView.findViewById<ImageView>(R.id.ivBack)
        btnSendMessage.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(MeetMeFragmentDirections.actionToChat(chatIDModel))
        }
        btnKeepExploring.setOnClickListener {
            dialog.dismiss()
        }
        ivBack.setOnClickListener {
            dialog.dismiss()
        }


        val options = RequestOptions()
        options.centerCrop()
        options.placeholder(R.drawable.user_circle_1179465)

        val tvUserOneName = dialogView.findViewById<TextView>(R.id.tvUserOneName)
        val tvUserTwoName = dialogView.findViewById<TextView>(R.id.tvUserTwoName)
        val ivUserOne = dialogView.findViewById<ShapeableImageView>(R.id.ivUserOne)
        val ivUserTwo = dialogView.findViewById<ShapeableImageView>(R.id.ivUserTwo)


        Glide.with(requireActivity()).load(userObject.image)
                .apply(options).into(ivUserOne)
        tvUserOneName.text = userObject.firstName + " "+userObject.lastName

        Glide.with(requireActivity()).load(chatIDModel.receiverImage)
                .apply(options).into(ivUserTwo)
        tvUserTwoName.text = chatIDModel.receiverName




        dialog.setContentView(dialogView)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        dialog.show()


    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        Log.e("MeetMeFragment", "onCardDragging: ")
    }

    override fun onCardSwiped(direction: Direction?) {
        Log.e("MeetMeFragment", "onCardSwiped: "+direction.toString())

        if(direction.toString()?.equals("Left")!!){
            val jsonObj = JsonObject()
            jsonObj.addProperty("action", "dislike")
            meetMeVM.callMeetMeLikeApi(
                    meetMeData._id,
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
            )
            dislike()
        } else if(direction.toString()?.equals("Right")!!){
            val jsonObj = JsonObject()
            jsonObj.addProperty("action", "like")
            meetMeVM.callMeetMeLikeApi(
                    meetMeData._id,
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
            )
            like()
        }


    }

    override fun onCardRewound() {
        Log.e("MeetMeFragment", "onCardRewound: ")
    }

    override fun onCardCanceled() {
        Log.e("MeetMeFragment", "onCardCanceled: ")
    }

    override fun onCardAppeared(view: View?, position: Int) {
        Log.e("MeetMeFragment", "onCardAppeared: ")

        meetMeData = list[position]

        Log.e("MeetMeFragment", "meetMeDataAA: "+meetMeData.firstName)

        viewBinding.likeFloating.setOnClickListener {
            // viewBinding.motionLayout.transitionToState(R.id.like)
//            showCongratsPopup()
           // viewBinding.cStack.swipe()
            Log.e("MeetMeFragment", "setOnClickListener: "+position)



            meetMeData = list[position]

            val jsonObj = JsonObject()
            jsonObj.addProperty("action", "like")

            meetMeVM.callMeetMeLikeApi(
                    list[position]._id,
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
            )

            like()

//            var chatIDModel = ChatIDModel()
//            if(meetMeData != null){
//                chatIDModel.receiverID = meetMeData._id
//                chatIDModel.receiverName = meetMeData.firstName + " "+meetMeData.lastName
//                chatIDModel.receiverImage = meetMeData.image
//
//                showCongratsPopup(chatIDModel)
//            }


        }

        viewBinding.unlikeFloating.setOnClickListener {
//            dislike()
            val jsonObj = JsonObject()
            jsonObj.addProperty("action", "dislike")

            meetMeVM.callMeetMeLikeApi(
                    list[position]._id,
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
            )

            dislike()
        }

    }

    override fun onCardDisappeared(view: View?, position: Int) {
        Log.e("MeetMeFragment", "onCardDisappeared: ${position} ")

        if (position == list.size - 1) {
            viewBinding.likeFloating.visibility = View.GONE
            viewBinding.unlikeFloating.visibility = View.GONE
        }


//        if (position == list.size - 5) {
////            repeat(10) {
////                list.add(DummyModel())
////            }
//            adapter.notifyDataSetChanged()
//        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SELECT_ADDRESS_REQUEST_CODE -> {
                    try {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        try {
                            val addresses: List<Address>
                            val geocoder = Geocoder(requireContext(), Locale.getDefault())
                            addresses = geocoder.getFromLocation(
                                place.latLng?.latitude!!,
                                place.latLng?.longitude!!,
                                1
                            )

//                            placeLat = place.latLng!!.latitude
//                            placeLng = place.latLng!!.longitude

                            Log.e(TAG , "placeLat2 "+placeLat+" "+placeLng)

                            sharedPref.setString("address", addresses[0].getAddressLine(0))
                            viewBinding.tvMyLocationHome.text = addresses[0].getAddressLine(0)
                            requireActivity().hideKeyboard()
                        } catch (e: Exception) {
                            Log.e("HomeFragment", "GeoCoder Exception: $e")
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }






    private fun initObservables() {

        meetMeVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }


        meetMeVM.userModelResponse.observe(viewLifecycleOwner) {
            getLiveDataProfile(it, "Profile")
        }


        meetMeVM.meetMeResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "MeetMe")
        }


        meetMeVM.likeModelResponse.observe(viewLifecycleOwner) {
            getLiveDataLike(it, "LikeMe")
        }


        var filter = sharedPref.getString("filter", "en")

        Log.e(TAG, "filter11 "+filter)


        val jsonObj = JsonObject()

        if(filter.equals("filter")){
            var interest = sharedPref.getString("interest", "en")
            var address = sharedPref.getString("address", "en")
            var dating_prefences = sharedPref.getString("dating_prefences", "en")
//            var employment = sharedPref.getString("employment")
            var age = sharedPref.getInt("age")
            var distance = sharedPref.getInt("distance")

            Log.e(TAG , "interestedInF "+interest.toUpperCase())
//            Log.e(TAG , "placeLat "+userObject.address)
//            Log.e(TAG , "placeLat "+userObject.latitude)
//            Log.e(TAG , "placeLat "+userObject.longitude)

         //   jsonObj.addProperty("interest", interest)
          //  jsonObj.addProperty("datingPreference", dating_prefences)
            jsonObj.addProperty("gender", interest.toUpperCase())
            jsonObj.addProperty("distance",distance)
            jsonObj.addProperty("age", age)
//            jsonObj.addProperty("lat", sharedPref.getString("placeLat"))
//            jsonObj.addProperty("long",sharedPref.getString("placeLng"))


//            jsonObj.addProperty("limit", 4)
//            jsonObj.addProperty("page", 1)

           // initializeSockets()
        }else{

            jsonObj.addProperty("gender", userObject.interestedIn.toUpperCase())
//            jsonObj.addProperty("distance",10)
//            jsonObj.addProperty("age", 25)
//            jsonObj.addProperty("lat", userObject.latitude)
//            jsonObj.addProperty("long", userObject.longitude)
          //  jsonObj.addProperty("limit", 4)
         //   jsonObj.addProperty("page", 1)
            //Log.e(TAG , "placeLat "+placeLat+" "+placeLng)

           // var dd = sharedPref.getString("address")
            Log.e(TAG , "interestedIn "+userObject.interestedIn)
            Log.e(TAG , "placeLat "+userObject.address)
            Log.e(TAG , "placeLat "+userObject.latitude)
            Log.e(TAG , "placeLat "+userObject.longitude)

           // initializeSockets()

        }

        Log.e(TAG, "jsonObjAA "+jsonObj.toString())

        meetMeVM.callMeetMeApi(
                jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
        )


        sharedPref.setString("filter", "")


    }





    private fun getLiveData(response: Resource<MeetMe>?, type: String) {

        Log.e(TAG, "onViewCreated12")

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "MeetMe" -> {
                        val data = response.data as MeetMe
                        Log.e(TAG, "dataAACCCVVV "+data.toString())
                        onMeetMeResponse(data)
                    }
                }
            }
            Resource.Status.ERROR -> {
                progressDialog?.dismiss()
                     toast(response.getErrorMessage().toString())
            }
            Resource.Status.CANCEL -> {
                progressDialog?.dismiss()
            }
        }
    }



    private fun getLiveDataLike(response: Resource<LikeModel>?, type: String) {

        //Log.e(TAG, "onViewCreated12")

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "LikeMe" -> {
                        val data = response.data as LikeModel
                        Log.e(TAG, "dataAAB "+data.toString())
                       // onMeetMeResponse(data)

                        data?.let {
                            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {

                                if(data.data.likedBy.size != 0){
                                    if(data.data.isMatched == true){
                                        var chatIDModel = ChatIDModel()
                                        if(chatIDModel != null){
                                            chatIDModel.receiverID = meetMeData._id
                                            chatIDModel.receiverName = meetMeData.firstName + " "+meetMeData.lastName
                                            chatIDModel.receiverImage = meetMeData.image

                                            showCongratsPopup(chatIDModel)
                                        }

                                    }else{
                                       // like()
                                    }
                                }

                                if(data.data.dislikedBy.size != 0){
                                    //dislike()
                                }


                            } else {
                                toast(data.message)
                            }
                        }
                    }
                }
            }
            Resource.Status.ERROR -> {
                progressDialog?.dismiss()
                toast(response.getErrorMessage().toString())
            }
            Resource.Status.CANCEL -> {
                progressDialog?.dismiss()
            }
        }
    }




    private fun onMeetMeResponse(data: MeetMe) {
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                list = data.data as ArrayList<MeetMeData>
                Log.e(TAG, "listAA "+list.size)



                adapter.updateData(list)
//                adapter.notifyDataSetChanged()

                if(list.size == 0){
                    viewBinding.likeFloating.visibility = View.GONE
                    viewBinding.unlikeFloating.visibility = View.GONE
                }

            } else {
                toast(data.message)
            }
        }
    }

    override fun onItemClick(model: MeetMeData) {
//        var person  = intent.getParcelableExtra<Person>("clicked_person") as Person
       // Log.e(TAG, "myFoodHistoryA "+model?.firstName)


        findNavController().navigate(MeetMeFragmentDirections.actionToOtherProfile(model._id))
    }






    private fun getLiveDataProfile(response: Resource<ResultModel<*>>?, type: String) {

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "Profile" -> {
                        val data = response.data as ResultModel<UserModel>
                        Log.e(TAG, "dataAAZZ " + response.data)
                        Log.e(TAG, "dataBBZZ " + data.toString())

                        //sharedPref.setString(AppConstants.USER_OBJECT, Gson().toJson(data.data))
                         onLoginResponse(data)

//                        if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
//
//                            if(response.data != null){
//                                Glide.with(requireActivity()).load(response.data.data?.image)
//                                    .apply(RequestOptions().placeholder(R.drawable.user_place_holder)).into(ivUserPic)
//
//                                tvOtherUserName.text = response.data.data?.firstName+" "+ (response.data.data?.lastName) +", "+response.data.data?.age
//                                tvAboutDesc.text = response.data.data?.description
//                                tvEmployementType.text = response.data.data?.work
//
//
//                                if(response.data.data?.isApproved == true){
//                                    imageViewThik.visibility = View.VISIBLE
//                                }else{
//                                    imageViewThik.visibility = View.INVISIBLE
//                                }
//
//
//                                adapter2.submitList(response.data.data?.docImage)
//                            }
//
//
//                        }


                    }
                }
            }
            Resource.Status.ERROR -> {
                progressDialog?.dismiss()
                toast(response.getErrorMessage().toString())
            }
            Resource.Status.CANCEL -> {
                progressDialog?.dismiss()
            }
        }
    }



    private fun onLoginResponse(data: ResultModel<UserModel>) {

        Log.e(TAG, "onLoginResponse " + data.data.toString())

        data?.let {

            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                sharedPref.setString(AppConstants.USER_TOKEN, it.data?.accessToken!!)
                sharedPref.setString(AppConstants.USER_OBJECT, Gson().toJson(it.data))
                Log.e(TAG, "dataBBZZXX " + Gson().toJson(it.data))
            } else {
                toast(data.message)
            }
        }
    }


}