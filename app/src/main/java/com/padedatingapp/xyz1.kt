package com.padedatingapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.stripe.android.*
import kotlinx.android.synthetic.main.xyz1.*


//import com.stripe.android.model.ConfirmPaymentIntentParams
//import com.stripe.android.model.StripeIntent
//import com.stripe.android.view.PaymentMethodsActivityStarter

class xyz1 : AppCompatActivity(){

    companion object{
        var TAG = "xyz1"
    }

    private var time: CountDownTimer? = null

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


//        detector = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build()
//        detector.setProcessor(processor)



        textScanResult.setOnClickListener {
            timer()
//            var seconds: Long = 1 * 60 * 60
//
//            var ss: Long = seconds % 60
//            var mm: Long = seconds / 60
//            var hh: Long = mm / 60
//
//            Log.e(TAG, "ss() " + ss)
//            Log.e(TAG, "mm() " + mm)
//            Log.e(TAG, "hh() " + hh)






        }


    }


    //var sum: Long = 1 *  60 * 60 * 60 * 1000
    var milliseconds: Int = 0


    private fun timer(){

        var sum: Long = 1 *  60 * 60 * 60 * 1000

        time = object : CountDownTimer(sum, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long)
            {
                milliseconds = milliseconds + 1000
                val seconds = (milliseconds / 1000) as Int % 60
                val minutes = (milliseconds / (1000 * 60) % 60)
                val hours = (milliseconds / (1000 * 60 * 60) % 24)

                timerAndResend?.text = ""+hours+":"+minutes+":"+seconds


//                var seconds = ""+millisUntilFinished % 60000 / 1000
//                var minutes = ""+millisUntilFinished / 60000
//
////                if (seconds.toInt() <= 9)
////                    seconds = "0"+seconds
////
////                if (minutes.toInt() <= 9)
////                    minutes = "0"+minutes
//
//
               // Log.e(TAG , "chronometer.getBase() "+chronometer.getBase())
//
//                val countUp: Long = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000

//                seconds = seconds + 1000
//
//                var sec = seconds % 60
//                var temp = seconds / 60
//                var mins = temp % 60
//                var hrs = temp / 60

//                timerAndResend?.text = ""+minutes+":"+seconds
//
//                var sss = ""
//                var mmm = ""
//                var hhh = ""
//
//                if(sec.toString().length == 1){
//                    sss = "0"+sec
//                }else{
//                    sss = sec.toString()
//                }
//
//                if(mins.toString().length == 1){
//                    mmm = "0"+mins
//                }else{
//                    mmm = mins.toString()
//                }
//
//                if(hrs.toString().length == 1){
//                    hhh = "0"+hrs
//                }else{
//                    hhh = hrs.toString()
//                }
//
//                timerAndResend?.text = ""+hhh+":"+mmm+":"+sss

//                Log.e(TAG , "chronometer.getBase() "+chronometer.getBase())
//
//                seconds = seconds + 1000
//                Log.e(TAG , "seconds() "+seconds)
//
//                var sss = ""+millisUntilFinished % 60000 / 1000


//                seconds = seconds + 1
//                Log.e(TAG, "seconds() " + seconds)

//                var sec = seconds % 60
//                var temp = seconds / 60
//                var mins = temp % 60
//                var hrs = temp / 60



            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
//                timerAndResend.setTextColor(Color.parseColor("#0b8793"))
//                timerAndResend?.text = ""+getString(R.string.resend)
//                timerAndResend?.isClickable = true
            }
        }.start()
    }


    private val processor = object : Detector.Processor<Barcode> {
        override fun release() {
        }
        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
            if (detections != null && detections.detectedItems.isNotEmpty()) {        val barcode = detections?.detectedItems
                if (barcode?.size() ?: 0 > 0) {
                    // show barcode content value
                    Toast.makeText(
                        this@xyz1,
                        barcode?.valueAt(0)?.displayValue ?: "",
                        Toast.LENGTH_SHORT
                    ).show()
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