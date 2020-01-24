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
    val isOnSiteConfirmTask = ObservableField<Boolean>(false)
    val stConfirmChargeCode = ObservableField<String>("")
    val stConfirmCompName = ObservableField<String>("")
    val stConfirmCompAddress = ObservableField<String>("")
    val stConfirmTaskDate = ObservableField<String>("")
    val stConfirmTaskTimeFrom = ObservableField<String>("")
    val stConfirmTaskTimeInto = ObservableField<String>("")
    val stConfirmCP = ObservableField<String>("")
    val stConfirmDescription = ObservableField<String>("")
    val stConfirmSolmanNo = ObservableField<String>("")
    val stConfirmPM = ObservableField<String>("")
    val stConfirmActHour = ObservableField<String>("")
    val stConfirmActDescription = ObservableField<String>("")
    val isHiddenSolmanNoTv = ObservableField<Boolean>(false)
    val isHiddenPMTv = ObservableField<Boolean>(false)
    private var stTypeTask : String? = ""
    private var stIntentTaskId : String = ""

    fun onClickConfirm(){
        when{
            !ConnectionObject.isNetworkAvailable(context) ->
                confirmTaskInterface.onAlertConfirmTask(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, ConfirmTaskActivity.ALERT_CONFIRM_TASK_NO_CONNECTION)
            !isSubmitConfirm() -> confirmTaskInterface.onAlertMessage(context.getString(R.string.fill_in_the_blank), ConstantObject.vToastInfo)
            else -> confirmTaskInterface.onAlertConfirmTask(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, ConfirmTaskActivity.ALERT_CONFIRM_TASK_CONFIRMATION)
        }
    }

    fun onLoadConfirmData(intentTaskId : String, intentTypeTask : String){
        stIntentTaskId = intentTaskId
        isProgressConfirmTask.set(true)
        stTypeTask = intentTypeTask
        Handler().postDelayed({
            stConfirmChargeCode.set("A-1003-096")
            stConfirmCompName.set("PT ABCD")
            stConfirmCompAddress.set("Jakarta")
            stConfirmCP.set("Michael Saputra")
            stConfirmTaskDate.set("17/01/2020")
            stConfirmTaskTimeFrom.set("08:00")
            stConfirmTaskTimeInto.set("17:00")
            stConfirmDescription.set("Buat Mobile Hris")
            confirmTaskInterface.onCheckConfirmRadio(true)
            when(intentTypeTask){
                ConstantObject.projectTask -> {
                    isHiddenPMTv.set(false)
                    isHiddenSolmanNoTv.set(true)
                    stConfirmPM.set("Edo Saputra")
                }
                ConstantObject.supportTask -> {
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
        }, 2000)
    }

    private fun isSubmitConfirm() : Boolean {
        if(stTypeTask == ConstantObject.preSalesTask ||
            stTypeTask == ConstantObject.prospectTask ||
            stTypeTask == ConstantObject.projectTask){
                if(stConfirmActHour.get() == "" || stConfirmActDescription.get() == "") return false
        }else {
            if(stConfirmActHour.get() == "") return false
        }
        return true
    }

    fun submitConfirmTask(){
        isProgressConfirmTask.set(true)
        Handler().postDelayed({
            confirmTaskInterface.onAlertMessage("Transaction Success", ConstantObject.vSnackBarWithButton)
            isProgressConfirmTask.set(false)
        }, 2000)
    }
}