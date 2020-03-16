package com.wcs.mobilehris.feature.confirmtask

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.util.ConstantObject
import org.json.JSONObject

class ConfirmTaskViewModel (private val context : Context,
                            private val confirmTaskInterface: ConfirmTaskInterface,
                            private val apiRepo: ApiRepo) : ViewModel(){
    val isProgressConfirmTask = ObservableField(false)
    val isHideCardView = ObservableField(false)
    val stConfirmChargeCode = ObservableField("")
    val stConfirmCompName = ObservableField("")
    val stConfirmTaskDateFrom = ObservableField("")
    val stConfirmTaskDateInto = ObservableField("")
    val stConfirmTaskTimeFrom = ObservableField("")
    val stConfirmTaskTimeInto = ObservableField("")
    val stConfirmCP = ObservableField("")
    val stConfirmDescription = ObservableField("")
    val stConfirmSolmanNo = ObservableField("")
    val stConfirmPM = ObservableField("")
    val stConfirmActHour = ObservableField("")
    val stConfirmActDescription = ObservableField("")
    val stButtonName = ObservableField("")
    val isHiddenSolmanNoTv = ObservableField(false)
    val isHiddenPMTv = ObservableField(false)
    private var stTypeTask : String? = ""
    private var stIntentTaskId : String = ""

    fun onClickConfirm(){
        when{
            !ConnectionObject.isNetworkAvailable(context) ->
                confirmTaskInterface.onAlertConfirmTask(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, ConfirmTaskActivity.ALERT_CONFIRM_TASK_NO_CONNECTION)
            !isSubmitConfirm() -> confirmTaskInterface.onAlertMessage(context.getString(R.string.fill_in_the_blank), ConstantObject.vSnackBarWithButton)
            else -> confirmTaskInterface.onAlertConfirmTask(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, ConfirmTaskActivity.ALERT_CONFIRM_TASK_CONFIRMATION)
        }
    }

    fun onLoadConfirmData(intentTaskId : String, intentChargeCode : String){
        stIntentTaskId = intentTaskId
        val confirmTaskSplitChargeCode = intentChargeCode.trim().split("|")
        stConfirmChargeCode.set(confirmTaskSplitChargeCode[0].trim() + " " + confirmTaskSplitChargeCode[1].trim())
        //FIND PREFIX CHARGE CODE and decide type project substring first in intentChargeCode
        when(confirmTaskSplitChargeCode[0].substring(0, 1)){
            "F" -> stTypeTask = ConstantObject.vProjectTask
            "E" -> stTypeTask = ConstantObject.vSupportTask
            "A" -> stTypeTask = ConstantObject.vPreSalesTask
        }

        isProgressConfirmTask.set(true)
        isHideCardView.set(true)
        apiRepo.getHeaderActivity(intentTaskId, context,  object  : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseConfimActivity = it.getString(ConstantObject.vResponseData)
                    val jObjConfirmAct = JSONObject(responseConfimActivity)
                    stConfirmCompName.set(jObjConfirmAct.getString("CUSTOMER_NAME"))
                    stConfirmTaskTimeFrom.set(jObjConfirmAct.getString("TIME_FROM"))
                    stConfirmTaskTimeInto.set(jObjConfirmAct.getString("TIME_TO"))
                    stConfirmCP.set(jObjConfirmAct.getString("PICCUSTOMER"))
                    stConfirmDescription.set(jObjConfirmAct.getString("DESCRIPTION"))
                    val dtlDateFrom = jObjConfirmAct.getString("DATE_FROM").split("T")
                    stConfirmTaskDateFrom.set(dtlDateFrom[0].trim())
                    val dtlDateInto = jObjConfirmAct.getString("DATE_TO").split("T")
                    stConfirmTaskDateInto.set(dtlDateInto[0].trim())

                    when(stTypeTask){
                        ConstantObject.vProjectTask -> {
                            isHiddenPMTv.set(false)
                            isHiddenSolmanNoTv.set(true)
                            stConfirmPM.set(jObjConfirmAct.getString("PM_NAME"))
                        }
                        ConstantObject.vSupportTask -> {
                            isHiddenPMTv.set(true)
                            isHiddenSolmanNoTv.set(false)
                            stConfirmSolmanNo.set(jObjConfirmAct.getString("SOLMAN_NUMBER"))
                        }
                        else -> {
                            isHiddenPMTv.set(true)
                            isHiddenSolmanNoTv.set(true)
                        }
                    }
                    isProgressConfirmTask.set(false)
                    isHideCardView.set(false)
                }
            }

            override fun onDataError(error: String?) {
                confirmTaskInterface.onAlertMessage(" err Header "
                        +error.toString().trim(), ConstantObject.vToastError)
                isProgressConfirmTask.set(false)
            }

        })
    }

    private fun isSubmitConfirm() : Boolean {
        if(stTypeTask == ConstantObject.vPreSalesTask ||
            stTypeTask == ConstantObject.vProspectTask ||
            stTypeTask == ConstantObject.vProjectTask){
                if(stConfirmActHour.get() == "" || stConfirmActDescription.get() == "") return false
        }else {
            if(stConfirmActHour.get() == "") return false
        }
        return true
    }

    fun submitConfirmTask(){
        isProgressConfirmTask.set(true)
        confirmTaskInterface.onSuccessConfirm()
    }

    fun onBackConfirmTask(){
        val intent = Intent(context, MenuActivity::class.java)
        intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_ACTIVITY)
        context.startActivity(intent)
    }
}