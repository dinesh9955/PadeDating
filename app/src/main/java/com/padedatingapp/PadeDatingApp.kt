package com.padedatingapp
import android.app.Application
import com.google.android.libraries.places.api.Places
import com.padedatingapp.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PadeDatingApp : Application() {
    companion object {
         var mAppLang = "en"
    }
    override fun onCreate() {
        super.onCreate()
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
    }
}