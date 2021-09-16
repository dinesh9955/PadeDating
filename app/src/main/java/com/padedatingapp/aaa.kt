package com.padedatingapp

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.padedatingapp.ui.chats.PageTransformer
import com.padedatingapp.ui.main.fragments.ChatFragment
import com.vanniktech.emoji.EmojiImageView
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.emoji.Emoji
import kotlinx.android.synthetic.main.fragment_chat.*


class aaa : AppCompatActivity() {
    var emojiPopup: EmojiPopup? = null

    var heightDiff: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_chat)

        val mRootWindow: Window = window
        val mRootView: View = mRootWindow.getDecorView().findViewById(android.R.id.content)

        emojiPopup = EmojiPopup.Builder.fromRootView(mainView).build(etTypingMessage)

        val r: Resources = resources
        val px = Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 36f, r.getDisplayMetrics()
            )
        )

//        val dps = 36f
//        val pxs = dps * resources.displayMetrics.density

        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(OnGlobalLayoutListener {
            heightDiff = mRootView.getRootView().getHeight() - mRootView.getHeight()

            if (emojiPopup != null) {
                emojiPopup?.dismiss();
            }
            emojiPopup?.setPopupWindowHeight(heightDiff - (px * 2))
        })


    //    emojiPopup?.show()

        ivEmoji.setOnClickListener { ignore ->
            val keyboard: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (keyboard.isAcceptingText()) {
                emojiPopup!!.toggle()
            }
        }

       // getKeyboardHeight()

      //  setUpEmojiPopup();



    }

}