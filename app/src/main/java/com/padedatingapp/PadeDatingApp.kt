package com.padedatingapp
//import com.vanniktech.emoji.facebook.FacebookEmojiProvider
//import com.vanniktech.emoji.ios.IosEmojiProvider
//import com.vanniktech.emoji.twitter.TwitterEmojiProvider
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.multidex.MultiDex
import com.facebook.FacebookSdk
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.padedatingapp.di.AppModule
import com.padedatingapp.sockets.AppSocketListener
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class PadeDatingApp : Application() {
    companion object {
         var mAppLang = "en"
        lateinit var gson: Gson
        var TAG = "PadeDatingApp"
    }

    var crashlytics: FirebaseCrashlytics? = null

    override fun onCreate() {
        super.onCreate()


        FacebookSdk.sdkInitialize(applicationContext)

        FirebaseApp.initializeApp(applicationContext);

        gson = Gson()
        startKoin {
            androidContext(applicationContext)
          Places.initialize(this@PadeDatingApp, getString(R.string.google_maps_key))
            modules(
                listOf(
                    AppModule.appModule(this@PadeDatingApp),
                    AppModule.networkModule,
                    AppModule.RemoteApiModule
                )
            )

        }
      /*  ProcessLifecycleOwner
            .get()
            .lifecycle
            .addObserver(ApplicationObserver())*/

        try {
            crashlytics = FirebaseCrashlytics.getInstance()
            crashlytics!!.log("Start logging!")
            crashlytics!!.setCustomKey("DeviceName", "Android")
            crashlytics!!.setCustomKey("Email", "dinesh.kumar@apptunix.com")
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "getMessage3 " + e.message)
        }

        initializeSocket(applicationContext)

        EmojiManager.install(GoogleEmojiProvider())
//        EmojiManager.install(IosEmojiProvider())
//        EmojiManager.install(TwitterEmojiProvider())
////        EmojiManager.install(GoogleCompatEmojiProvider())
//        EmojiManager.install(FacebookEmojiProvider())
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }



    public fun initializeSocket(applicationContext: Context) {
        try {
            Log.e("initializeSocket", "try")
            AppSocketListener.getInstance().initialize(applicationContext)
        } catch (ex: Exception) {
            Log.e("initializeSocket", "catch")
            ex.printStackTrace()
        }
    }
    fun destroySocketListener() {
        try {
            Log.e("destroySocketListener", "try")
            AppSocketListener.getInstance().destroy()
        } catch (ex: Exception) {
            Log.e("destroySocketListener", "Catch")
            ex.printStackTrace()
        }
    }
    override fun onTerminate() {
        destroySocketListener()
        super.onTerminate()
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}