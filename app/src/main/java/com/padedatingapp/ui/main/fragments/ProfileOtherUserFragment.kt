package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.padedatingapp.R
import com.padedatingapp.adapter.MatchesAtChatAdapter
import com.padedatingapp.adapter.MessagesListAdapter
import com.padedatingapp.adapter.OtherUserImagesAdapter
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentProfileOtherUserBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.utils.hideKeyboard

class ProfileOtherUserFragment : DataBindingFragment<FragmentProfileOtherUserBinding>(),
    OtherUserImagesAdapter.OnItemClickListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
      viewBinding.ivBack.setOnClickListener {
          findNavController().popBackStack()
      }

        var list = ArrayList<DummyModel>()
        repeat(3) {
            list.add(DummyModel())
        }

        var adapter2 = OtherUserImagesAdapter(this)
        adapter2.submitList(list)
        adapter2.notifyDataSetChanged()
        viewBinding.rvImagesList.adapter = adapter2
        viewBinding.rvImagesList.layoutManager = LinearLayoutManager(requireContext()).also {
            it.orientation = RecyclerView.HORIZONTAL
        }


    }


    override fun layoutId(): Int {
        return R.layout.fragment_profile_other_user
    }

    override fun onItemClick(model: DummyModel) {
        Log.e("Profile Other User", "onItemClick: ", )
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }
}