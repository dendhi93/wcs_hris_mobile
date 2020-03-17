package com.wcs.mobilehris.connection

import android.content.Context
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener
import com.wcs.mobilehris.BuildConfig
import com.wcs.mobilehris.util.ConstantObject
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
                        when{ConnectionObject.checkSuccessHttpCode(okHttpResponse.code().toString()) -> callback.onDataLoaded(response) }
                    }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","Login " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString("Message"))
                        }
                        else -> {
                            Log.d("###_2","Login " +anError.errorBody.toString())
                            callback.onDataError(anError.message.toString())
                        }
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
                            ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response)
                            ConnectionObject.checkHttpNotFound(it.code()) -> callback.onDataError(response.toString())
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","date Master " +anError?.message)
                            callback.onDataError(anError?.errorBody.toString())
                        }
                        else -> {
                            Log.d("###_2","date Master "+anError.message.toString())
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
                            ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response)
                            ConnectionObject.checkHttpNotFound(it.code()) -> callback.onDataError(response.toString())
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","masterData " +anError?.message)
                            callback.onDataError(anError?.errorBody.toString())
                        }
                        else -> {
                            Log.d("###_2","masterData "+anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun getDataActivity(unUser : String,selectedDate : String,activityFrom : String,context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        val urlActivity = when(activityFrom.trim()){
            ConstantObject.vCompletedTask -> BuildConfig.HRIS_URL+"getcompletedactivitybynikandbydate/"+unUser.trim()+"/"+selectedDate.trim()
            else -> BuildConfig.HRIS_URL+"getactivitybydate/"+selectedDate.trim()+"/"+unUser.trim()
        }

        Log.d("###", "url getDataActivity ${urlActivity.trim()}")
        AndroidNetworking.get(urlActivity.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let {
                        when{
                            ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response)
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","activity " +anError?.message)
                            callback.onDataError(anError?.errorBody.toString())
                        }
                        else -> {
                            Log.d("###","activity "+anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun getHeaderActivity(idActivity : String,context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        val urlHeaderActivity = BuildConfig.HRIS_URL+"getactivityheaderbyid/"+idActivity.trim()
        Log.d("###", "url getHeaderActivity ${urlHeaderActivity.trim()}")
        AndroidNetworking.get(urlHeaderActivity.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let {
                       when{
                           ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response)
                       }
                   }
                }

                override fun onError(anError: ANError?) {
                    Log.d("###","header activity "+anError?.message.toString())
                    callback.onDataError(anError?.message.toString())
                }
            })
    }

    fun getDetailActivity(idDTlActivity : String,context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        val urlDtlActivity = BuildConfig.HRIS_URL+"getactivitydetailbyid/"+idDTlActivity.trim()
        Log.d("###", "url getDtlActivity ${urlDtlActivity.trim()}")
        AndroidNetworking.get(urlDtlActivity.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let {
                        when{
                            ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response)
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("###","dtl activity "+anError?.message.toString())
                    callback.onDataError(anError?.message.toString())
                }
            })
    }

    //interface response from server
    interface ApiCallback<T> {
        fun onDataLoaded(data: T?)
        fun onDataError(error: String?)
    }
}