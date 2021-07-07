package com.padedatingapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.propertyonthespot.utils.Utility
import java.io.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class abc : AppCompatActivity() {

    companion object{
        var TAG = "abc"
    }

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1999


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .build()
        StrictMode.setThreadPolicy(policy)


        Handler().postDelayed({
            if (Utility.checkAndRequestPermissions(this@abc, REQUEST_ID_MULTIPLE_PERMISSIONS)) {
                getLogin()
            } else {
            }
        }, 1000)

    }




    @Suppress("UNCHECKED_CAST")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                if (Utility.checkAdditionPermissionsCheck(this@abc,
                        permissions as Array<String>, grantResults, REQUEST_ID_MULTIPLE_PERMISSIONS)) {
                    getLogin()
                }
            }
        }
    }


    private fun getLogin() {
          DownloadFile(this@abc, "subject").execute("http://13.126.22.0/saad/app/uploads/invoice/pdf/60b6523a1726a.pdf")
    }

    private class DownloadFile internal constructor(
        var context: Context,
        var subject: String
    ) :
        AsyncTask<String?, String?, String>() {
        private var progressDialog: ProgressDialog? = null
        private var fileName: String? = null
        private var folder: String? = null
        private val isDownloaded = false

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(context)
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }

        /**
         * Downloading file in background thread
         */
        protected override fun doInBackground(vararg params: String?): String? {
            var count: Int
            try {
                val url = URL(params[0])
                val connection = url.openConnection()
                connection.connect()

                val lengthOfFile = connection.contentLength

                val input: InputStream = BufferedInputStream(url.openStream(), 8192)
                val timestamp =
                    SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date())


                fileName = params.get(0)?.substring(params.get(0)!!.lastIndexOf('/').plus(1))

                Log.e(TAG, "folderCC "+fileName)

                fileName = timestamp + "_" + fileName

                Log.e(TAG, "folderBB "+fileName)

                folder = Environment.getExternalStorageDirectory()
                    .toString() + File.separator + "SIR/"

                Log.e(TAG, "folderAA "+folder)

                val directory = File(folder)

                if (!directory.exists()) {
                    directory.mkdirs()
                }

                val newFileName = "CreditNote.pdf"
                // Output stream to write file
                val output: OutputStream = FileOutputStream(folder + newFileName)
                val data = ByteArray(1024)
                var total: Long = 0
                while (input.read(data).also { count = it } != -1) {
                    total += count.toLong()
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (total * 100 / lengthOfFile).toInt())
                    Log.e(
                        TAG,
                        "Progress: " + (total * 100 / lengthOfFile).toInt()
                    )

                    // writing data to file
                    output.write(data, 0, count)
                }

                // flushing output
                output.flush()

                // closing streams
                output.close()
                input.close()
                return "" + folder + newFileName
            } catch (e: Exception) {
                Log.e("Error: ", e.message!!)
            }
            return "Something went wrong"
        }


        protected override fun onProgressUpdate(vararg values: String?) {
            progressDialog!!.progress = values.get(0)?.toInt()!!
        }

        override fun onPostExecute(message: String) {
            progressDialog!!.dismiss()
            Log.e(
                TAG,
                "onPostExecuteFile $message"
            )


            val intentShareFile = Intent(Intent.ACTION_SEND)
            val fileWithinMyDir = File(message)
            val photoURI = FileProvider.getUriForFile(
                context,
                "com.padedatingapp.provider",
                fileWithinMyDir
            )
            if (fileWithinMyDir.exists()) {
                intentShareFile.type = "image/*"
                val outputFileUri = Uri.fromFile(fileWithinMyDir)
                intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI)
                intentShareFile.putExtra(
                    Intent.EXTRA_SUBJECT,
                    subject
                )

//                if (isAppAvailable(context, "com.google.android.gm")){
//                    intentShareFile.setPackage("com.google.android.gm");
//                }
//                context.startActivity(intentShareFile);
                context.startActivity(
                    Intent.createChooser(
                        intentShareFile, "Share")
                    )

            }
        }

    }


}