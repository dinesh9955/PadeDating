package com.padedatingapp
import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import com.padedatingapp.di.AppModule
import com.padedatingapp.sockets.AppSocketListener
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PadeDatingApp : Application() {
    companion object {
         var mAppLang = "en"
        lateinit var gson: Gson
    }
    override fun onCreate() {
        super.onCreate()
        gson = Gson()
        startKoin {
            androidContext(applicationContext)
          Places.initialize(this@PadeDatingApp, getString(R.string.google_maps_key))
            modules(listOf(
                    AppModule.appModule(this@PadeDatingApp),
                    AppModule.networkModule,
                    AppModule.RemoteApiModule))
        }
      /*  ProcessLifecycleOwner
            .get()
            .lifecycle
            .addObserver(ApplicationObserver())*/

        initializeSocket(applicationContext)
    }



    fun initializeSocket(applicationContext: Context) {
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



}