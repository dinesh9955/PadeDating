package com.padedatingapp.ui.onboarding.fragments.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.*
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.LoginRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.OtpData
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class LoginVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val loginRepo: LoginRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<ResultModel<UserModel>>>()
    var sendOtpResponse = SingleLiveEvent<Resource<ResultModel<OtpData>>>()


//    var firstname = ObservableField("")
//    var lastName = ObservableField("")
//    var emailOb = ObservableField("")
//    var facebookToken: String? = ""
//
//    var callbackManager: CallbackManager? = null

    var countryCode = ""
    var verificationType = "phone"
    var isEmailLogin = false
    var _errorMessage = MutableLiveData<String>()
    var isRememberMe = MutableLiveData<Boolean>()
    var email = MutableLiveData<String>()
    var phoneNo = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var emailPattern = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")


//    companion object {
//        private const val RC_SIGN_IN = 120
//    }
//
//    init {
//        callbackManager = CallbackManager.Factory.create()
//    }

    fun validate() {
        when {
            isEmailLogin -> {
                when {
                    email.value.toString().contains(" ") -> {
                        _errorMessage.value =
                            resourceProvider.getString(R.string.email_should_not_contain_white_spaces)
                    }
//                    email.value.toString().trim().isEmpty() || !email.value.toString()
//                        .matches(Regex(emailPattern)) -> {
//                        _errorMessage.value =
//                            resourceProvider.getString(R.string.please_enter_valid_email)
//                    }
//                    password.value.toString().trim().isEmpty() -> {
//                        _errorMessage.value =
//                            resourceProvider.getString(R.string.please_enter_valid_password)
//                    }
                    else -> {
                        val jsonObj = JsonObject()
                        jsonObj.addProperty("email", email.value)
                        jsonObj.addProperty("password", password.value)
                        jsonObj.addProperty("deviceType", "ANDROID")
                        jsonObj.addProperty("deviceToken", FirebaseInstanceId.getInstance().getToken())
                        FirebaseInstanceId.getInstance().getToken();
                        Log.d("REGISTER_RQST_BODY_DATA", "validateInputs: $jsonObj")
                        callLoginUserApi(
                            jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                        )
                    }
                }
            }

            else -> {

                when {
                    phoneNo.value.toString().trim()
                        .isEmpty() -> {
                        _errorMessage.value =
                            resourceProvider.getString(R.string.please_enter_valid_phone_number)

                    }
//                    phoneNo.value.toString().length < 7 -> {
//                        _errorMessage.value =
//                            resourceProvider.getString(R.string.phone_length_should_be_greater_than_7_characters)
//
//                    }
//                    password.value.toString().trim().isEmpty() -> {
//                        _errorMessage.value =
//                            resourceProvider.getString(R.string.please_enter_valid_password)
//                    }
                    else-> {
                        val jsonObj = JsonObject()
                        jsonObj.addProperty("phoneNo", phoneNo.value)
                        jsonObj.addProperty("countryCode", countryCode)
                        jsonObj.addProperty("password", password.value)
                        jsonObj.addProperty("deviceType", "ANDROID")
                        jsonObj.addProperty("deviceToken", FirebaseInstanceId.getInstance().getToken())
                        Log.d("REGISTER_RQST_BODY_DATA", "validateInputs: $jsonObj")
                        callLoginUserApi(
                            jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                        )
                    }
                }
            }
        }
    }

    private fun callLoginUserApi(body: RequestBody) {
        coroutinesManager.ioScope.launch {
            loginResponse.postValue(Resource.loading(null))
            loginResponse.postValue(loginRepo.login(body))
        }
    }




    fun callSocialApi(toRequestBody: RequestBody) {
        coroutinesManager.ioScope.launch {
            loginResponse.postValue(Resource.loading(null))
            loginResponse.postValue(
                    loginRepo.socialUser(toRequestBody)
            )

        }
    }





//    fun onClicks(type: String) {
//        when (type) {
//            "facebook" -> {
//                facebookSignIn()
//            }
//
//            "google" -> {
//                googleSignIn()
//            }
//        }
//    }






