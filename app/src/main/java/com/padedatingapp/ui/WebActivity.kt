package com.patchoguefd.settings

import android.graphics.Bitmap
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.birimo.birimosports.utils.SharedPref
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.privacyPolicy.Data
import com.padedatingapp.model.privacyPolicy.PrivacyPolicyModel
import com.padedatingapp.vm.WebVM
import kotlinx.android.synthetic.main.fragment_messages.*
import kotlinx.android.synthetic.main.header_layout_with_gradient_back.*
import org.koin.android.ext.android.inject


class WebActivity : AppCompatActivity() {

   // internal lateinit var progressBar: ProgressBar
    internal lateinit var webView: WebView

    var TAG = "WebActivity"
    //internal var url: String? = ""

    private val chatVM by inject<WebVM>()
    private var progressDialog: CustomProgressDialog? = null
    private val sharedPref by inject<SharedPref>()
    private lateinit var userObject : UserModel

    lateinit var textViewTitleBar: TextView
//    override fun layoutId(): Int {
//        TODO("Not yet implemented")
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_activity)

//        viewBinding.vm = chatVM
//        viewBinding.lifecycleOwner = this

      //  getWindow().setBackgroundDrawableResource(R.drawable.background_image);

      //  toolbar = findViewById(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayShowTitleEnabled(false)
        textViewTitleBar = findViewById(R.id.tvTitle) as TextView
        textViewTitleBar.text = "Terms and Conditions"


        webView = findViewById(R.id.webView1) as WebView
      //  progressBar = findViewById(R.id.progressBar1444) as ProgressBar

        val policy = StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .build()
        StrictMode.setThreadPolicy(policy)

        ivBack.setOnClickListener {
            finish()
        }


//        val bundle = intent.extras
//        val url = bundle?.getString("url")
//        val key = bundle?.getString("key")


        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptEnabled = true
        webView.settings.defaultZoom = WebSettings.ZoomDensity.FAR

        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false



        webView.settings.textSize = WebSettings.TextSize.LARGER
      //  textViewTitleBar.text = "" + key!!


        initObservables()



        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                //progressBar.visibility = View.VISIBLE
                            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return super.shouldOverrideUrlLoading(view, url)

            }

            override fun onPageFinished(view: WebView, url: String) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url)
               // progressBar.visibility = View.GONE
                webView!!.loadUrl("javascript:document.body.style.padding= \"2%\"; void 0")
//                                webView!!.loadUrl( "javascript:document.body.style.setProperty(\"color\", \"#ff0000\");" );
//                webView!!.loadUrl("javascript:document.body.style.background =\"#00000000\"; void 0");
////        webView.loadUrl("javascript:document.body.style.fontSize ='20pt'");
              //  webView!!.loadUrl("javascript:document.body.style.color =\"#ff0000\"; void 0");

                //webView!!.setBackgroundColor(0x00000000);

            }


        }



        webView!!.setBackgroundColor(0x00000000);
     //   webView!!.loadUrl("https://stackoverflow.com/users/4248565/volodymyr-baydalka")
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed()
        webView.goBack()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initObservables() {

        chatVM.errorMessage.observe(this) {
            if (it != "") {
                toast(it)
            }
        }


        chatVM.privacyPolicyResponse.observe(this) {
            getLiveData(it, "ChatUser")
        }



        chatVM.callPrivacyPolicyApi()



    }




    private fun getLiveData(response: Resource<PrivacyPolicyModel>?, type: String) {

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "ChatUser" -> {
                        val data = response.data as PrivacyPolicyModel
                        Log.e(TAG, "dataAA " + data.toString())
                        data?.let {
                            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS) {
                                Log.e(TAG, "listAA " + data.data.size)
                                var list_data = data.data as ArrayList<Data>
                                var privacy = list_data[0].privacy
                                var image = list_data[0].image

                                val content = privacy +
                                "</br><p><img src="+image+" /></p>"
                                webView.loadDataWithBaseURL("", content, "text/html", "UTF-8", "")

                               // webView.loadData(privacy, "text/html", "UTF-8");
                            } else {
                                toast(data.message)
                            }
                        }
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




    protected fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}