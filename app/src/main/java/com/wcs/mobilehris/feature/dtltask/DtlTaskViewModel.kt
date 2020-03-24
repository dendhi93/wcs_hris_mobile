package com.wcs.mobilehris.feature.dtltask

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.util.ConstantObject
import org.json.JSONArray
import org.json.JSONObject

class DtlTaskViewModel(private val context : Context,
                       private val dtlTaskInterface : DtlTaskInterface,
                       private val apiRepo: ApiRepo) :ViewModel(){
    val stChargeCode = ObservableField("")
    val stCompanyName = ObservableField("")
    val stDtlTaskDateFrom = ObservableField("")
    val stDtlTaskDateInto = ObservableField("")
    val stDtlTaskTimeFrom = ObservableField("")
    val stDtlTaskTimeInto = ObservableField("")
    val stDtlContactPerson = ObservableField("")
    val stDtlDescription = ObservableField("")
    val stDtlSolmanNo = ObservableField("")
    val stDtlProjectManager = ObservableField("")
    val isProgressDtl = ObservableField(false)
    val isHiddenRv = ObservableField(false)
    val isHiddenSolmanTv = ObservableField(false)
    val isHiddenProjectManager = ObservableField(false)
    private var stIntentTaskId : String = ""
    private var stIntentTypeTask : String = ""

    fun initDataDtl(taskId : String, chargeCode : String){
        stIntentTaskId = taskId.trim()
        val dtlTaskSplitChargeCode = chargeCode.trim().split("|")
        when(dtlTaskSplitChargeCode[0].trim().substring(0, 1)){
            "F" -> stIntentTypeTask = ConstantObject.vProjectTask
            "E" -> stIntentTypeTask = ConstantObject.vSupportTask
            else -> ConstantObject.vPreSalesTask
        }
        stChargeCode.set(dtlTaskSplitChargeCode[0].trim() +" " +dtlTaskSplitChargeCode[1].trim())
        when {
            !ConnectionObject.isNetworkAvailable(context) -> dtlTaskInterface.onAlertDtlTask(context.getString(R.string.alert_no_connection), ConstantObject.vAlertDialogNoConnection,
                    DetailTaskActivity.ALERT_DTL_TASK_NO_CONNECTION)
            else -> AsyncDtlTask().execute()
        }
    }

    private fun initDtlTask(){
        isProgressDtl.set(true)
        isHiddenRv.set(true)
        apiRepo.getHeaderActivity(stIntentTaskId, context, object  :
            ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseHeaderActivity = it.getString(ConstantObject.vResponseData)
                    val jObjHeaderAct = JSONObject(responseHeaderActivity)
                    stCompanyName.set(jObjHeaderAct.getString("CUSTOMER_NAME"))
                    stDtlTaskTimeFrom.set(jObjHeaderAct.getString("TIME_FROM"))
                    stDtlTaskTimeInto.set(jObjHeaderAct.getString("TIME_TO"))
                    stDtlContactPerson.set(jObjHeaderAct.getString("PICCUSTOMER"))
                    stDtlDescription.set(jObjHeaderAct.getString("DESCRIPTION"))
                    stDtlTaskDateFrom.set(jObjHeaderAct.getString("DATE_FROM").split("T")[0].trim())
                    stDtlTaskDateInto.set(jObjHeaderAct.getString("DATE_TO").split("T")[0].trim())
                    when(stIntentTypeTask.trim()) {
                        ConstantObject.vSupportTask -> {
                            isHiddenProjectManager.set(true)
                            stDtlSolmanNo.set(jObjHeaderAct.getString("SOLMAN_NUMBER"))
                        }
                        ConstantObject.vProjectTask ->{
                            isHiddenSolmanTv.set(true)
                            stDtlProjectManager.set(jObjHeaderAct.getString("PM_NAME"))
                        }
                        else -> {
                            isHiddenSolmanTv.set(true)
                            isHiddenProjectManager.set(true)
                        }
                    }
                }
            }

            override fun onDataError(error: String?) {
                dtlTaskInterface.onErrorMessage(" err Header "
                        +error.toString().trim(), ConstantObject.vToastError)
                isProgressDtl.set(false)
            }
        })
    }

    private fun initListTeam(){
        val listFriend = mutableListOf<FriendModel>()
        var friendModel : FriendModel
        apiRepo.getDetailActivity(stIntentTaskId,
            context, object  : ApiRepo.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        val responsePlanData = it.getString(ConstantObject.vResponseData)
                        val jArrayDataFriend = JSONArray(responsePlanData)
                        for (i in 0 until jArrayDataFriend.length()) {
                            val jObjFriend = jArrayDataFriend.getJSONObject(i)
                            when(jObjFriend.getString("AVAILABLE")){
                                "Y" -> {
                                    friendModel = FriendModel(jObjFriend.getString("NIK"),
                                        jObjFriend.getString("EMPLOYEE_NAME"),
                                        "Free", false)
                                    listFriend.add(friendModel)
                                }
                                else -> {
                                    friendModel = FriendModel(jObjFriend.getString("NIK"),
                                        jObjFriend.getString("EMPLOYEE_NAME"),
                                        "Conflict", true)
                                    listFriend.add(friendModel)
                                }
                            }
                        }
                        when{
                            listFriend.isNotEmpty() -> dtlTaskInterface.loadTeam(listFriend)
                        }
                    }
                }

                override fun onDataError(error: String?) {
                    dtlTaskInterface.onErrorMessage(" err dtl "
                            +error.toString().trim(), ConstantObject.vToastError)
                }

            })
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncDtlTask: AsyncTask<Void, Void, String>(){

        override fun doInBackground(vararg params: Void?): String {
            return "OK"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            initDtlTask()
            initListTeam()
        }
    }

    fun onBackPressMenu(){
        val intent = Intent(context, MenuActivity::class.java)
        intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_ACTIVITY)
        context.startActivity(intent)
    }
}