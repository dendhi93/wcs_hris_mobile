@file:Suppress("DEPRECATION")

package com.wcs.mobilehris.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


object ConnectionObject {
    const val timeout = 60 //second
    /**
     * check respond http response
     */
    fun checkHttpCode(httpCode: String): Boolean = httpCode == "200"
//    fun checkHttpBadRequest(httpCode: Int): Boolean = httpCode == 400

    /**
     * check if network available either wi-fi or 3g data
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->  return true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                }
            }
        } else {
            try {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    Log.i("update_status", "Network is available : true")
                    return true
                }
            } catch (e: Exception) { Log.i("update_statut", "" + e.message) }
        }
        return false
    }

    fun okHttpClient(retry: Boolean, duration: Int): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(duration.toLong(), TimeUnit.SECONDS)
            .readTimeout(duration.toLong(), TimeUnit.SECONDS)
            .writeTimeout(duration.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(retry)
            builder.addNetworkInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))

        return builder.build()
    }
}