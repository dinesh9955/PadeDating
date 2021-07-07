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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.birimo.birimosports.utils.SharedPref
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.padedatingapp.R
import com.padedatingapp.adapter.SwipeableCardAdapter
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentMeetMeBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.utils.setMarqueText
import com.yuyakaido.android.cardstackview.*
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class MeetMeFragment : DataBindingFragment<FragmentMeetMeBinding>(), CardStackListener,
    SwipeableCardAdapter.OnItemClickListener {
    private lateinit var dialog: Dialog
    private lateinit var adapter: SwipeableCardAdapter
    var list = ArrayList<DummyModel>()
    private lateinit var manager: CardStackLayoutManager
    private val sharedPref by inject<SharedPref>()

    companion object {
        private const val SELECT_ADDRESS_REQUEST_CODE = 1003
    }

    override fun layoutId(): Int = R.layout.fragment_meet_me
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
        setUserData()
    }

    private fun setUserData() {
        if (sharedPref.getString("address") != "") {
            viewBinding.tvMyLocationHome.text = sharedPref.getString("address")
        } else {
            val userObject =
                Gson().fromJson(
                    sharedPref.getString(AppConstants.USER_OBJECT),
                    UserModel::class.java
                )
            viewBinding.tvMyLocationHome.text = userObject.address
        }
        setMarqueText(viewBinding.tvMyLocationHome)
    }

    private fun initComponents() {
        viewBinding.tvMyLocationHome.setOnClickListener {
            showAddressOverlay()
        }
        viewBinding.tvMyMatchesTab.setOnClickListener {
            findNavController().navigate(R.id.action_to_matches)
        }

        viewBinding.ivNoti.setOnClickListener {
            findNavController().navigate(R.id.action_to_notif)
        }

        viewBinding.likeFloating.setOnClickListener {
            // viewBinding.motionLayout.transitionToState(R.id.like)
            showCongratsPopup()
        }

        viewBinding.unlikeFloating.setOnClickListener {
            dislike()
        }
        viewBinding.ivFilters.setOnClickListener {
            findNavController().navigate(MeetMeFragmentDirections.actionToFilters())
        }


        repeat(10) {
            list.add(DummyModel())
        }

        initializeCard()
        adapter = SwipeableCardAdapter(this)
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
        viewBinding.cStack.adapter = adapter
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


    private fun showCongratsPopup() = try {
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
            findNavController().navigate(MeetMeFragmentDirections.actionToChat())
        }
        btnKeepExploring.setOnClickListener {
            dialog.dismiss()
        }
        ivBack.setOnClickListener {
            dialog.dismiss()
        }

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
        Log.e("MeetMeFragment", "onCardSwiped: ")
    }

    override fun onCardRewound() {
        Log.e("MeetMeFragment", "onCardRewound: ")
    }

    override fun onCardCanceled() {
        Log.e("MeetMeFragment", "onCardCanceled: ")
    }

    override fun onCardAppeared(view: View?, position: Int) {
        Log.e("MeetMeFragment", "onCardAppeared: ")
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        Log.e("MeetMeFragment", "onCardDisappeared: ${position} ")
        if (position == list.size - 5) {
            repeat(10) {
                list.add(DummyModel())
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemClick(model: DummyModel) {
        Log.e("MeetMeFragment", "onCardDragging: ")
        findNavController().navigate(MeetMeFragmentDirections.actionToOtherProfile())
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
}