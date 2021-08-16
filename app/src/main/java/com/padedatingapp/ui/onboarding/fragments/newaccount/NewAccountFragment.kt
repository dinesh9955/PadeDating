package com.padedatingapp.ui.onboarding.fragments.newaccount

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
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
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentNewAccountBinding
import com.padedatingapp.model.OtpData
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.ui.onboarding.fragments.login.FacebookEventObject
import com.padedatingapp.ui.onboarding.fragments.login.LoginFragment
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_new_account.*
import org.json.JSONObject
import org.koin.android.ext.android.inject
import com.google.android.gms.tasks.OnCompleteListener
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.padedatingapp.ui.main.HomeActivity
import com.padedatingapp.ui.onboarding.fragments.login.LoginFragmentDirections
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class NewAccountFragment : DataBindingFragment<FragmentNewAccountBinding>() {

    companion object{
        var TAG = "NewAccountFragment"
    }

    var fcmToken: String = ""
    var mGoogleSignInClient: GoogleSignInClient? = null
    lateinit var gso: GoogleSignInOptions
    lateinit var callbackManager: CallbackManager
    val RC_SIGN_IN=120
    var jsonObject: FacebookEventObject? = null


    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var mAuth: FirebaseAuth


    var idOb = ObservableField("")
    var firstnameOb = ObservableField("")
    var lastNameOb = ObservableField("")
    var emailOb = ObservableField("")



    private val signUpVM by inject<SignUpVM>()
    private val sharedPref by inject<SharedPref>()
    private var progressDialog: CustomProgressDialog? = null
    override fun layoutId(): Int = R.layout.fragment_new_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.vm = signUpVM
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



        initComponents()
    }

    private fun initComponents() {


        signUpVM.countryCode = ccp.selectedCountryCodeWithPlus

        viewBinding.btnGetStarted.setOnClickListener {
            showAccountVerificationChoice()
        }

        viewBinding.tvLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        viewBinding.llRoot.setOnClickListener {
            requireActivity().hideKeyboard()
        }

        viewBinding.ccp.setOnCountryChangeListener {
            signUpVM.countryCode = ccp.selectedCountryCodeWithPlus
        }

        signUpVM._errorMessage.observe(viewLifecycleOwner) {
            if (it != "") toast(it)
        }


        viewBinding.facebookImage.setOnClickListener {
            facebookSignIn()
        }

        viewBinding.googleImage.setOnClickListener {
           // googleSignIn()
        }

        viewBinding.instagramImage.setOnClickListener {
            //facebookSignIn()
        }

        initObservables()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun initObservables() {
        signUpVM.loginResponse.observe(viewLifecycleOwner) {
            getLiveData(
                it,
                "registerUserResponse"
            )
        }
        signUpVM.sendOtpResponse.observe(viewLifecycleOwner) {
            getLiveData(
                it,
                "sentOtpResponse"
            )
        }

        signUpVM.socialResponse.observe(viewLifecycleOwner) {
            getLiveData(
                    it,
                    "sentSocialResponse"
            )
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
                    "registerUserResponse" -> {
                        var data = response.data as ResultModel<UserModel>

                        Log.e(TAG, "registerUserResponse "+data.toString())

                        onRegisterUserResponse(data)
                    }

                    "sentOtpResponse" -> {
                        var data = response.data as ResultModel<OtpData>

                        Log.e(TAG, "sentOtpResponse "+data.toString())
                        onSendOtpResponse(data)
                    }

                    "sentSocialResponse" -> {
                        var data = response.data as ResultModel<UserModel>

                        Log.e(TAG, "sentSocialResponse "+data.toString())
                        sentSocialResponse(data)
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


    private fun onRegisterUserResponse(data: ResultModel<UserModel>?) {
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                signUpVM.phoneNo.value = data.data!!.phoneNo
                signUpVM.countryCode = data.data!!.countryCode
                signUpVM.email.value = data.data!!.email
                if (it.data!!.isEmailVerified || it.data!!.isPhoneVerified){
                   // findNavController().navigate(NewAccountFragmentDirections.actionToCreateNewAccount())
                    sharedPref.setString(AppConstants.USER_TOKEN,it.data?.accessToken)
                    findNavController().navigate(NewAccountFragmentDirections.actionToCreateNewAccount(email = it.data?.email?:"",phone = it.data?.phoneNo?:"",countryCode = it.data?.countryCode?:""))
                }
                else
                    showAccountVerificationChoice()
            } else {
                toast(data.message)
            }
        }
    }


    private fun sentSocialResponse(data: ResultModel<UserModel>?) {
        Log.e(TAG, "sentSocialResponse")
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                if(it.data?.dateofbirth.equals("")){
                    sharedPref.setString(AppConstants.SOCIAL_FN, it.data?.firstName?:"")
                    sharedPref.setString(AppConstants.SOCIAL_LN, it.data?.lastName?:"")

                    Log.e(TAG, "sentSocialResponseF "+it.data?.firstName?:"")
                    Log.e(TAG, "sentSocialResponseL "+it.data?.lastName?:"")
                    it.data?.accessToken?.let { it1 ->
                        sharedPref.setString(AppConstants.USER_TOKEN,
                            it1
                        )
                    }
                    findNavController().navigate(NewAccountFragmentDirections.actionToCreateNewAccount(email = it.data?.email?:"",phone = it.data?.phoneNo?:"",countryCode = it.data?.countryCode?:""))

                }else{
                    it.data?.accessToken?.let { it1 ->
                        sharedPref.setString(AppConstants.USER_TOKEN,
                            it1
                        )
                    }
                    when (it.data!!.profileStatus) {
                        1 -> {

                            findNavController().navigate(NewAccountFragmentDirections.actionToUploadPhoto("login"))
                        }
                        2 -> {
                            findNavController().navigate(
                                NewAccountFragmentDirections.actionToSignUpAboutFragment(
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


//                    toast(it.message)
//                    it.data?.let { it1 -> sharedPref.setString(AppConstants.USER_ID, it1._id) }
//                    sharedPref.setString(AppConstants.USER_OBJECT, Gson().toJson(it.data))
//                    startActivity(Intent(requireContext(), HomeActivity::class.java))
//                    requireActivity().finish()
                }


            } else {
                toast(data.message)
            }
        }
    }


    private fun onSendOtpResponse(data: ResultModel<OtpData>?) {
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                when (signUpVM.verificationType) {
                    "email" -> {
                        findNavController().navigate(
                            NewAccountFragmentDirections.actionNewAccountToVerifyOtp(
                                "email",
                                email = signUpVM.email.value.toString()
                            )
                        )
                    }
                    "phone" -> {
                        findNavController().navigate(
                            NewAccountFragmentDirections.actionNewAccountToVerifyOtp(
                                "phone",
                                phone = signUpVM.phoneNo.value.toString()
                            )
                        )
                    }
                }

            } else {
                toast(data.message)
            }
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

        tvPhoneValue.text = "" + signUpVM.countryCode + signUpVM.phoneNo.value
        tvEmailValue.text = signUpVM.email.value

        tvPhone?.setOnClickListener {
            signUpVM.verificationType = "phone"
            dialog.dismiss()
              findNavController().navigate(NewAccountFragmentDirections.actionNewAccountToVerifyOtp("phone",phone = signUpVM.phoneNo.value.toString(),countryCode = signUpVM.countryCode))
          //  toast("Currently Unavailable")
        }
        tvPhoneValue?.setOnClickListener {
            signUpVM.verificationType = "phone"
            dialog.dismiss()
             findNavController().navigate(NewAccountFragmentDirections.actionNewAccountToVerifyOtp("phone",phone = signUpVM.phoneNo.value.toString(),countryCode = signUpVM.countryCode))
         //   toast("Currently Unavailable")
        }
        tvEmail?.setOnClickListener {
            dialog.dismiss()
            signUpVM.verificationType = "email"
            signUpVM.callSendOtpApi()

        }
        tvEmailValue?.setOnClickListener {
            dialog.dismiss()
            signUpVM.verificationType = "email"
            signUpVM.callSendOtpApi()
        }
        btnCancel?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(dialogView)
        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
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
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        requireActivity().startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    fun handleGoogleSignin(task: Task<GoogleSignInAccount>) {
        Log.e(LoginFragment.TAG, "handleGoogleSignin")
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            Log.d("MainActivity", "firebaseAuthWithGoogle:" + account.id)

            idOb.set(account.id)
            firstnameOb.set(account.givenName)
            lastNameOb.set(account.familyName)
            emailOb.set(account.email)

            Log.e(LoginFragment.TAG, "gt" + idOb.get().toString())
            Log.e(LoginFragment.TAG, "gt" + firstnameOb.get().toString())
            Log.e(LoginFragment.TAG, "gt" + lastNameOb.get().toString())
            Log.e(LoginFragment.TAG, "gt" + emailOb.get().toString())

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
                    sharedPref.setString("fcmToken", ""+fcmToken)
                    Log.e("device_Token", " " + fcmToken)
                })
    }



    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }




    fun callSocialLoginApi(type : String){
        val jsonObj = JsonObject()
        if(type.equals("facebook")){
            jsonObj.addProperty("facebookId", "" + idOb.get())
        }else if(type.equals("google")){
            jsonObj.addProperty("googleId", "" + idOb.get())
        }
        jsonObj.addProperty("email", "" + emailOb.get())
        jsonObj.addProperty("firstName", ""+firstnameOb.get())
        jsonObj.addProperty("lastName", ""+lastNameOb.get())
        jsonObj.addProperty("deviceType", "ANDROID")
        jsonObj.addProperty("deviceToken", FirebaseInstanceId.getInstance().getToken())
        FirebaseInstanceId.getInstance().getToken();
        Log.e("REGISTER_RQST_BODY_DATA", "validateInputs: $jsonObj")
        signUpVM.callSocialApi(
                jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
        )
    }



}