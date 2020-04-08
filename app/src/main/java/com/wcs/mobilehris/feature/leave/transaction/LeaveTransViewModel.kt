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
import com.wcs.mobilehris.util.Preference
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.lang.Exception
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
    val stLeaveRejectButton = ObservableField("")
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
    private var leaveId : Int = 0
    private var reasonCode : String = ""
    private var reasonLeaveDescription : String = ""
    private var intentFromLeave : String = ""
    private var preference = Preference(context)
    private var strIdLeave : String = ""
    private var strDocNo : String = ""

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
                stLeaveSubmitButton.set(context.getString(R.string.approve))
                isHiddenButton.set(false)
                isHiddenRejectButton.set(false)
                stLeaveRejectButton.set(context.getString(R.string.reject_save))
            }
            else -> {
                isDtlLeaveMenu.set(false)
                stLeaveSubmitButton.set(ConstantObject.vEditTask)
                isHiddenButton.set(false)
                isHiddenRejectButton.set(false)
                stLeaveRejectButton.set(context.getString(R.string.delete))

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
                        Log.d("###", "getLeaveData -> response = $data")
                        val jObjLeave = JSONObject(responseLeave)
                        strIdLeave = jObjLeave.getString("ID")
                        strDocNo = jObjLeave.getString("DOCUMENT_NO")

                        stLeaveDateFrom.set(jObjLeave.getString("DATE_FROM"))
                        stLeaveDateInto.set(jObjLeave.getString("DATE_TO"))
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

    //todo add api here
    fun onSubmitLeave(clickAlertFrom : Int){
        isProgressLeaveTrans.set(true)
        when(clickAlertFrom) {
            LeaveTransactionActivity.ALERT_LEAVE_TRANS_REQUEST -> postReqLeave()
            LeaveTransactionActivity.ALERT_LEAVE_TRANS_EDIT -> editLeaveRequest()
            LeaveTransactionActivity.ALERT_LEAVE_TRANS_DELETE -> deleteLeaveRequest()
            LeaveTransactionActivity.ALERT_LEAVE_TRANS_APPROVE -> approveLeaveRequest(strDocNo)
            LeaveTransactionActivity.ALERT_LEAVE_TRANS_REJECT -> rejectLeaveRequest(strDocNo, stLeaveRejectNotes.get().toString())
            else ->{
                Handler().postDelayed({
                    when(clickAlertFrom) {
                        LeaveTransactionActivity.ALERT_LEAVE_TRANS_EDIT -> leaveTransInterface.onSuccessLeaveTrans("Transaction Success Edited")
                        LeaveTransactionActivity.ALERT_LEAVE_TRANS_APPROVE -> leaveTransInterface.onSuccessLeaveTrans("Transaction Success Approved")
                        else -> leaveTransInterface.onSuccessLeaveTrans("Transaction Successful rejected")
                    }
                }, 2000)
            }
        }
    }

    private fun postReqLeave(){
        apiRepo.insertLeave(preference.getUn(), context, jObjReqLeave(0, "", 1), object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    Log.d("###", "json $data")
                    val responseSuccess = it.getString(ConstantObject.vResponseStatus)
                    if(responseSuccess.contains(ConstantObject.vValueResponseSuccess)){leaveTransInterface.onSuccessLeaveTrans(context.getString(R.string.alert_transaction_success))}
                }
            }

            override fun onDataError(error: String?) {
                isProgressLeaveTrans.set(false)
                leaveTransInterface.onMessage("failed leave " +error.toString(), ConstantObject.vToastError)
            }
        })
    }

    private fun jObjReqLeave(strIdLeave : Int, strDocNumber : String, func : Int):JSONObject{
        var dateFrom : String = ""
        var dateTo : String = ""
        if(func == 1) {
            dateFrom = stLeaveDateFrom.get().toString()
            dateTo = stLeaveDateInto.get().toString()
        } else if (func == 2) {
            dateFrom = DateTimeUtils.changeDateFormat(stLeaveDateFrom.get().toString()).toString()
            dateTo = DateTimeUtils.changeDateFormat(stLeaveDateInto.get().toString()).toString()
        }

        val paramLeaveReq = JSONObject()
        paramLeaveReq.put("ID", strIdLeave)
        paramLeaveReq.put("CHARGE_CD", reasonCode)
        paramLeaveReq.put("DOCUMENT_NO", strDocNumber)
        paramLeaveReq.put("DOCUMENT_DT", null)
        paramLeaveReq.put("LEAVE_TYPE_CD", leaveId.toString())
        paramLeaveReq.put("LEAVE_TYPE_NAME", reasonLeaveDescription)
        paramLeaveReq.put("LEAVE_REASON", stLeaveNotes.get().toString().trim())
        paramLeaveReq.put("NIK", preference.getUn().trim())
        paramLeaveReq.put("DATE_FROM", dateFrom)
        paramLeaveReq.put("DATE_TO", dateTo)
        paramLeaveReq.put("TIME_FROM", DateTimeUtils.getChangeEnglishTimeFormat(stLeaveTimeFrom.get().toString()))
        paramLeaveReq.put("TIME_TO", DateTimeUtils.getChangeEnglishTimeFormat(stLeaveTimeInto.get().toString()))
        paramLeaveReq.put("LEAVE_DAY", countDay())
        paramLeaveReq.put("LEAVE_HOUR", stLeaveCountTime.get()?.toInt())
        paramLeaveReq.put("IS_APPROVED", null)
        paramLeaveReq.put("IS_CANCELLED", null)
        paramLeaveReq.put("IS_REJECTED", null)
        paramLeaveReq.put("LEAVE_STATUS_CD", null)
        paramLeaveReq.put("LEAVE_STATUS", null)
        paramLeaveReq.put("APPROVED_DT", null)
        paramLeaveReq.put("CREATED_BY", null)
        paramLeaveReq.put("CREATED_DT", null)
        paramLeaveReq.put("MODIFIED_BY", null)
        paramLeaveReq.put("MODIFIED_DT", null)
        paramLeaveReq.put("isEdit", "False")
        paramLeaveReq.put("STATUS_DESC", null)

        return paramLeaveReq
    }

    fun editLeaveRequest() {
        apiRepo.insertLeave(preference.getUn(), context, jObjReqLeave(strIdLeave.toInt(), strDocNo, 2), object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val objectResponse = it.getString(ConstantObject.vResponseData)
                    Log.d("###", "updateActivity -> response = $data")

                    val responseSuccess = it.getString(ConstantObject.vResponseStatus)
                    if(responseSuccess.contains(ConstantObject.vValueResponseSuccess)) {
                        leaveTransInterface.onSuccessLeaveTrans(context.getString(R.string.alert_transaction_success))
                    }
                }
            }

            override fun onDataError(error: String?) {
                isProgressLeaveTrans.set(false)
                leaveTransInterface.onMessage("failed leave " +error.toString(), ConstantObject.vToastError)
            }
        })
    }

    fun deleteLeaveRequest() {
        apiRepo.deleteLeave(preference.getUn(), strIdLeave, context, object : ApiRepo.ApiCallback<JSONObject> {
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val objectResponse = it.getString(ConstantObject.vResponseData)
                    Log.d("###", "deleteActivity -> response = $data")
                    leaveTransInterface.onSuccessLeaveTrans(context.getString(R.string.alert_transaction_success))
                }
            }

            override fun onDataError(error: String?) {
                isProgressLeaveTrans.set(false)
                leaveTransInterface.onMessage("failed delete " +error.toString(), ConstantObject.vToastError)
            }

        })
    }

    fun approveLeaveRequest(docNo : String) {
        apiRepo.approveLeaveRequest(docNo, context, object : ApiRepo.ApiCallback<JSONObject> {
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val objectResponse = it.getString(ConstantObject.vResponseData)
                    Log.d("###", "approveLeaveRequest -> response = $data")
                    leaveTransInterface.onSuccessLeaveTrans(context.getString(R.string.alert_transaction_success))
                }
            }

            override fun onDataError(error: String?) {
                isProgressLeaveTrans.set(false)
                leaveTransInterface.onMessage("failed approve leave request " +error.toString(), ConstantObject.vToastError)
            }

        })
    }

    fun rejectLeaveRequest(docNo : String, reason : String) {
        apiRepo.rejectLeaveRequest(docNo, reason, context, object : ApiRepo.ApiCallback<JSONObject> {
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val objectResponse = it.getString(ConstantObject.vResponseData)
                    Log.d("###", "rejectLeaveRequest -> response = $data")
                    leaveTransInterface.onSuccessLeaveTrans(context.getString(R.string.alert_transaction_success))
                }
            }

            override fun onDataError(error: String?) {
                isProgressLeaveTrans.set(false)
                leaveTransInterface.onMessage("failed delete " +error.toString(), ConstantObject.vToastError)
            }

        })
    }

    private fun countDay():Int{
        return try{
            when(reasonLeaveDescription.trim()){
                LeaveTransactionActivity.valueTwoHour -> 0
                LeaveTransactionActivity.valueFourHours -> 0
                else -> DateTimeUtils.getDifferentDate(stLeaveDateFrom.get().toString(), stLeaveDateInto.get().toString().trim())+1
            }
        }catch (e: Exception){0}

    }

    fun clickLeaveReject() {
        when {
            !ConnectionObject.isNetworkAvailable(context) -> leaveTransInterface.onAlertLeaveTrans(
                context.getString(R.string.alert_no_connection), ConstantObject.vAlertDialogNoConnection,
                LeaveTransactionActivity.ALERT_LEAVE_TRANS_NO_CONNECTION)

            intentFromLeave == ConstantObject.vEditTask -> leaveTransInterface.onAlertLeaveTrans(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, LeaveTransactionActivity.ALERT_LEAVE_TRANS_DELETE)

            intentFromLeave == ConstantObject.extra_fromIntentApproval -> leaveTransInterface.onAlertLeaveTrans(context.getString(R.string.transaction_alert_confirmation),
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
                    stLeaveNotes.get().equals("") -> return false
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
        doAsync {
            val reasonLeaveList = mReasonLeaveDao.getDtlReason(reasonLeaveDescription.trim())
            uiThread {
                leaveId = reasonLeaveList[0].id
            }
        }
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
