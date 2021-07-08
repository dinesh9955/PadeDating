package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.padedatingapp.R
import com.padedatingapp.adapter.PremiumPacksAdapter
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentBuyPremiumBinding
import com.padedatingapp.model.Doc
import com.padedatingapp.model.DummyModel
import com.padedatingapp.utils.hideKeyboard

class BuyPremiumFragment : DataBindingFragment<FragmentBuyPremiumBinding>(),
    PremiumPacksAdapter.OnItemClickListener {

    override fun layoutId(): Int = R.layout.fragment_buy_premium
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {

        var from = arguments?.getString("from","")

        var list = ArrayList<DummyModel>()
        repeat(5) {
            list.add(DummyModel())
        }
        var adapter = PremiumPacksAdapter(this)
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
        viewBinding.rvPremiumPackList.adapter = adapter
        viewBinding.rvPremiumPackList.layoutManager = LinearLayoutManager(requireContext())

        viewBinding.ivBack.setOnClickListener {
            if (from == "loyalty_points")
            {
                findNavController().popBackStack(R.id.loyaltyPointFragment,true)
            }
            else
            findNavController().popBackStack()
        }
    }

    override fun onItemClick(model: DummyModel) {
        Log.e("BuyPremiumFragment", "onItemClick: ", )
        findNavController().navigate(BuyPremiumFragmentDirections.actionToBuy("Buy Premium"))
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }
}