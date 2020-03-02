package com.wcs.mobilehris.feature.leave.transaction

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.database.daos.ReasonLeaveDao
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class LeaveTransViewModel (private val context: Context, private val leaveTransInterface: LeaveTransInterface) : ViewModel(){
    val isProgressLeaveTrans = ObservableField<Boolean>(false)
    val isDtlLeaveMenu = ObservableField<Boolean>(false)
    val stLeaveSubmitButton = ObservableField<String>("")
    val stLeaveDateFrom = ObservableField<String>("")
    val stLeaveDateInto = ObservableField<String>("")
    val stLeaveTimeFrom = ObservableField<String>("")
    val stLeaveTimeInto = ObservableField<String>("")
    private lateinit var mReasonLeaveDao : ReasonLeaveDao
    private val calendar : Calendar = Calendar.getInstance()
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0
    private var mHour : Int = 0
    private var mMinute : Int = 0
    private var mSecond : Int = 0

    fun onInitLeaveData(fromLeaveMenu : String){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> leaveTransInterface.onAlertLeaveTrans(context.getString(
                R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, LeaveTransactionActivity.ALERT_LEAVE_TRANS_NO_CONNECTION)
            else -> getLeaveData(fromLeaveMenu)
        }
    }

    private fun getLeaveData(fromLeaveMenu : String){
        if(fromLeaveMenu != ConstantObject.extra_fromIntentRequest){
            isProgressLeaveTrans.set(true)
                Handler().postDelayed({
                    isProgressLeaveTrans.set(false)
                }, 2000)
        }
    }

    fun initDateFrom(){initDate(LeaveTransactionActivity.columnDateFrom)}
    fun initDateInto(){
        when(stLeaveDateFrom.get()?.trim()){
            "" -> leaveTransInterface.onMessage("Please fill date from first", ConstantObject.vSnackBarWithButton)
            else -> initDate(LeaveTransactionActivity.columnTimeFrom)
        }
    }

    private fun initDate(fromColumn : String){
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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
                    LeaveTransactionActivity.columnDateFrom -> stLeaveDateFrom.set("$year-$selectedMonth-$selectedDay")
                    else -> validateDate("$year-$selectedMonth-$selectedDay")
                }
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    private fun validateDate(selectedDate : String){
        val intDiffDate = DateTimeUtils.getDifferentDate(stLeaveDateFrom.get().toString(), selectedDate.trim())
        when{
            intDiffDate < 0 -> leaveTransInterface.onMessage("Return Date should not less then Depart Date  ", ConstantObject.vSnackBarWithButton)
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

    private fun initLeaveTime(chooseTime : String){
        mHour = calendar.get(Calendar.HOUR_OF_DAY)
        mMinute = calendar.get(Calendar.MINUTE)
        mSecond = calendar.get(Calendar.SECOND)
        var selectedHour: String
        var selectedMinutes: String
        var selectedSecond: String
        val timePickerDialog = TimePickerDialog(context,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                selectedHour = if (hourOfDay < 10) {
                    "0$hourOfDay"
                } else {
                    hourOfDay.toString()
                }
                selectedMinutes = if (minute < 10) {
                    "0$minute"
                } else {
                    minute.toString()
                }
                selectedSecond = if (mSecond < 10) {
                    "0$mSecond"
                } else {
                    mSecond.toString()
                }
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
            intDiff < 0 -> leaveTransInterface.onMessage("Time To less then Time From, Please fill again ", ConstantObject.vSnackBarWithButton)
            intDiff < 1 -> leaveTransInterface.onMessage("Time To less then two hour, Please fill again ", ConstantObject.vSnackBarWithButton)
            else -> stLeaveTimeInto.set(chooseTime.trim())
        }
    }

    fun validateLeave(){
        leaveTransInterface.onMessage("Tes", ConstantObject.vToastInfo)
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

}