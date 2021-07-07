package com.padedatingapp.base
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class DataBindingActivity<VB : ViewDataBinding> : BaseActivity() {
    lateinit var viewBinding: VB
    override fun setContentView(layoutResID: Int) {
        viewBinding = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false)
        super.setContentView(viewBinding.root)
    }
}