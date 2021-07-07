package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.padedatingapp.R
import com.padedatingapp.adapter.GiftCardsAdapter
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentGiftCardsBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.utils.hideKeyboard

class GiftCardsFragment : DataBindingFragment<FragmentGiftCardsBinding>(),
    GiftCardsAdapter.OnItemClickListener {

    override fun layoutId(): Int = R.layout.fragment_gift_cards
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        viewBinding.header.tvTitle.text = getString(R.string.gift_cards)
        var list = ArrayList<DummyModel>()
        repeat(13) {
            list.add(DummyModel())
        }
        var adapter = GiftCardsAdapter(this)
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
        viewBinding.rvGiftCardList.adapter = adapter
        viewBinding.rvGiftCardList.layoutManager = LinearLayoutManager(requireContext())

        viewBinding.header.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }
        viewBinding.btnBuyMore.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().navigate(GiftCardsFragmentDirections.actionToBuyGiftCard())
        }
    }

    override fun onItemClick(model: DummyModel) {
        Log.e("BuyPremiumFragment", "onItemClick: ", )

    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

}