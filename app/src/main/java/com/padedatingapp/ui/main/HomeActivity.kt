package com.padedatingapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.fxn.OnBubbleClickListener
import com.padedatingapp.R
import com.padedatingapp.base.DataBindingActivity
import com.padedatingapp.databinding.ActivityHomeBinding
import com.padedatingapp.extensions.onNavDestinationSelected
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : DataBindingActivity<ActivityHomeBinding>() {

    override fun layoutId(): Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        val navController = findNavController(R.id.nav_host_fragment_home)
        viewBinding.bottomMenu.setSelected(1)
        viewBinding.bottomMenu.setSelected(0)
        viewBinding.bottomMenu.addBubbleListener(object :OnBubbleClickListener{
            override fun onBubbleClick(id: Int) {
                viewBinding.bottomMenu.onNavDestinationSelected(id, navController)
            }
        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == bottom_menu[0].id ||
                destination.id == bottom_menu[1].id ||
                destination.id == bottom_menu[2].id ||
                destination.id == bottom_menu[3].id){
                viewBinding.bottomMenu.isVisible = true
                viewBinding.bottomMenu.setSelectedWithId(destination.id, false)
            }
            else viewBinding.bottomMenu.isVisible = false
           // destination.displayName ==  "com.padedatingapp:id/editProfileFragment"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach { fragment ->
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}