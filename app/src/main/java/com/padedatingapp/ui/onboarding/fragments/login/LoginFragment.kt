package com.padedatingapp.ui.onboarding.fragments.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.ObservableField
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.facebook.*
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentLoginBinding
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.ui.main.HomeActivity
import com.padedatingapp.ui.onboarding.fragments.newaccount.NewAccountFragment
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.utils.togglePasswordVisibility
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginFragment : DataBindingFragment<FragmentLoginBinding>() {

    companion object{
        var TAG = "LoginFragment"
    }


    var fcmToken: String = ""
    var mGoogleSignInClient: GoogleSignInClient? = null
    lateinit var gso: GoogleSignInOptions
    lateinit var callbackManager: CallbackManager
    val RC_SIGN_IN=120


    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var mAuth: FirebaseAuth

    var idOb = ObservableField("")
    var firstnameOb = ObservableField("")
    var lastNameOb = ObservableField("")
    var emailOb = ObservableField("")
//    var facebookToken: String? = ""
//
//    var callbackManager: CallbackManager? = null



    private val loginVM by inject<LoginVM>()
    private val sharedPref by inject<SharedPref>()
    private var progressDialog: CustomProgressDialog? = null

    override fun layoutId(): Int = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.vm = loginVM
        viewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireContext())


        callbackManager = CallbackManager.Factory.create()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        getFcmToken()
        printHashKey(requireContext())

        initComponents()
    }

    private fun initComponents() {
        loginVM.isEmailLogin = false
        loginVM.password.value = ""

        if (sharedPref.getBoolean(AppConstants.REMEMBER_ME)) {
            loginVM.isRememberMe.value = true
            loginVM.email.value = (sharedPref.getString(AppConstants.USER_EMAIL))
            loginVM.phoneNo.value = sharedPref.getString(AppConstants.USER_PHONE)
            viewBinding.ccp.setCountryForPhoneCode(sharedPref.getInt(AppConstants.USER_COUNTRY_CODE))
            loginVM.password.value = (sharedPref.getString(AppConstants.USER_PASSWORD))
        }

        viewBinding.ivHideShowPass.setOnClickListener {
            togglePasswordVisibility(
                    requireContext(),
                    viewBinding.etPassword,
                    viewBinding.ivHideShowPass
            )
        }

        loginVM.countryCode = viewBinding.ccp.selectedCountryCodeWithPlus
        initObservables()
        setUpListeners()
    }

    private fun initObservables() {

        loginVM.isRememberMe.observe(viewLifecycleOwner) {
            sharedPref.setBoolean(AppConstants.REMEMBER_ME, it)
        }
        loginVM.loginResponse.observe(viewLifecycleOwner) {
            getLiveData(
                    it,
                    "loginResponse"
            )
        }

        loginVM._errorMessage.observe(viewLifecycleOwner) {
            if (it != "") toast(it)
        }
    }

    private fun getLiveData(response: Resource<ResultModel<*>>?, type: String) {
        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "loginResponse" -> {
                        val data = response.data as ResultModel<UserModel>
                        Log.e(TAG, "dataAA " + response.data)
                        Log.e(TAG, "dataBB " + data.toString())
                        onLoginResponse(data)
                    }
                }
            }
            Resource.Status.ERROR -> {
                progressDialog?.dismiss()
                toast(response.getErrorMessage().toString())
            }
            Resource.Status.CANCEL -> {
                progressDialog?.dismiss()
            }
        }
    }

    private fun onLoginResponse(data: ResultModel<UserModel>) {

        Log.e(TAG, "onLoginResponse " + data.data.toString())

        data?.let {

            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                sharedPref.setString(AppConstants.USER_TOKEN, it.data?.accessToken!!)


                if(it.data?.dateofbirth.equals("")){
                    sharedPref.setString(AppConstants.SOCIAL_FN, it.data?.firstName ?: "")
                    sharedPref.setString(AppConstants.SOCIAL_LN, it.data?.lastName ?: "")

                    Log.e(NewAccountFragment.TAG, "sentSocialResponseF " + it.data?.firstName ?: "")
                    Log.e(NewAccountFragment.TAG, "sentSocialResponseL " + it.data?.lastName ?: "")

                    findNavController().navigate(LoginFragmentDirections.actionToCreateNewAccount(email = it.data?.email
                            ?: "", phone = it.data?.phoneNo
                            ?: "", countryCode = it.data?.countryCode ?: ""))

                }else{
                    if (sharedPref.getBoolean(AppConstants.REMEMBER_ME)) {
                        sharedPref.setBoolean(AppConstants.REMEMBER_ME, true)
                        sharedPref.setString(AppConstants.USER_EMAIL, it.data.email)
                        sharedPref.setString(AppConstants.USER_PHONE, it.data.phoneNo)
                        if(!it.data.countryCode.equals("")){
                            sharedPref.setInt(
                                    AppConstants.USER_COUNTRY_CODE,
                                    it.data.countryCode.replace("+", "").toInt()
                            )
                        }

                        sharedPref.setString(
                                AppConstants.USER_PASSWORD,
                                viewBinding.etPassword.text.toString()
                        )
                    }
                    else{
                        sharedPref.setBoolean(AppConstants.REMEMBER_ME, false)
                        sharedPref.setString(AppConstants.USER_EMAIL, "")
                        sharedPref.setString(AppConstants.USER_PHONE, "")
                        sharedPref.setInt(
                                AppConstants.USER_COUNTRY_CODE,
                                viewBinding.ccp.selectedCountryCode.replace("+", "").toInt()
                        )
                    }
                    when (it.data!!.profileStatus) {
                        1 -> {
                            findNavController().navigate(LoginFragmentDirections.actionToUploadPhoto("login"))
                        }
                        2 -> {
                            findNavController().navigate(
                                    LoginFragmentDirections.actionToSignUpAboutFragment(
                                            "Sign Up", "login"
                                    )
                            )
                        }
                        3 -> {
                            toast(it.message)
                            sharedPref.setString(AppConstants.USER_ID, it.data._id)
                            sharedPref.setString(AppConstants.USER_OBJECT, Gson().toJson(it.data))
                            startActivity(Intent(requireContext(), HomeActivity::class.java))
                            requireActivity().finish()
                        }

//                        0 -> {
//                            toast(it.message)
//                            sharedPref.setString(AppConstants.USER_ID, it.data._id)
//                            sharedPref.setString(AppConstants.USER_OBJECT, Gson().toJson(it.data))
//                            startActivity(Intent(requireContext(), HomeActivity::class.java))
//                            requireActivity().finish()
//                        }
                    }
                }


            } else {
                toast(data.message)
            }
        }
    }

    private fun setUpListeners() {
        viewBinding.ccp.setOnCountryChangeListener {
            loginVM.countryCode = viewBinding.ccp.selectedCountryCodeWithPlus
        }

        viewBinding.cbRememberMe.setOnCheckedChangeListener { buttonView, isChecked ->
            loginVM.isRememberMe.value = isChecked
        }

        viewBinding.tvContinueWith.setOnClickListener {
            toggleEmailPhone()
        }
        viewBinding.llRoot.setOnClickListener {
            requireActivity().hideKeyboard()
        }

        /*   viewBinding.btnLogin.setOnClickListener {
               startActivity(Intent(requireContext(), HomeActivity::class.java))
               requireActivity().finish()
           }*/

        viewBinding.tvSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionToSignUp())
        }

        viewBinding.tvForgotPassword.setOnClickListener {
            /*if(viewBinding.clPhone.isVisible)
            findNavController().navigate(LoginFragmentDirections.actionToForgotPassword("phone"))
            else
            findNavController().navigate(LoginFragmentDirections.actionToForgotPassword("email"))*/
            showAccountVerificationChoice()
        }


        viewBinding.facebookImage.setOnClickListener {
           facebookSignIn()
        }

        viewBinding.googleImage.setOnClickListener {
            googleSignIn()
        }

        viewBinding.instagramImage.setOnClickListener {
           // facebookSignIn()
        }
    }

    private fun toggleEmailPhone() {
        requireActivity().hideKeyboard()
        viewBinding.etEmail.isVisible = !viewBinding.etEmail.isVisible
        viewBinding.clPhone.isVisible = !viewBinding.clPhone.isVisible
        viewBinding.tvContinueWith.text = if (viewBinding.clPhone.isVisible) {
            loginVM.isEmailLogin = false
            getString(R.string.continue_with_email)
        } else {
            loginVM.isEmailLogin = true
            getString(R.string.continue_with_phone)
        }
    }

    private fun showAccountVerificationChoice() = try {
        val dialogView = layoutInflater.inflate(R.layout.dialog_account_verification_type, null)
        val dialog = BottomSheetDialog(requireContext(), R.style.TransparentDialog)
        val tvPhone = dialogView.findViewById<TextView>(R.id.tvPhone)
        val tvPhoneValue = dialogView.findViewById<TextView>(R.id.tvPhoneValue)
        val tvEmail = dialogView.findViewById<TextView>(R.id.tvEmail)
        val tvEmailValue = dialogView.findViewById<TextView>(R.id.tvEmailValue)
        val btnCancel = dialogView.findViewById<TextView>(R.id.btnCancel)
        tvPhone?.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(LoginFragmentDirections.actionToForgotPassword("phone"))
        }
        tvPhoneValue?.isVisible = false
        tvEmail?.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(LoginFragmentDirections.actionToForgotPassword("email"))
        }
        tvEmailValue?.isVisible = false
        btnCancel?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(dialogView)
        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }


    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }





    private fun facebookSignIn() {
        LoginManager.getInstance().loginBehavior = LoginBehavior.WEB_ONLY
        LoginManager.getInstance()
            .logInWithReadPermissions(context as Activity, listOf("email", "public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager, object :
                FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                fcmToken = result?.accessToken?.token.toString()
                Log.e("Facebook............", " id : " + result?.accessToken?.token)

                //added new
                val graph: GraphRequest =
                        GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()) { `object`, response ->
                            Log.e("data of fb ", " all data facebook $`object` response  $response")
                            getFacebookData(`object`!!)
                        }
                val bundle: Bundle = Bundle()
                bundle.putString("fields", "id,first_name,last_name,email,gender")
                graph.parameters = bundle
                graph.executeAsync()
            }

            override fun onCancel() {
                Log.d("TAG", "Login attempt cancelled.")
            }

            override fun onError(error: FacebookException?) {
                Log.d("TAG", "Login attempt failed.")
            }
        })
    }





    private fun getFacebookData(jsonObject: JSONObject) {

        val id: String = jsonObject.getString("id")
        val pic: String = "https://graph.facebook.com/$id/picture?type=large"
        val firstName: String = jsonObject.getString("first_name")
        val lastname: String = jsonObject.getString("last_name")




        var Email: String = ""
        var gender: String = ""
        if (jsonObject.has("email"))
            Email = jsonObject.getString("email")

        ////****   facebook data ....

        Log.e("call", "FacebookLogin id: $id")
        Log.e("call", "FacebookLogin firstname: $firstName")
        Log.e("call", "FacebookLogin lastname: $lastname")
        Log.e("call", "FacebookLogin profile: $pic")
        Log.e("call", "FacebookLogin email: $Email")

        idOb.set(id)
        firstnameOb.set(firstName)
        lastNameOb.set(lastname)
        emailOb.set(Email)

         callSocialLoginApi("facebook")
    }




    fun googleSignIn() {

// Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(requireActivity().getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        signOut()

        mAuth = FirebaseAuth.getInstance()
        signIn()
        Log.e(TAG, "googleSignIn")
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        requireActivity().startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    fun handleGoogleSignin(task: Task<GoogleSignInAccount>) {
        Log.e(TAG, "handleGoogleSignin")
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            Log.d("MainActivity", "firebaseAuthWithGoogle:" + account.id)

            idOb.set(account.id)
            firstnameOb.set(account.givenName)
            lastNameOb.set(account.familyName)
            emailOb.set(account.email)

            Log.e(TAG, "gt" + idOb.get().toString())
            Log.e(TAG, "gt" + firstnameOb.get().toString())
            Log.e(TAG, "gt" + lastNameOb.get().toString())
            Log.e(TAG, "gt" + emailOb.get().toString())

            callSocialLoginApi("google")



        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.e("MainActivity", "Google sign in failed", e)
        }

    }


    private fun signOut() {
        if(googleSignInClient != null){
            googleSignInClient.signOut();
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e(TAG, "onActivityResult " + requestCode + " " + resultCode)

        super.onActivityResult(requestCode, resultCode, data)
        //SignInMethod for Google
        if (requestCode == RC_SIGN_IN)
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.e(TAG, "firebaseAuthWithGoogle:" + task.isSuccessful)
            handleGoogleSignin(task)
//             vm!!.callSocialLoginApi()

        } else {
            // Pass the activity result back to the Facebook SDK
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
    }



    private fun getFcmToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("fcm_task", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                fcmToken = task.result?.token!!
                sharedPref.setString("fcmToken", "" + fcmToken)
                Log.e("device_Token", " " + fcmToken)
            })
    }


    fun printHashKey(context: Context) {
        try {
            val info: PackageInfo = context.getPackageManager().getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "printHashKey()", e)
        }
    }





    fun callSocialLoginApi(type : String){
        val jsonObj = JsonObject()
        if(type.equals("facebook")){
            jsonObj.addProperty("facebookId", "" + idOb.get())
        }else if(type.equals("google")){
            jsonObj.addProperty("googleId", "" + idOb.get())
        }
        jsonObj.addProperty("email", "" + emailOb.get())
        jsonObj.addProperty("firstName", "" + firstnameOb.get())
        jsonObj.addProperty("lastName", "" + lastNameOb.get())
        jsonObj.addProperty("deviceType", "ANDROID")
        jsonObj.addProperty("deviceToken", FirebaseInstanceId.getInstance().getToken())
        FirebaseInstanceId.getInstance().getToken();
        Log.e("REGISTER_RQST_BODY_DATA", "validateInputs: $jsonObj")
        loginVM.callSocialApi(
                jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
        )
    }


}
