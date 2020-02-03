@file:Suppress("DEPRECATION")

package com.wcs.mobilehris.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log


object ConnectionObject {
    /**
     * check respond http response
     */
    fun checkHttpCode(httpCode: String): Boolean = httpCode == "200"

    fun checkHttpBadRequest(httpCode: Int): Boolean = httpCode == 401

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
                    Log.i("update_statut", "Network is available : true")
                    return true
                }
            } catch (e: Exception) { Log.i("update_statut", "" + e.message) }
        }
        return false
    }
}