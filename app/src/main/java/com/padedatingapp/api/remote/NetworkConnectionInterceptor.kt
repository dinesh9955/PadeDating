package com.padedatingapp.api.remote

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import org.json.JSONObject
import java.io.IOException
import kotlin.Throws

@Suppress("DEPRECATION")
class NetworkConnectionInterceptor(private val mContext: Context) :
    Interceptor {

    @SuppressLint("DefaultLocale")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected) {
            throw NoConnectivityException()
            // Throwing our custom exception 'NoConnectivityException'
        } else {
            val t1 = System.nanoTime()
            val request: Request = chain.request()
            logger.info(
                String.format(
                    "Sending request %s on %s%n%s",
                    request.url,
                    chain.connection(),
                    request.headers + "REQUEST = ${request.body}"
                )
            )
            val response: Response = chain.proceed(request)
            val responseBody: ResponseBody? = response.body
            val responseBodyString = response.body!!.string()
            val newResponse = response.newBuilder()
                .body(responseBodyString.toByteArray().toResponseBody(responseBody!!.contentType()))
                .build()
            Log.d("HTTP_RESPONSE_CODE", "#### ${newResponse.code}")
            // todo deal with the issues the way you need to
            if (newResponse.code == 500) {
                return response
            }
            val t2 = System.nanoTime()
            val responseObj = JSONObject(responseBodyString)

            val maxLogSize = 1000
            val stringLength = responseObj.toString(4).length
            for (i in 0..stringLength / maxLogSize) {
                val start = i * maxLogSize
                var end = (i + 1) * maxLogSize
                end = if (end > stringLength) stringLength else end
                Log.v("RESPONSE --->", responseObj.toString(4).substring(start, end))
            }
            return newResponse
        }
    }

    private val isConnected: Boolean
        get() =
            (mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getNetworkCapabilities(activeNetwork)?.run {
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    } ?: false
                } else {
                    val netInfo: NetworkInfo? = activeNetworkInfo
                    if (netInfo != null) {
                        return netInfo.isConnected
                    } else {
                        false
                    }
                }
            }
}