package com.padedatingapp.custom_views

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import com.padedatingapp.R

class CustomProgressDialog (context: Context?) : Dialog(context!!) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_progress_dialog)
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawableResource(R.color.colorGreyTranspaent)
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
   //     window?.decorView?.background = ColorDrawable(Color.WHITE)
//        dialog.progressLoading.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)

    }
}