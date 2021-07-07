package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.padedatingapp.R
import com.padedatingapp.adapter.NotificationListAdapter
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentNotificationsBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.utils.hideKeyboard

class NotificationsFragment : DataBindingFragment<FragmentNotificationsBinding>(),
    NotificationListAdapter.OnItemClickListener {

    override fun layoutId(): Int = R.layout.fragment_notifications

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
    }

    private fun initComponents() {

        viewBinding.header.tvTitle.text = getString(R.string.notifications)
        var list = ArrayList<DummyModel>()
        repeat(7) {
            list.add(DummyModel())
        }
        var adapter = NotificationListAdapter(this)
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
        viewBinding.rvNotificationList.adapter = adapter
        viewBinding.rvNotificationList.layoutManager = LinearLayoutManager(requireContext())

        viewBinding.header.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onItemClick(model: DummyModel) {
        Log.e("Noti_Fragment", "onItemClick: ", )
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

}