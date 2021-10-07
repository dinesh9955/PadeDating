package com.padedatingapp.di

import androidx.databinding.library.BuildConfig
import com.birimo.birimosports.utils.SharedPref
import com.padedatingapp.PadeDatingApp
import com.padedatingapp.api.NetworkUrls
import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.remote.NetworkConnectionInterceptor
import com.padedatingapp.api.repository.*
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.vm.EditProfileVM
import com.padedatingapp.ui.onboarding.fragments.about_me.AboutMeVM
import com.padedatingapp.ui.onboarding.fragments.create_account.CreateAccountVM
import com.padedatingapp.ui.onboarding.fragments.forgot_password.ForgotPasswordVM
import com.padedatingapp.ui.onboarding.fragments.login.LoginVM
import com.padedatingapp.ui.onboarding.fragments.newaccount.SignUpVM
import com.padedatingapp.ui.onboarding.fragments.otp.OtpVM
import com.padedatingapp.ui.onboarding.fragments.password_recovery_fragment.PasswordRecoveryVM
import com.padedatingapp.ui.onboarding.fragments.upload_photo.UploadPhotoVM
import com.padedatingapp.utils.ResourceProvider
import com.padedatingapp.vm.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AppModule {

    fun appModule(app: PadeDatingApp): Module = module {
        single { ResourceProvider(androidApplication()) }
        single { CoroutinesManager() }
        single { SharedPref(get()) }
           single { NetworkConnectionInterceptor(get()) }
        //activities
        viewModel { SignUpVM(get(), get(), get()) }
        viewModel { OtpVM(get(), get(), get()) }
        viewModel { CreateAccountVM(get(), get(), get()) }
        viewModel { LoginVM(get(), get(), get()) }
        viewModel { UploadPhotoVM(get(), get(), get()) }
        viewModel { AboutMeVM(get(), get(), get()) }
        viewModel { ForgotPasswordVM(get(), get(), get()) }
        viewModel { PasswordRecoveryVM(get(), get(), get()) }
        viewModel { EditProfileVM(get(), get(), get()) }
        viewModel { BuyGiftCardsListVM(get(), get(), get()) }
        viewModel { MeetMeVM(get(), get(), get()) }
        viewModel { MyMatchesVM(get(), get(), get()) }
        viewModel { ChatUserVM(get(), get(), get()) }
        viewModel { ChatVM(get(), get(), get()) }
        viewModel { ProfileOtherVM(get(), get(), get()) }
        viewModel { ChangePasswordVM(get(), get(), get()) }
        viewModel { BuyPremiumVM(get(), get(), get()) }
        viewModel { BuyGiftVM(get(), get(), get()) }



    }

    val RemoteApiModule = module {
        single { provideRetrofit(get(), NetworkUrls.BASE_URL) }
        single { SignUpRepo(get()) }
        single { OtpRepo(get()) }
        single { LoginRepo(get()) }
        single { UploadPhotoRepo(get()) }
        single { AboutMeRepo(get()) }
        single { ForgotPasswordRepo(get()) }
        single { EditProfileRepo(get()) }
        single { GiftCardRepo(get()) }
        single { HomeRepo(get()) }
        single { ChatRepo(get()) }
        single { ProfileOtherRepo(get()) }


        factory {
            createWebService<PadeDatingApi>(
                get()
            )
        }
    }

    val networkModule = module {
        single { providesHttplogging() }
        single { providesOkHttpClient(get()) }
    }


    private fun provideRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun providesOkHttpClient(interceptor: NetworkConnectionInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(90L, TimeUnit.SECONDS)
            .readTimeout(90L, TimeUnit.SECONDS)
            .addInterceptor(interceptor).build()
    }

    private fun providesHttplogging(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    inline fun <reified T> createWebService(retrofit: Retrofit): T = retrofit.create(T::class.java)
}

/*class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request();
        var response: Response = chain.proceed(request);
        Log.d("HTTP_RESPONSE_CODE", "############# ${response.code}")
        // todo deal with the issues the way you need to
        if (response.code == 500) {
            return response;
        }
        return response;
    }
}*/
