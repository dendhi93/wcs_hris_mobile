package com.wcs.mobilehris.connection

import android.content.Context
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener
import com.wcs.mobilehris.BuildConfig
import okhttp3.Response
import org.json.JSONObject

//class untuk ambil data ke server
class ApiRepo {

    fun getLogin(userName : String, passUn : String, context : Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        Log.d("###","url Login "+BuildConfig.HRIS_URL+"authenticates/"+userName.trim()+"/"+passUn.trim())

        AndroidNetworking.get(BuildConfig.HRIS_URL+"authenticates/"+userName.trim()+"/"+passUn.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let {
                        when{ConnectionObject.checkHttpCode(okHttpResponse.code().toString()) -> callback.onDataLoaded(response) }
                    }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("errMessage","" +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString("Message"))
                        }
                        else -> callback.onDataError(anError.errorBody.toString())
                    }
                }
            })
    }

    //interface response from server
    interface ApiCallback<T> {
        fun onDataLoaded(data: T?)
        fun onDataError(error: String?)
    }
}