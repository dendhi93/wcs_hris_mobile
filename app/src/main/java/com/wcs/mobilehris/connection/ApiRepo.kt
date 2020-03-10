package com.wcs.mobilehris.connection

import android.content.Context
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndJSONArrayRequestListener
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener
import com.wcs.mobilehris.BuildConfig
import okhttp3.Response
import org.json.JSONArray
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

    fun getUpdateTableMaster(tableName : String, context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        Log.d("###","url getUpdateTableMaster "+BuildConfig.HRIS_URL+"getlatestmaster/"+tableName.trim())
        AndroidNetworking.get(BuildConfig.HRIS_URL+"getlatestmaster/"+tableName.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.LOW)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let {
                        when{
                            ConnectionObject.checkHttpCode(it.code().toString()) -> callback.onDataLoaded(response)
                            ConnectionObject.checkHttpNotFound(it.code()) -> callback.onDataError(response.toString())
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","" +anError?.message)
                            callback.onDataError(anError?.errorBody.toString())
                        }
                        else -> {
                            Log.d("###_2",""+anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun getDataMaster(typeMaster : String, context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        Log.d("###","url getDataMaster "+BuildConfig.HRIS_URL+typeMaster)
        AndroidNetworking.get(BuildConfig.HRIS_URL+typeMaster)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.LOW)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let {
                        when{
                            ConnectionObject.checkHttpCode(it.code().toString()) -> callback.onDataLoaded(response)
                            ConnectionObject.checkHttpNotFound(it.code()) -> callback.onDataError(response.toString())
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","" +anError?.message)
                            callback.onDataError(anError?.errorBody.toString())
                        }
                        else -> {
                            Log.d("###_2",""+anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
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