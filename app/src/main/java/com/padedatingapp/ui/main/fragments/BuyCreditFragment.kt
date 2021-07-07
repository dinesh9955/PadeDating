package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.padedatingapp.R
import com.padedatingapp.adapter.PeopleWhoLikedAdapter
import com.padedatingapp.adapter.RechargeCoinAdapter
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentBuyCreditBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.utils.hideKeyboard


class BuyCreditFragment : DataBindingFragment<FragmentBuyCreditBinding>(),
    RechargeCoinAdapter.OnItemClickListener {

    override fun layoutId(): Int = R.layout.fragment_buy_credit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        viewBinding.header.tvTitle.text = getString(R.string.buy_credit)

        var list = ArrayList<DummyModel>()
        repeat(6) {
            list.add(DummyModel())
        }
        var adapter = RechargeCoinAdapter(this)
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
        viewBinding.rvRechargeCoinList.adapter = adapter
        viewBinding.rvRechargeCoinList.layoutManager = GridLayoutManager(requireContext(),3)

        viewBinding.btnBuyNow.setOnClickListener {
            findNavController().popBackStack()
        }

        viewBinding.header.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }

        viewBinding.llTop.setOnClickListener{
            requireActivity().hideKeyboard()
        }
    }

    override fun onItemClick(model: DummyModel) {
        Log.e("BuyCreditFragment", "onItemClick: ", )
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }
}