package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.padedatingapp.R
import com.padedatingapp.adapter.OtherUserImagesAdapter
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentProfileOtherUserBinding
import com.padedatingapp.model.DocImage
import com.padedatingapp.model.DummyModel
import com.padedatingapp.model.MeetMeData
import com.padedatingapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_profile_other_user.*

class ProfileOtherUserFragment : DataBindingFragment<FragmentProfileOtherUserBinding>(),
    OtherUserImagesAdapter.OnItemClickListener {

    companion object{
        var TAG = "ProfileOtherUserFragment"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val bundle = arguments
//
//        var person  = bundle?.getParcelable<MeetMeData>("meetMeModel") as MeetMeData
//
//        Log.e(TAG, "myFoodHistory "+person?.firstName)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
      viewBinding.ivBack.setOnClickListener {
          findNavController().popBackStack()
      }

        //var list = ArrayList<DummyModel>()
//        repeat(3) {
//            list.add(DummyModel())
//        }

        var adapter2 = OtherUserImagesAdapter(this)
//        adapter2.submitList(list)
        adapter2.notifyDataSetChanged()
        viewBinding.rvImagesList.adapter = adapter2
        viewBinding.rvImagesList.layoutManager = LinearLayoutManager(requireContext()).also {
            it.orientation = RecyclerView.HORIZONTAL
        }


        val bundle = arguments

        var person  = bundle?.getParcelable<MeetMeData>("meetMeModel") as MeetMeData

        Log.e(TAG, "myFoodHistory "+person?.firstName)

        Glide.with(requireActivity()).load(person?.image)
                .apply(RequestOptions().placeholder(R.drawable.user_place_holder)).into(ivUserPic)

        tvOtherUserName.text = person.firstName+" "+person.lastName+", "+person.age
        tvAboutDesc.text = person.description
        tvEmployementType.text = person.work

        adapter2.submitList(person.docImage)

    }


    override fun layoutId(): Int {
        return R.layout.fragment_profile_other_user
    }

    override fun onItemClick(model: DocImage) {
        Log.e("Profile Other User", "onItemClick: ", )
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }
}