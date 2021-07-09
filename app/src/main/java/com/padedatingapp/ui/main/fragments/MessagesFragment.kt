package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.padedatingapp.R
import com.padedatingapp.adapter.MatchesAtChatAdapter
import com.padedatingapp.adapter.MessagesListAdapter
import com.padedatingapp.adapter.PeopleWhoLikedAdapter
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentMessagesBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.model.MeetMeData
import com.padedatingapp.utils.hideKeyboard

class MessagesFragment : DataBindingFragment<FragmentMessagesBinding>(),
    PeopleWhoLikedAdapter.OnItemClickListener, MatchesAtChatAdapter.OnItemClickListener {
    override fun layoutId(): Int = R.layout.fragment_messages
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        initComponents()
    }

    private fun initComponents() {
        var list = ArrayList<DummyModel>()
        repeat(10) {
            list.add(DummyModel())
        }
        var adapter = MessagesListAdapter(this)
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
        viewBinding.rvMessageList.adapter = adapter
        viewBinding.rvMessageList.layoutManager = LinearLayoutManager(requireContext())

        var adapter2 = MatchesAtChatAdapter(this)
        adapter2.submitList(list)
        adapter2.notifyDataSetChanged()
        viewBinding.rvMatches.adapter = adapter2
        viewBinding.rvMatches.layoutManager = LinearLayoutManager(requireContext()).also {
            it.orientation = RecyclerView.HORIZONTAL
        }

    }

    override fun onItemClick(model: DummyModel) {
        findNavController().navigate(R.id.action_to_chat_fragment)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

    override fun onItemClick(model: MeetMeData) {
        TODO("Not yet implemented")
    }
}