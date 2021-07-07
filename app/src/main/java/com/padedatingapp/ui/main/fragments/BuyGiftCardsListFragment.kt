package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.padedatingapp.R
import com.padedatingapp.adapter.PremiumPacksAdapter
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentBuyGiftCardsListBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.utils.hideKeyboard

class BuyGiftCardsListFragment :  DataBindingFragment<FragmentBuyGiftCardsListBinding>(),
    PremiumPacksAdapter.OnItemClickListener {

    override fun layoutId(): Int = R.layout.fragment_buy_gift_cards_list
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        viewBinding.header.tvTitle.text = getString(R.string.gift_cards)
        var list = ArrayList<DummyModel>()
        repeat(5) {
            list.add(DummyModel())
        }
        var adapter = PremiumPacksAdapter(this)
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
        viewBinding.rvGiftCardList.adapter = adapter
        viewBinding.rvGiftCardList.layoutManager = LinearLayoutManager(requireContext())

        viewBinding.header.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onItemClick(model: DummyModel) {
        Log.e("BuyPremiumFragment", "onItemClick: ", )
          findNavController().navigate(BuyGiftCardsListFragmentDirections.actionToBuy())

    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

}