package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.padedatingapp.R
import com.padedatingapp.adapter.PeopleWhoLikedAdapter
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentMatchesBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.utils.hideKeyboard

class MatchesFragment : DataBindingFragment<FragmentMatchesBinding>(),
    PeopleWhoLikedAdapter.OnItemClickListener {
    override fun layoutId(): Int = R.layout.fragment_matches
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
    }

    private fun initComponents() {
        var list = ArrayList<DummyModel>()
        repeat(10) {
            list.add(DummyModel())
        }
        var adapter = PeopleWhoLikedAdapter(this)
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
        viewBinding.rvWhoLiked.adapter = adapter
        viewBinding.rvWhoLiked.layoutManager = GridLayoutManager(requireContext(),2)

       viewBinding.ivBack.setOnClickListener {
           findNavController().popBackStack()
       }

        viewBinding.ivNoti.setOnClickListener {
            findNavController().navigate(MatchesFragmentDirections.actionToNotif())
        }
        viewBinding.tvPadeTab.setOnClickListener {
            findNavController().navigate(MatchesFragmentDirections.actionToMeetMe())
        }

        viewBinding.ivWhoLiked.setOnClickListener {
            findNavController().navigate(MatchesFragmentDirections.actionToBuyPremium())
        }

        viewBinding.ivFilters.setOnClickListener {
            findNavController().navigate(MatchesFragmentDirections.actionToFilters())
        }
    }
    override fun onItemClick(model: DummyModel) {
        Log.e("Matches Fragment", "onItemClick: " )
        findNavController().navigate(MatchesFragmentDirections.actionToChat())
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }
}