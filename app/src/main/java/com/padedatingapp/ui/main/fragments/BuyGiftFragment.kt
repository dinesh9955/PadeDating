package com.padedatingapp.ui.main.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.padedatingapp.R
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentBuyGiftBinding
import com.padedatingapp.model.ChatIDModel
import com.padedatingapp.model.plans.Doc
import com.padedatingapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.header_layout.view.*
import kotlinx.android.synthetic.main.item_premium_packst.*


class BuyGiftFragment : DataBindingFragment<FragmentBuyGiftBinding>(){

    private lateinit var planData: Doc

    override fun layoutId(): Int = R.layout.fragment_buy_gift
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {

      //  val bundle = arguments

        var title = arguments?.getString("title","")

//        var title = arguments?.getString("title","")

        planData  = arguments?.getParcelable<Doc>("planData") as Doc

        tvPackName.text = planData.name
        tvPackValidity.text = ""+planData.type + " months"
        tvPackValidity.visibility = View.VISIBLE

        tvPackPrice.text = planData.price.units +" "+ planData.price.amount

        viewBinding.header.tvTitle.text = title
        viewBinding.header.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }

        viewBinding.btnBuyNow.setOnClickListener {
            if (title =="Buy Gift Card")
            showCongratsBottomSheet()
            else
                findNavController().popBackStack()
        }

        viewBinding.llTop.setOnClickListener{
            requireActivity().hideKeyboard()
        }

    }

    private fun showCongratsBottomSheet() = try {
        val dialog = Dialog(requireActivity(), R.style.dialog_style)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setDimAmount(0f)
        dialog.setCancelable(false)
        val dialogView = layoutInflater.inflate(R.layout.bottomsheet_gift_card_purchased, null)
        dialog.setContentView(dialogView)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        val btnDone = dialogView.findViewById<TextView>(R.id.btnDone)
        val ivClose = dialogView.findViewById<ImageView>(R.id.ivClose)

        ivClose?.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack(R.id.buyGiftCardsListFragment, true)
        }
        btnDone?.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack(R.id.buyGiftCardsListFragment, true)
        }

        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

}