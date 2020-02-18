package com.wcs.mobilehris.feature.confirmtask

import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject

class ConfirmTaskViewModel (private val context : Context, private val confirmTaskInterface: ConfirmTaskInterface) : ViewModel(){
    val isProgressConfirmTask = ObservableField<Boolean>(false)
    val isHideCardView = ObservableField<Boolean>(false)
    val isOnSiteConfirmTask = ObservableField<Boolean>(false)
    val stConfirmChargeCode = ObservableField<String>("")
    val stConfirmCompName = ObservableField<String>("")
    val stConfirmTaskDate = ObservableField<String>("")
    val stConfirmTaskTimeFrom = ObservableField<String>("")
    val stConfirmTaskTimeInto = ObservableField<String>("")
    val stConfirmCP = ObservableField<String>("")
    val stConfirmDescription = ObservableField<String>("")
    val stConfirmSolmanNo = ObservableField<String>("")
    val stConfirmPM = ObservableField<String>("")
    val stConfirmActHour = ObservableField<String>("")
    val stConfirmActDescription = ObservableField<String>("")
    val stButtonName = ObservableField<String>("")
    val isHiddenSolmanNoTv = ObservableField<Boolean>(false)
    val isHiddenPMTv = ObservableField<Boolean>(false)
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
        isProgressConfirmTask.set(true)
        isHideCardView.set(true)
        Handler().postDelayed({
            stConfirmChargeCode.set(intentChargeCode.trim())
            stConfirmCompName.set("PT ABCD")
            stConfirmCP.set("Michael Saputra")
            stConfirmTaskDate.set("17/01/2020")
            stConfirmTaskTimeFrom.set("08:00")
            stConfirmTaskTimeInto.set("17:00")
            stConfirmDescription.set("Buat Mobile Hris")
            //FIND PREFIX CHARGE CODE and decide type project substring first in intentChargeCode
            when(intentChargeCode.substring(0, 1)){
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