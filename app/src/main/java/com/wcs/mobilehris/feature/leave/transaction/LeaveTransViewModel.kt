package com.wcs.mobilehris.feature.leave.transaction

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.database.daos.ReasonLeaveDao
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.util.*

class LeaveTransViewModel (private val context: Context,
                           private val leaveTransInterface: LeaveTransInterface,
                           private val apiRepo: ApiRepo) : ViewModel(){
    val isProgressLeaveTrans = ObservableField(false)
    val isDtlLeaveMenu = ObservableField(false)
    val isHiddenContent = ObservableField(false)
    val isHiddenButton = ObservableField(false)
    val isHiddenRejectButton = ObservableField(false)
    val isHiddenDocText = ObservableField(false)
    val stLeaveSubmitButton = ObservableField("")
    val stLeaveDateFrom = ObservableField("")
    val stLeaveDateInto = ObservableField("")
    val stLeaveTimeFrom = ObservableField("")
    val stLeaveTimeInto = ObservableField("")
    val stLeaveCountTime = ObservableField("")
    val stLeaveNotes = ObservableField("")
    val stLeaveRejectNotes = ObservableField("")
    val stLeaveDoc = ObservableField("")
    private lateinit var mReasonLeaveDao : ReasonLeaveDao
    private val calendar : Calendar = Calendar.getInstance()
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0
    private var mHour : Int = 0
    private var mMinute : Int = 0
    private var mSecond : Int = 0
    private var reasonCode : String = ""
    private var reasonLeaveDescription : String = ""
    private var intentFromLeave : String = ""

    fun onInitLeaveData(fromLeaveMenu : String, intentLeaveId : String){
        intentFromLeave = fromLeaveMenu
        when{
            !ConnectionObject.isNetworkAvailable(context) -> leaveTransInterface.onAlertLeaveTrans(context.getString(
                R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, LeaveTransactionActivity.ALERT_LEAVE_TRANS_NO_CONNECTION)
            else -> getLeaveData(fromLeaveMenu, intentLeaveId)
        }
    }

    fun onValidateFromMenu(fromLeaveMenu : String){
        when(fromLeaveMenu){
            LeaveTransactionActivity.valueLeaveDtlType ->{
                isDtlLeaveMenu.set(true)
                isHiddenButton.set(true)
            }
            ConstantObject.extra_fromIntentRequest -> {
                isDtlLeaveMenu.set(false)
                stLeaveSubmitButton.set(context.getString(R.string.save))
                isHiddenButton.set(false)
                isHiddenRejectButton.set(true)
            }
            ConstantObject.extra_fromIntentApproval -> {
                isDtlLeaveMenu.set(true)
                stLeaveSubmitButton.set(context.getString(R.string.save))
                isHiddenButton.set(false)
                isHiddenRejectButton.set(false)
            }
            else -> {
                isDtlLeaveMenu.set(false)
                stLeaveSubmitButton.set(ConstantObject.vEditTask)
                isHiddenButton.set(false)
                isHiddenRejectButton.set(true)
            }
        }
    }

    private fun getLeaveData(fromLeaveMenu : String, fromLeaveId : String){
        if(fromLeaveMenu != ConstantObject.extra_fromIntentRequest){
            isHiddenDocText.set(false)
            isProgressLeaveTrans.set(true)
            isHiddenContent.set(true)
            apiRepo.getDtlLeave(fromLeaveId.trim(), context, object : ApiRepo.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        val responseLeave = it.getString(ConstantObject.vResponseData)
                        val jObjLeave = JSONObject(responseLeave)
                        stLeaveDateFrom.set(DateTimeUtils.getChangeDateFormat(jObjLeave.getString("DATE_FROM"), ConstantObject.dateTimeFormat_3))
                        stLeaveDateInto.set(DateTimeUtils.getChangeDateFormat(jObjLeave.getString("DATE_TO"), ConstantObject.dateTimeFormat_3))
                        stLeaveCountTime.set(jObjLeave.getString("LEAVE_HOUR"))
                        stLeaveTimeFrom.set(DateTimeUtils.getChangeTimeFormat(jObjLeave.getString("TIME_FROM")))
                        stLeaveTimeInto.set(DateTimeUtils.getChangeTimeFormat(jObjLeave.getString("TIME_TO")))
                        stLeaveNotes.set(jObjLeave.getString("LEAVE_REASON"))
                        stLeaveDoc.set(jObjLeave.getString("DOCUMENT_NO"))
                        leaveTransInterface.onSelectedSpinner(jObjLeave.getString("LEAVE_TYPE_NAME"))
                        when (fromLeaveMenu) {
                            ConstantObject.extra_fromIntentApproval -> validateReasonLeave(jObjLeave.getString("LEAVE_TYPE_NAME"),
                                jObjLeave.getString("CHARGE_CD"))
                        }
                        isProgressLeaveTrans.set(false)
                        isHiddenContent.set(false)
                    }
                }

                override fun onDataError(error: String?) {
                    isProgressLeaveTrans.set(false)
                    leaveTransInterface.onMessage("failed leave " +error.toString(), ConstantObject.vToastError)
                }
            })
//                Handler().postDelayed({
//                    stLeaveDateFrom.set("05/02/2020")
//                    stLeaveDateInto.set("05/02/2020")
//                    stLeaveCountTime.set("8")
//                    stLeaveTimeFrom.set("08:30")
//                    stLeaveTimeInto.set("17:30")
//                    when (fromLeaveMenu) {
//                        LeaveTransactionActivity.valueLeaveDtlType, ConstantObject.vEditTask -> {
//                            leaveTransInterface.onSelectedSpinner("Sick Leave")
//                            stLeaveNotes.set("Sakit Panas")
//                        }
//                        ConstantObject.extra_fromIntentApproval -> {
//                            leaveTransInterface.onSelectedSpinner("Sick Leave")
//                            validateReasonLeave("Sick Leave", "D-001002")
//                            stLeaveNotes.set("Sakit Panas")
//                        }
//                    }
//                    isProgressLeaveTrans.set(false)
//                    isHiddenContent.set(false)
//                }, 2000)
        }else{ isHiddenDocText.set(true)}

        when(fromLeaveMenu){
            ConstantObject.extra_fromIntentRequest ->{
                leaveTransInterface.enableUI(LeaveTransactionActivity.columnCountTime)
                leaveTransInterface.enableUI(LeaveTransactionActivity.columnDateInto)
            }
        }
    }

    fun initDateFrom(){initDate(LeaveTransactionActivity.columnDateFrom)}
    fun initDateInto(){
        when(stLeaveDateFrom.get()?.trim()){
            "" -> leaveTransInterface.onMessage("Please fill date from first", ConstantObject.vSnackBarWithButton)
            else -> initDate(LeaveTransactionActivity.columnDateInto)
        }
    }

    private fun initDate(fromColumn : Int){
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedMonth: String = if (month < 10) {
                    "0" + (month + 1)
                } else {
                    month.toString()
                }
                val selectedDay: String = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    dayOfMonth.toString()
                }
                when(fromColumn){
                    LeaveTransactionActivity.columnDateFrom -> validateDateFrom("$year-$selectedMonth-$selectedDay")
                    else -> validateDateInto("$year-$selectedMonth-$selectedDay")
                }
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    private fun validateDateFrom(selectedDateFrom : String){
        val intDiffDateFrom = DateTimeUtils.getDifferentDate(DateTimeUtils.getCurrentDate(), selectedDateFrom.trim())
        when{
            intDiffDateFrom < 0 -> leaveTransInterface.onMessage("Date From should not less than today", ConstantObject.vSnackBarWithButton)
            else -> {
                when (reasonLeaveDescription) {
                    LeaveTransactionActivity.valueTwoHour, LeaveTransactionActivity.valueFourHours, LeaveTransactionActivity.valueSickLeave -> {
                        stLeaveDateFrom.set(selectedDateFrom.trim())
                        stLeaveDateInto.set(selectedDateFrom.trim())
                    }
                    else -> stLeaveDateFrom.set(selectedDateFrom.trim())
                }
            }
        }
    }

    private fun validateDateInto(selectedDate : String){
        val intDiffDate = DateTimeUtils.getDifferentDate(stLeaveDateFrom.get().toString(), selectedDate.trim())
        when{
            intDiffDate < 0 -> leaveTransInterface.onMessage("Date into should not less than Date From ", ConstantObject.vSnackBarWithButton)
            else -> stLeaveDateInto.set(selectedDate.trim())
        }
    }

    fun initTimeFrom(){initLeaveTime(LeaveTransactionActivity.columnTimeFrom)}
    fun initTimeInto(){
        when(stLeaveTimeFrom.get()){
            "" -> leaveTransInterface.onMessage("Please fill Time From first", ConstantObject.vSnackBarWithButton)
            else -> initLeaveTime(LeaveTransactionActivity.columnTimeInto)
        }
    }

    private fun initLeaveTime(chooseTime : Int){
        mHour = calendar.get(Calendar.HOUR_OF_DAY)
        mMinute = calendar.get(Calendar.MINUTE)
        mSecond = calendar.get(Calendar.SECOND)
        var selectedHour: String
        var selectedMinutes: String
        var selectedSecond: String
        val timePickerDialog = TimePickerDialog(context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                selectedHour = if (hourOfDay < 10) { "0$hourOfDay"
                } else { hourOfDay.toString() }
                selectedMinutes = if (minute < 10) { "0$minute"
                } else { minute.toString() }
                selectedSecond = if (mSecond < 10) { "0$mSecond"
                } else { mSecond.toString() }
                when (chooseTime) {
                    LeaveTransactionActivity.columnTimeFrom -> stLeaveTimeFrom.set("$selectedHour:$selectedMinutes:$selectedSecond")
                    LeaveTransactionActivity.columnTimeInto -> validateLeaveTime("$selectedHour:$selectedMinutes:$selectedSecond")
                }
            }, mHour, mMinute, true
        )
        timePickerDialog.show()
    }

    private fun validateLeaveTime(chooseTime : String){
        val intDiff = DateTimeUtils.getDifferentHours(stLeaveTimeFrom.get().toString(), chooseTime)
        when{
            intDiff < 0 -> leaveTransInterface.onMessage("Time To less than Time From, Please fill again ", ConstantObject.vSnackBarWithButton)
            intDiff < 1 -> leaveTransInterface.onMessage("Time To less than two hour, Please fill again ", ConstantObject.vSnackBarWithButton)
            intDiff > 9 -> leaveTransInterface.onMessage("Time To should not more than 8 hours, Please fill again ", ConstantObject.vSnackBarWithButton)
            else -> {
                when(reasonLeaveDescription){
                    LeaveTransactionActivity.valueTwoHour -> {
                        when{
                            intDiff > 2 -> leaveTransInterface.onMessage("Time To should not more than 2 hours, Please fill again ", ConstantObject.vSnackBarWithButton)
                            else -> stLeaveTimeInto.set(chooseTime.trim())
                        }
                    }
                    LeaveTransactionActivity.valueFourHours -> {
                        when{
                            intDiff > 4 -> leaveTransInterface.onMessage("Time To should not more than 4 hours, Please fill again ", ConstantObject.vSnackBarWithButton)
                            else -> stLeaveTimeInto.set(chooseTime.trim())
                        }
                    }
                    else -> {
                        when{
                            intDiff < 8 -> leaveTransInterface.onMessage("Time To less than 8 hours, Please fill again ", ConstantObject.vSnackBarWithButton)
                            else -> stLeaveTimeInto.set(chooseTime.trim())
                        }
                    }
                }
            }
        }
    }

    fun clickLeave(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> leaveTransInterface.onAlertLeaveTrans(context.getString(
                R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, LeaveTransactionActivity.ALERT_LEAVE_TRANS_NO_CONNECTION)
            intentFromLeave == ConstantObject.vEditTask -> leaveTransInterface.onAlertLeaveTrans(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, LeaveTransactionActivity.ALERT_LEAVE_TRANS_EDIT)
            intentFromLeave == ConstantObject.extra_fromIntentRequest ->{
                when {
                    !validateLeave() -> leaveTransInterface.onMessage(context.getString(R.string.fill_in_the_blank), ConstantObject.vSnackBarWithButton)
                    else -> leaveTransInterface.onAlertLeaveTrans(context.getString(R.string.transaction_alert_confirmation),
                        ConstantObject.vAlertDialogConfirmation, LeaveTransactionActivity.ALERT_LEAVE_TRANS_REQUEST)
                }
            }
            intentFromLeave == ConstantObject.extra_fromIntentApproval -> leaveTransInterface.onAlertLeaveTrans(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, LeaveTransactionActivity.ALERT_LEAVE_TRANS_APPROVE)
        }
    }

     fun onSubmitLeave(clickAlertFrom : Int){
         isProgressLeaveTrans.set(true)
         Handler().postDelayed({
             when(clickAlertFrom) {
                 LeaveTransactionActivity.ALERT_LEAVE_TRANS_EDIT -> leaveTransInterface.onSuccessLeaveTrans("Transaction Success Edited")
                 LeaveTransactionActivity.ALERT_LEAVE_TRANS_REQUEST -> leaveTransInterface.onSuccessLeaveTrans(context.getString(R.string.alert_transaction_success))
                 LeaveTransactionActivity.ALERT_LEAVE_TRANS_APPROVE -> leaveTransInterface.onSuccessLeaveTrans("Transaction Success Approved")
                 else -> leaveTransInterface.onSuccessLeaveTrans("Transaction Successful rejected")
             }
         }, 2000)
    }

    fun clickLeaveReject() {
        when {
            !ConnectionObject.isNetworkAvailable(context) -> leaveTransInterface.onAlertLeaveTrans(
                context.getString(R.string.alert_no_connection), ConstantObject.vAlertDialogNoConnection,
                LeaveTransactionActivity.ALERT_LEAVE_TRANS_NO_CONNECTION)
            else -> leaveTransInterface.onAlertLeaveTrans(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, LeaveTransactionActivity.ALERT_LEAVE_TRANS_REJECT)
        }
    }

    private fun validateLeave() : Boolean{
        when{
            stLeaveDateFrom.get().equals("")||
                    stLeaveDateInto.get().equals("")||
                    stLeaveTimeFrom.get().equals("")||
                    stLeaveTimeInto.get().equals("")||
                    stLeaveCountTime.get().equals("")||
                    stLeaveNotes.get().equals("")||
                    reasonCode == "" -> return false
        }
        return true
    }

    fun initReasonSpinner(){
        mReasonLeaveDao = WcsHrisApps.database.reasonLeaveDao()
        doAsync {
            val reasonLeaveList = mReasonLeaveDao.getAllReasonLeave()
            uiThread {
                when{
                    reasonLeaveList.isNotEmpty() -> leaveTransInterface.onLoadReasonLeave(reasonLeaveList)
                }
            }
        }
    }

    fun validateReasonLeave(reasonDesc : String, codeLeave : String){
        reasonCode = codeLeave
        reasonLeaveDescription = reasonDesc
        when(reasonDesc){
            LeaveTransactionActivity.valueAnnualLeave ->{
                stLeaveCountTime.set("8")
                leaveTransInterface.disableUI(LeaveTransactionActivity.columnCountTime)
            }
            LeaveTransactionActivity.valueSickLeave ->{
                stLeaveCountTime.set("8")
                leaveTransInterface.disableUI(LeaveTransactionActivity.columnCountTime)
                leaveTransInterface.disableUI(LeaveTransactionActivity.columnDateInto)
            }
            LeaveTransactionActivity.valueTwoHour-> {
                stLeaveCountTime.set("2")
                leaveTransInterface.disableUI(LeaveTransactionActivity.columnCountTime)
                leaveTransInterface.disableUI(LeaveTransactionActivity.columnDateInto)
            }
            LeaveTransactionActivity.valueFourHours -> {
                stLeaveCountTime.set("4")
                leaveTransInterface.disableUI(LeaveTransactionActivity.columnCountTime)
                leaveTransInterface.disableUI(LeaveTransactionActivity.columnDateInto)
            }
        }
    }
}