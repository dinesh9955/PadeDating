package com.padedatingapp.ui.main.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.birimo.birimosports.utils.SharedPref
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.padedatingapp.R
import com.padedatingapp.adapter.ChatListAdapter
import com.padedatingapp.adapter.PremiumPacksAdapter
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentChatBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.model.MeetMeData
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import org.koin.android.ext.android.inject


class ChatFragment : DataBindingFragment<FragmentChatBinding>(),
    PremiumPacksAdapter.OnItemClickListener, ChatListAdapter.OnItemClickListener {
    private lateinit var dialog: Dialog

    private val sharedPref by inject<SharedPref>()

    private lateinit var userObject : UserModel


    override fun layoutId(): Int = R.layout.fragment_chat
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initComponents()
    }



    private fun initComponents() {

        userObject =
                Gson().fromJson(
                        sharedPref.getString(AppConstants.USER_OBJECT),
                        UserModel::class.java
                )


        val bundle = arguments
        var person  = bundle?.getParcelable<MeetMeData>("meetMeModelChat") as MeetMeData
        //myMatchesVM.token = sharedPref.getString(AppConstants.USER_TOKEN)

        val options = RequestOptions()
        options.centerCrop()
        options.placeholder(R.drawable.user_place_holder)

        Glide.with(requireActivity()).load(userObject.image)
                .apply(options).into(viewBinding.ivUserOne)
        viewBinding.tvUserOneName.text = userObject.firstName + " "+userObject.lastName

        Glide.with(requireActivity()).load(person.image)
                .apply(options).into(viewBinding.ivUserTwo)
        viewBinding.tvUserTwoName.text = person.firstName + " "+person.lastName


        var list = ArrayList<DummyModel>()
        repeat(32) {
            list.add(DummyModel())
        }
        var adapter = ChatListAdapter(this)
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
        viewBinding.rvMessageList.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        viewBinding.rvMessageList.layoutManager = layoutManager

        viewBinding.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }

        viewBinding.ivChatOptions.setOnClickListener {
            viewBinding.llChatOptions.isVisible = !viewBinding.llChatOptions.isVisible
        }

        viewBinding.ivChatCall.setOnClickListener {
            showCallPopup()
        }

        viewBinding.ivChatVideoCall.setOnClickListener {
            showCallPopup()
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    override fun onItemClick(model: DummyModel) {
        Log.e("BuyPremiumFragment", "onItemClick: ")
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

    private fun showCallPopup() = try {
        if (::dialog.isInitialized && dialog.isShowing)
            dialog.cancel()
        dialog = Dialog(requireActivity(), android.R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false)
        val dialogView = layoutInflater.inflate(R.layout.dialog_call, null)
        val tvEndCall = dialogView.findViewById<TextView>(R.id.tvEndCall)
        val ivChat = dialogView.findViewById<ImageView>(R.id.ivChat)
        tvEndCall.setOnClickListener {
            dialog.dismiss()
        }
        ivChat.setOnClickListener {
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

}