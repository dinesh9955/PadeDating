package com.padedatingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.annotation.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.stripe.android.*
//import com.stripe.android.model.ConfirmPaymentIntentParams
//import com.stripe.android.model.StripeIntent
//import com.stripe.android.view.PaymentMethodsActivityStarter

class xyz1 : AppCompatActivity(){

    companion object{
        var TAG = "xyz1"
    }


    private lateinit var detector: BarcodeDetector



//    private  var paymentMethodId:String="pm_1HJJ6nAKcjKtlZOHN79JOkPe"
//    private  val clientSecret:String="pi_1HJNNtAKcjKtlZOHbeYMvCLU_secret_FzF0mL6a94RkebcdJ5gvtHr0n"
//    private  var paymentSession: PaymentSession?=null
//    private  var stripe: Stripe?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.xyz1)


        // mainModel.setLocations(getLocations());

        // String sssssxx = new Gson().toJson(mainModel);

//        generateNoteOnSD(xyz.this , "track.txt", "");
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


//        PaymentConfiguration.init(
//            this@xyz1,
//            "pk_test_51CiPu1Iuz09BIRfI2jDraDneZ1NUdC9zh5OXorg8NeKZgNirmXyIo0p8LWPxtCUucdpUhUQI5M8mvuRUYIuhxLr9006nzziOmM"
//        )
//
//
//        makePayment()


        detector = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build()
        detector.setProcessor(processor)

    }


    private val processor = object : Detector.Processor<Barcode> {
        override fun release() {
        }
        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
            if (detections != null && detections.detectedItems.isNotEmpty()) {        val barcode = detections?.detectedItems
                if (barcode?.size() ?: 0 > 0) {
                    // show barcode content value
                    Toast.makeText(this@xyz1,  barcode?.valueAt(0)?.displayValue ?: "", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//    fun initializeCustomerSession(context: Context?, EphemeralKeyRawJson:String){
//
//        context?.let {context->
//
//            CustomerSession.initCustomerSession(
//                context,
//                StripeDemoEphemeralKeyProvider(EphemeralKeyRawJson)
//            )
//        }}
//
//
//    class StripeDemoEphemeralKeyProvider(private val ephemeralKeyRawJson: String) : EphemeralKeyProvider {
//
//        override fun createEphemeralKey(
//            @Size(min = 4) apiVersion: String,
//            keyUpdateListener: EphemeralKeyUpdateListener
//        ) {
//
//            keyUpdateListener.onKeyUpdate(ephemeralKeyRawJson)
//
//        }
//    }
//
//
//    private fun makePayment() {
//        initializeCustomerSession(this,"cus_CFRA95y1cKuNH7 ")
//        paymentSession = PaymentSession(
//            this,
//            createPaymentSessionConfig()
//        )
//
//        paymentSession?.init(createPaymentSessionListener())
//
//        PaymentMethodsActivityStarter(this)
//            .startForResult(
//                PaymentMethodsActivityStarter.Args.Builder()
//                    .build()
//            )
//    }
//
//
//
//    private fun createPaymentSessionConfig(): PaymentSessionConfig {
//        return PaymentSessionConfig.Builder()
//            .setShippingMethodsRequired(false)
//            .setShippingInfoRequired(false)
//            .build()
//    }
//
//    private fun confirmPayment(
//        clientSecret: String,
//        paymentMethodId: String
//    ) {
//
//        stripe = Stripe(
//            applicationContext,
//            PaymentConfiguration.getInstance(this).publishableKey)
//
//        stripe?.confirmPayment(
//            this,
//            ConfirmPaymentIntentParams.createWithPaymentMethodId(
//                paymentMethodId,
//                clientSecret
//            )
//        )
//
//    }
//
//    private fun createPaymentSessionListener(): PaymentSession.PaymentSessionListener {
//        return object : PaymentSession.PaymentSessionListener {
//            override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
//                Log.e(TAG,"onCommunicatingStateChanged "+isCommunicating)
//            }
//
//            override fun onError(errorCode: Int, errorMessage: String) {
//                Log.e(TAG,"onError "+errorCode+"  "+errorMessage)
//            }
//
//            override fun onPaymentSessionDataChanged(data: PaymentSessionData) {
//                Log.e(TAG,"onPaymentSessionDataChanged "+data.isPaymentReadyToCharge+"  "+data)
//
//                if (data.isPaymentReadyToCharge){
//                    confirmPayment(
//                        clientSecret,paymentMethodId
//                    )
//                }
//            }
//        }
//    }
//
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PaymentMethodsActivityStarter.REQUEST_CODE) {
//
//            data?.let {
//                val result = PaymentMethodsActivityStarter.Result.fromIntent(data)
//
//                val paymentMethod = result?.paymentMethod
//                Log.e(TAG, "paymentMethodId : " + paymentMethod?.id)
//                paymentMethodId = paymentMethod?.id.toString()
//                paymentSession?.handlePaymentData(requestCode, resultCode, it)
//            }
//        }else {
//            stripe?.onPaymentResult(
//                requestCode,
//                data,
//                object : ApiResultCallback<PaymentIntentResult> {
//                    override fun onSuccess(result: PaymentIntentResult) {
//                        val paymentIntent = result.intent
//                        when (paymentIntent.status) {
//                            StripeIntent.Status.Succeeded -> {
//                                Log.e(TAG, "Payment Success")
//                            }
//                            StripeIntent.Status.RequiresPaymentMethod -> {
//                                Log.e(
//                                    TAG,
//                                    "Payment Failed " + paymentIntent.lastPaymentError?.message
//                                )
//                            }
//                            else -> {
//                                Log.e(TAG, "Payment status unknown " + paymentIntent.status)
//
//                            }
//                        }
//                    }
//
//                    override fun onError(e: Exception) {
//                        Log.e(TAG, "Payment Error " + e.localizedMessage)
//                    }
//                })
//        }
//    }


}