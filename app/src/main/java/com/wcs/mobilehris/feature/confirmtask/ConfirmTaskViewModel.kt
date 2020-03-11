package com.wcs.mobilehris.feature.confirmtask

import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject

class ConfirmTaskViewModel (private val context : Context, private val confirmTaskInterface: ConfirmTaskInterface) : ViewModel(){
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
        isProgressConfirmTask.set(true)
        isHideCardView.set(true)
        Handler().postDelayed({
            stConfirmChargeCode.set(confirmTaskSplitChargeCode[0].trim() + " " + confirmTaskSplitChargeCode[1].trim())
            stConfirmCompName.set("PT ABCD")
            stConfirmCP.set("Michael Saputra")
            stConfirmTaskDateFrom.set("17/01/2020")
            stConfirmTaskDateFrom.set("19/01/2020")
            stConfirmTaskTimeFrom.set("08:00")
            stConfirmTaskTimeInto.set("17:00")
            stConfirmDescription.set("Buat Mobile Hris")
            //FIND PREFIX CHARGE CODE and decide type project substring first in intentChargeCode
            when(confirmTaskSplitChargeCode[0].substring(0, 1)){
                "F" -> stTypeTask = ConstantObject.vProjectTask
                "E" -> stTypeTask = ConstantObject.vSupportTask
                "A" -> stTypeTask = ConstantObject.vPreSalesTask
            }
            when(stTypeTask){
                ConstantObject.vProjectTask -> {
                    isHiddenPMTv.set(false)
                    isHiddenSolmanNoTv.set(true)
                    stConfirmPM.set("Edo Saputra")
                }
                ConstantObject.vSupportTask -> {
                    isHiddenPMTv.set(true)
                    isHiddenSolmanNoTv.set(false)
                    stConfirmSolmanNo.set("A001-001")
                }
                else -> {
                    isHiddenPMTv.set(true)
                    isHiddenSolmanNoTv.set(true)
                }
            }
            isProgressConfirmTask.set(false)
            isHideCardView.set(false)
        }, 2000)
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
}