//    private fun facebookSignIn() {
//        LoginManager.getInstance().loginBehavior = LoginBehavior.WEB_ONLY
//        LoginManager.getInstance()
//            .logInWithReadPermissions(context as Activity, listOf("email", "public_profile"))
//        LoginManager.getInstance().registerCallback(callbackManager, object :
//            FacebookCallback<LoginResult> {
//            override fun onSuccess(result: LoginResult?) {
//                facebookToken = result?.accessToken?.token
//                Log.e("Facebook............", " id : " + result?.accessToken?.token)
//
//                //added new
//                val graph: GraphRequest =
//                    GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()) { `object`, response ->
//                        Log.e("data of fb ", " all data facebook $`object` response  $response")
//                        getFacebookData(`object`!!)
//                    }
//                val bundle: Bundle = Bundle()
//                bundle.putString("fields", "id,first_name,last_name,email,gender")
//                graph.parameters = bundle
//                graph.executeAsync()
//            }
//
//            override fun onCancel() {
//                Log.d("TAG", "Login attempt cancelled.")
//            }
//
//            override fun onError(error: FacebookException?) {
//                Log.d("TAG", "Login attempt failed.")
//            }
//        })
//    }
//
//
//
//
//    private fun getFacebookData(jsonObject: JSONObject) {
//
//        val id: String = jsonObject.getString("id")
//        val pic: String = "https://graph.facebook.com/$id/picture?type=large"
//        val firstName: String = jsonObject.getString("first_name")
//        val lastname: String = jsonObject.getString("last_name")
//
//
//
//
//        var Email: String = ""
//        var gender: String = ""
//        if (jsonObject.has("email"))
//            Email = jsonObject.getString("email")
//
//        ////****   facebook data ....
//
//        Log.e("call", "FacebookLogin id: $id")
//        Log.e("call", "FacebookLogin firstname: $firstName")
//        Log.e("call", "FacebookLogin lastname: $lastname")
//        Log.e("call", "FacebookLogin profile: $pic")
//        Log.e("call", "FacebookLogin email: $Email")
//
//        firstname.set(firstName)
//        lastName.set(lastname)
//        emailOb.set(Email)
//
//       // callSocialLoginApi()
//    }
//
//
//
//
//
//
//
//    fun googleSignIn() {
//// Configure Google Sign In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(context.getString(com.facebook.R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(context, gso)
//
//        mAuth = FirebaseAuth.getInstance()
//        signIn()
//    }
//
//    fun signIn() {
//        val signInIntent = googleSignInClient.signInIntent
//        (context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)
//
//    }
//
//    fun handleGoogleSignin(task: Task<GoogleSignInAccount>) {
//
//        try {
//            // Google Sign In was successful, authenticate with Firebase
//            val account = task.getResult(ApiException::class.java)!!
//            Log.d("MainActivity", "firebaseAuthWithGoogle:" + account.id)
//
//            firstname.set(account.givenName)
//            lastName.set(account.familyName)
//            email.set(account.email)
//
//            Log.v("gt",firstname.get().toString())
//            Log.v("gt",email.get().toString())
//            Log.v("gt",lastName.get().toString())
//
//            callSocialLoginApi()
//        } catch (e: ApiException) {
//            // Google Sign In failed, update UI appropriately
//            Log.w("MainActivity", "Google sign in failed", e)
//        }
//
//    }
//
//






//    fun callSocialLoginApi() {
//        val jsonElement = JsonObject()
//        jsonElement.addProperty("email",email.get())
//        jsonElement.addProperty("firstName",firstname.get())
//        jsonElement.addProperty("lastName",lastName.get())
//        jsonElement.addProperty("type", type.get())
//        jsonElement.addProperty("socialId", socialId.get())
//        jsonElement.addProperty("name", name.get())
//        jsonElement.addProperty("deviceType", deviceType.get())
//        jsonElement.addProperty("pushToken", pushToken.get())
//        jsonElement.addProperty("role", Role.get())
//
//        try {
//            RetrofitCall().callService(
//                context,
//                true,
//                "",
//                object : RequestProcessor<Response<SigninResponse>> {
//                    override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<SigninResponse> {
//                        return retrofitApi.social_login(jsonElement)
//                        Log.d("request"," "+ retrofitApi.login(jsonElement))
//                    }
//
//                    override fun onResponse(res: Response<SigninResponse>) {
//                        val response = res.body()!!
//                        if (res.isSuccessful) {
//                            Log.v("Tag","Success")
//                            Log.d("response"," "+ response)
//                            val rememberMeModel = RememberMeData()
//                            if (response.success) {
//                                Log.v("Tag","Successfully")
//
//                                if (ischecked.get()) {
//                                    if (isEmail.get()) {
//                                        rememberMeModel.email = email.get()!!
//                                        rememberMeModel.phone = ""
//                                        rememberMeModel.password = password.get()!!
//                                        // rememberMeModel.countryCode = ""
//                                    } else {
//                                        rememberMeModel.email = ""
//                                        rememberMeModel.phone = phone.get()!!
//                                        rememberMeModel.password = password.get()!!
//                                    }
//                                }
//                                rememberMeModel.isRemember = ischecked.get()
//
//                                PreferenceFile.storeLoginData(context, response)
//
//                                response.data.token?.let {
//                                    PreferenceFile.storeAuthToken(context, it)
//                                }
//                                (context as SignIn).startActivity(Intent(context, MainActivity::class.java))
//                                (context as Activity).finish()
//                            } else {
//                                Log.v("Tag","UnSuccess")
//                                showToast(context, response.message)
//                            }
//                        } else {
//
//                            Log.v("Tag","sdgdrgSuccess")
//
//                            showToast(context, context.getString(com.facebook.R.string.backendError))
//                        }
//                    }
//
//                    override fun onException(message: String?) {
//                        Log.v("Tag","Error")
//
//                        Log.e("signException", "====$message")
//                    }
//                })
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }






}