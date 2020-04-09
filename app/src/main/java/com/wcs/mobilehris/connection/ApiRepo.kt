package com.wcs.mobilehris.connection

import android.content.Context
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener
import com.wcs.mobilehris.BuildConfig
import com.wcs.mobilehris.feature.dtlreqtravel.DtlRequestTravelActivity
import com.wcs.mobilehris.util.ConstantObject
import okhttp3.Response
import org.json.JSONObject

//class untuk ambil data ke server
class ApiRepo {
    fun getLogin(jObjLogin : JSONObject,context : Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        Log.d("###","url Login "+BuildConfig.HRIS_URL+"mobileauthenticates")
        AndroidNetworking.post(BuildConfig.HRIS_URL+"mobileauthenticates")
            .addJSONObjectBody(jObjLogin)
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
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","Login " +anError.message.toString())
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
                            Log.d("###","Login " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","Login " +anError.message.toString())
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
                            Log.d("###","data master " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","data master " +anError.message.toString())
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
                        when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) }
                    }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","activity " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","activity " +anError.message.toString())
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
                       when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) }
                   }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","header activity " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","header activity " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
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
                        when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) }
                    }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","detail activity " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","detail activity " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun getTravelListData(unUser :String, fromTravelType : String, context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        val urlTravel = when(fromTravelType.trim()){
            ConstantObject.extra_fromIntentApproval -> BuildConfig.HRIS_URL+"gettravelrequestapprovalall/"+unUser.trim()
            else -> BuildConfig.HRIS_URL+"gettravelrequestheaderbynik/"+unUser.trim()
        }
        Log.d("###", "url getTravel Data ${urlTravel.trim()}")
        AndroidNetworking.get(urlTravel.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let {
                        when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) }
                    }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","travel list data " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","travel list data" +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun getCountAllApproval(unUser :String,context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        Log.d("###","count approval " +BuildConfig.HRIS_URL+"getcountallapproval/"+unUser.trim())
        AndroidNetworking.get(BuildConfig.HRIS_URL+"getcountallapproval/"+unUser.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let {
                        when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) }
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("###","err count approval "+anError?.message.toString())
                    callback.onDataError(anError?.message.toString())
                }
            })
    }

    fun getLeaveList(unUser : String ,leaveFrom : String, context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        val urlLeave : String = when(leaveFrom){
            ConstantObject.extra_fromIntentRequest -> BuildConfig.HRIS_URL+"getallleaverequest/"+unUser.trim()
            else -> BuildConfig.HRIS_URL+"getleaverequestapprovalall/"+unUser.trim()
        }
        Log.d("###", "url leave $urlLeave")
        AndroidNetworking.get(urlLeave.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let {
                        when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) }
                    }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","leave list " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","leave list " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun searchDataTeam(fromMenu : String, teamName : String, selectedDateFrom : String, selectedDateInto : String, context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)

        val teamUrl = when(fromMenu){
            ConstantObject.extra_fromIntentTeam ->{
                     BuildConfig.HRIS_URL+"getteamemployeeallbyemployename/"+
                        selectedDateFrom.trim()+"/"+
                        selectedDateInto.trim()+"/08:30/17:30/"+
                        teamName.trim()
            }
            else ->{
                BuildConfig.HRIS_URL+"getconflictedplan/"+
                        teamName.trim()+"/"+
                        selectedDateFrom.trim()+"/"+
                        selectedDateInto.trim()+"/08:30/17:30"
            }
        }

        Log.d("###", "url searchDataTeam $teamUrl")
        AndroidNetworking.get(teamUrl.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","search team " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","search team " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun getHeaderTravel(urlTravel : String, context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        Log.d("###", "url header travel $urlTravel")
        AndroidNetworking.get(urlTravel)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","travel header " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","travel header " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun getDataDashboard(unUser : String, context: Context,  callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        Log.d("###", "url dashboard " +BuildConfig.HRIS_URL+"getmobiledashboard/"+unUser.trim())
        AndroidNetworking.get(BuildConfig.HRIS_URL+"getmobiledashboard/"+unUser.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.HIGH)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","dashboard " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","dashboard " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun getLogout(unUser : String, context: Context,  callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        Log.d("###", "url Logout " +BuildConfig.HRIS_URL+"logout/"+unUser.trim())
        AndroidNetworking.get(BuildConfig.HRIS_URL+"logout/"+unUser.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.HIGH)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","Logout " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","Logout " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun postReqTravelReq(jObjReqTravel : JSONObject, context: Context,  callback: ApiCallback<JSONObject>){
        Log.d("###", "json travel $jObjReqTravel")
        AndroidNetworking.initialize(context)
        Log.d("###", "url insert leave " +BuildConfig.HRIS_URL+"inserttravelrequest")
        AndroidNetworking.post(BuildConfig.HRIS_URL+"inserttravelrequest")
            .addJSONObjectBody(jObjReqTravel)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","insert travel " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","insert travel " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun validateToken(jObjToken : JSONObject, context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        Log.d("###", "url validate token " +BuildConfig.HRIS_URL+"checktoken")
        AndroidNetworking.post(BuildConfig.HRIS_URL+"checktoken")
            .addJSONObjectBody(jObjToken)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.HIGH)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","validate Token " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","validate Token " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun postApproveTravel(docNumber : String, reasonReject : String, travelReqFrom : String,context: Context,  callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        val urlApprovalTravel = when(travelReqFrom){
            DtlRequestTravelActivity.approvalAccept -> BuildConfig.HRIS_URL+"travelrequestapprove/"+docNumber.trim()
            else -> BuildConfig.HRIS_URL+"travelrequestreject/"+docNumber.trim()+"/"+reasonReject.trim()
        }
        Log.d("###", "url accept travel $urlApprovalTravel")
        AndroidNetworking.post(urlApprovalTravel)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","accept travel " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","accept travel " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun deleteTravelReq(idTravel : String, context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        Log.d("###", "url delete travel" +BuildConfig.HRIS_URL+"travelrequestdelete/"+idTravel.trim())
        AndroidNetworking.post(BuildConfig.HRIS_URL+"travelrequestdelete/"+idTravel)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","delete travel " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","delete travel " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun insertActivity(data: JSONObject, context: Context, callback: ApiCallback<JSONObject>) {
        AndroidNetworking.initialize(context)

        AndroidNetworking.post(BuildConfig.HRIS_URL + "insertactivity")
            .addJSONObjectBody(data)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    Log.d("###","err insert activity "+anError?.message.toString())
                    callback.onDataError(anError?.errorDetail.toString())
                }

            })
    }

    fun updateActivity(data: JSONObject, context: Context, callback: ApiCallback<JSONObject>) {
        AndroidNetworking.initialize(context)

        AndroidNetworking.post(BuildConfig.HRIS_URL + "updateactivitydetailbymember")
            .addJSONObjectBody(data)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    Log.d("###","err update activity "+anError?.message.toString())
                    callback.onDataError(anError?.message.toString())
                }

            })
    }

    fun deleteActivity(activityHeaderId : String, nik : String, context : Context, callback: ApiCallback<JSONObject>) {
        AndroidNetworking.initialize(context)

        AndroidNetworking.post(BuildConfig.HRIS_URL + "deleteactivitybyheaderid/" + activityHeaderId.trim() + "/" + nik)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    Log.d("###","err delete or reject activity "+anError?.errorDetail.toString())
                    Log.d("###","err delete or reject activity "+anError?.errorBody.toString())
                    callback.onDataError(anError?.message.toString())
                }

            })
    }

    fun getActivityApprovalAll(userId: String, context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)

        val url = BuildConfig.HRIS_URL+"getactivityapprovalall/"+userId.trim()
        AndroidNetworking.get(url)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let {
                        when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) }
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("###","err leave "+anError?.message.toString())
                    callback.onDataError(anError?.message.toString())
                }
            })
    }

    fun approveActivityDetail(docNo : String, context : Context, callback: ApiCallback<JSONObject>) {
        AndroidNetworking.initialize(context)

        AndroidNetworking.post(BuildConfig.HRIS_URL + "acitvityapprove/" + docNo.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    Log.d("###","err activityApprove Detail "+anError?.errorDetail.toString())
                    Log.d("###","err activityApprove Body "+anError?.errorBody.toString())
                    callback.onDataError(anError?.message.toString())
                }

            })
    }

    fun rejectActivityDetail(docNo : String, reason : String, context: Context, callback: ApiCallback<JSONObject>) {
        AndroidNetworking.initialize(context)

        AndroidNetworking.post(BuildConfig.HRIS_URL + "activityreject/" + docNo.trim() + "/" + reason)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    Log.d("###","err activityReject Detail "+anError?.errorDetail.toString())
                    Log.d("###","err activityReject Body "+anError?.errorBody.toString())
                    callback.onDataError(anError?.message.toString())
                }

            })
    }

    fun getDtlLeave(leaveId : String, context: Context, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        Log.d("###", "url dtl leave " +BuildConfig.HRIS_URL+"getleaverequestbyid/"+leaveId.trim())
        AndroidNetworking.get(BuildConfig.HRIS_URL+"getleaverequestbyid/"+leaveId.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.HIGH)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","detail leave " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","detail leave " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun insertLeave(unUser : String, context: Context, jObjLeave : JSONObject, callback: ApiCallback<JSONObject>){
        AndroidNetworking.initialize(context)
        Log.d("###", "url insert leave " +BuildConfig.HRIS_URL+"createleaverequest/"+unUser.trim())
        AndroidNetworking.post(BuildConfig.HRIS_URL+"createleaverequest/"+unUser.trim())
            .addJSONObjectBody(jObjLeave)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    when{
                        anError?.errorCode != 0 -> {
                            Log.d("###","detail leave " +anError?.errorBody.toString())
                            val errObj = JSONObject(anError?.errorBody.toString())
                            callback.onDataError(errObj.getString(ConstantObject.vResponseMessage))
                        }
                        else -> {
                            Log.d("###_2","detail leave " +anError.message.toString())
                            callback.onDataError(anError.message.toString())
                        }
                    }
                }
            })
    }

    fun deleteLeave(userId : String, idLeave : String, context : Context, callback: ApiCallback<JSONObject>) {
        AndroidNetworking.initialize(context)

        AndroidNetworking.post(BuildConfig.HRIS_URL + "deleteleaverequest/" + userId.trim() + "/" + idLeave)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    Log.d("###","err delete Leave "+anError?.errorDetail.toString())
                    Log.d("###","err delete or Leave "+anError?.errorBody.toString())
                    callback.onDataError(anError?.message.toString())
                }

            })
    }

    fun approveLeaveRequest(docNo : String, context : Context, callback: ApiCallback<JSONObject>) {
        AndroidNetworking.initialize(context)

        AndroidNetworking.post(BuildConfig.HRIS_URL + "leaverequestapprove/" + docNo.trim())
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    Log.d("###","err delete Leave "+anError?.errorDetail.toString())
                    Log.d("###","err delete or Leave "+anError?.errorBody.toString())
                    callback.onDataError(anError?.message.toString())
                }

            })
    }

    fun rejectLeaveRequest(docNo : String, reason : String, context: Context, callback: ApiCallback<JSONObject>) {
        AndroidNetworking.initialize(context)

        AndroidNetworking.post(BuildConfig.HRIS_URL + "leaverequestreject/" + docNo.trim() + "/" + reason)
            .setOkHttpClient(ConnectionObject.okHttpClient(false, ConnectionObject.timeout))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: JSONObject?) {
                    okHttpResponse?.let { when{ConnectionObject.checkSuccessHttpCode(it.code().toString()) -> callback.onDataLoaded(response) } }
                }

                override fun onError(anError: ANError?) {
                    Log.d("###","err rejectLeaveRequest Detail "+anError?.errorDetail.toString())
                    Log.d("###","err rejectLeaveRequest Body "+anError?.errorBody.toString())
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