package com.padedatingapp.base
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.padedatingapp.PadeDatingApp

abstract class BaseActivity : AppCompatActivity() {
    @LayoutRes
    abstract fun layoutId(): Int

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
        setContentView(layoutId())

//       var app = PadeDatingApp()
//        app.initializeSocket(applicationContext)
    }
}