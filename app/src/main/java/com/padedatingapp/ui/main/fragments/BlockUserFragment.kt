package com.padedatingapp.ui.main.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.birimo.birimosports.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.adapter.BlockUserAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentBlockBinding
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.blockUser.BlockModel
import com.padedatingapp.model.blockUser.BlockUserModel
import com.padedatingapp.model.blockUser.Data
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.BlockUserVM
import kotlinx.android.synthetic.main.fragment_messages.*
import org.koin.android.ext.android.inject

class BlockUserFragment : DataBindingFragment<FragmentBlockBinding>(), BlockUserAdapter.OnItemClickListener {

    companion object {
        var TAG = "BlockUserFragment"
    }

    private val chatVM by inject<BlockUserVM>()
    private var progressDialog: CustomProgressDialog? = null
    private val sharedPref by inject<SharedPref>()
    private lateinit var userObject : UserModel

    private lateinit var adapter1: BlockUserAdapter

    var list_data = ArrayList<Data>()

    override fun layoutId(): Int = R.layout.fragment_block
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = chatVM
        viewBinding.lifecycleOwner = this

        initComponents()
        initObservables()
    }




    private fun initComponents() {
        chatVM.token = sharedPref.getString(AppConstants.USER_TOKEN)
        userObject =
            Gson().fromJson(
                sharedPref.getString(AppConstants.USER_OBJECT),
                UserModel::class.java
            )

//        var list = ArrayList<DummyModel>()
//        repeat(10) {
//            list.add(DummyModel())
//        }

//        adapter1 = MatchesAtChatAdapter(this)
////        adapter2.submitList(list)
////        adapter2.notifyDataSetChanged()
//        viewBinding.rvMatches.adapter = adapter1
//        viewBinding.rvMatches.layoutManager = LinearLayoutManager(requireContext()).also {
//            it.orientation = RecyclerView.HORIZONTAL
//        }
//
//
//        adapter = MessagesListAdapter(this)
//        viewBinding.rvMessageList.adapter = adapter
//        viewBinding.rvMessageList.layoutManager = LinearLayoutManager(requireContext())



        viewBinding.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
                //activity?.finish()
                findNavController().popBackStack()

        }

    }



    private fun initObservables() {
        chatVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }


        chatVM.blockUsersResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "ChatUser")
        }


        chatVM.unblockUserResponse.observe(viewLifecycleOwner) {
            getLiveDataUnBlock(it, "ChatUser")
        }

        Log.e(TAG, "onViewCreated11")


        val jsonObj = JsonObject()
        jsonObj.addProperty("limit", 10)
        jsonObj.addProperty("page", 1)


        chatVM.callBlockUserApi()



    }




    private fun getLiveData(response: Resource<BlockModel>?, type: String) {

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "ChatUser" -> {
                        val data = response.data as BlockModel
                        Log.e(MessagesFragment.TAG, "dataAA " + data.toString())
                        data?.let {
                            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                                Log.e(MessagesFragment.TAG, "listAA " + data.data.size)
                                list_data = data.data as ArrayList<Data>

                                if (list_data.size == 0) {
                                    tvMsg.visibility = View.VISIBLE
                                } else {
                                    tvMsg.visibility = View.GONE
                                }


                                adapter1 = BlockUserAdapter(this)
                                viewBinding.rvBlockUser.adapter = adapter1
                                viewBinding.rvBlockUser.layoutManager = LinearLayoutManager(
                                    requireContext()
                                ).also {
                                    it.orientation = RecyclerView.HORIZONTAL
                                }

                                adapter1.submitList(list_data)
                                adapter1.notifyDataSetChanged()

                            } else {
                                toast(data.message)
                            }
                        }
                    }
                }
            }
            Resource.Status.ERROR -> {
                progressDialog?.dismiss()
                toast(response.getErrorMessage().toString())
            }
            Resource.Status.CANCEL -> {
                progressDialog?.dismiss()
            }
        }
    }



    private fun getLiveDataUnBlock(response: Resource<BlockUserModel>?, type: String) {

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "ChatUser" -> {
                        val data = response.data as BlockUserModel
                        Log.e(MessagesFragment.TAG, "dataAA " + data.toString())
                        data?.let {
                            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                                toast(data.message)
                                findNavController().popBackStack()
                            } else {
                                toast(data.message)
                            }
                        }
                    }
                }
            }
            Resource.Status.ERROR -> {
                progressDialog?.dismiss()
                toast(response.getErrorMessage().toString())
            }
            Resource.Status.CANCEL -> {
                progressDialog?.dismiss()
            }
        }
    }




    override fun onItemClick(model: Data) {
        Log.e(TAG, "onItemClick_model " + model._id)
//        val jsonObj = JsonObject()
//        jsonObj.addProperty("limit", 10)
//        jsonObj.addProperty("page",1)



        showDialogOK(requireActivity(),
            "Do you want to unblock this user?",
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        dialog.dismiss()
                        chatVM.callUnBlockApi(model._id)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
                }
            })

    }


    private fun showDialogOK(
        splashScreen: Activity,
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(splashScreen)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
             .setNegativeButton("CANCEL", okListener)
//            .setCancelable(false)
            .create()
            .show()
    }

}