package com.wcs.mobilehris.feature.createtask

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.database.daos.ChargeCodeDao
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class CreateTaskViewModel(private val context : Context, private val createTaskInterface : CreateTaskInterface) :
    ViewModel(){
    val isProgressCreateTask = ObservableField(false)
    val isHiddenRv = ObservableField(false)
    val isHiddenSolmanTv = ObservableField(false)
    val isHiddenPMTv = ObservableField(false)
    val isEnableCompanyNameTxt = ObservableField(false)
    val isEnablePMTxt = ObservableField(false)
    val stDateTaskFrom = ObservableField("")
    val stDateTaskInto = ObservableField("")
    val stChargeCodeValidFrom = ObservableField("")
    val stChargeCodeValidInto = ObservableField("")
    val stDateTimeFrom = ObservableField("")
    val stDateTimeInto = ObservableField("")
    val stCompanyName = ObservableField("")
    val stContactPerson = ObservableField("")
    val stDescriptionTask = ObservableField("")
    val stSolmanNoTask = ObservableField("")
    val stPMTask = ObservableField("")
    val stPMCode = ObservableField("")
    private val calendar : Calendar = Calendar.getInstance()
    private var mChargeCode : String = ""
    private var mTypeTask : String = ""
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0
    private var mHour : Int = 0
    private var mMinute : Int = 0
    private var mSecond : Int = 0
    private lateinit var mChargeCodeDao : ChargeCodeDao

    fun initUI(){
        isProgressCreateTask.set(false)
        isHiddenRv.set(true)
        isHiddenSolmanTv.set(true)
        isHiddenPMTv.set(true)
        mChargeCodeDao = WcsHrisApps.database.chargeCodeDao()
    }

    private fun initDate(dateColumn : String){
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, OnDateSetListener { view, year, month, dayOfMonth ->
                    val selectedMonth: String = if (month < 10) { "0" + (month + 1) } else { month.toString() }
                    val selectedDay: String = if (dayOfMonth < 10) { "0$dayOfMonth" } else { dayOfMonth.toString() }
            when(dateColumn){
                CreateTaskActivity.chooseDateFrom -> validateDate(CreateTaskActivity.chooseDateFrom,"$year-$selectedMonth-$selectedDay")
                else -> validateDate(CreateTaskActivity.chooseDateInto,"$year-$selectedMonth-$selectedDay")
            }

            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    fun initDateFrom(){initDate(CreateTaskActivity.chooseDateFrom)}
    fun initDateInto(){
        when{
            stDateTaskFrom.get().equals("") -> createTaskInterface.onMessage("Please fill Date from ", ConstantObject.vSnackBarWithButton)
            else -> initDate(CreateTaskActivity.chooseDateInto)
        }
    }

    private fun validateDate(dateColumn : String, dateChoosen : String){
        when(dateColumn){
            CreateTaskActivity.chooseDateInto -> {
                val intEndDiff = DateTimeUtils.getDifferentDate(stDateTaskFrom.get().toString(), dateChoosen)
                when{
                    intEndDiff < 0 -> createTaskInterface.onMessage("Date To less then Date From, Please fill again ", ConstantObject.vSnackBarWithButton)
                    intEndDiff < 1 -> createTaskInterface.onMessage("Date to less then one day, Please fill again ", ConstantObject.vSnackBarWithButton)
                    else -> stDateTaskInto.set(dateChoosen.trim())
                }
            }
            else -> {
                val intFromDiff = DateTimeUtils.getDifferentDate(DateTimeUtils.getCurrentDate(), dateChoosen)
                when{
                    intFromDiff < 0 -> createTaskInterface.onMessage("Date From Should be today, Please fill again ", ConstantObject.vSnackBarWithButton)
                    else -> stDateTaskFrom.set(dateChoosen)
                }
            }
        }
    }

    fun onAddTeam(){ createTaskInterface.getTeamData() }
    fun initTimeFrom(){ initTime(CreateTaskActivity.chooseTimeFrom) }
    fun initTimeInto(){
        when{
            stDateTimeFrom.get().equals("") -> createTaskInterface.onMessage("Please fill Time from ", ConstantObject.vSnackBarWithButton)
            else -> initTime(CreateTaskActivity.chooseTimeInto)
        }
    }

    private fun initTime(chooseTime : String){
        mHour = calendar.get(Calendar.HOUR_OF_DAY)
        mMinute = calendar.get(Calendar.MINUTE)
        mSecond = calendar.get(Calendar.SECOND)
        var selectedHour: String
        var selectedMinutes: String
        var selectedSecond: String
        val timePickerDialog = TimePickerDialog(context, OnTimeSetListener { view, hourOfDay, minute ->
                selectedHour = if (hourOfDay < 10) { "0$hourOfDay" } else { hourOfDay.toString() }
                selectedMinutes = if (minute < 10) { "0$minute" } else { minute.toString() }
                selectedSecond = if (mSecond < 10) { "0$mSecond" } else { mSecond.toString() }
                when(chooseTime){
                    CreateTaskActivity.chooseTimeFrom -> stDateTimeFrom.set("$selectedHour:$selectedMinutes:$selectedSecond")
                    CreateTaskActivity.chooseTimeInto -> validateEndTime("$selectedHour:$selectedMinutes:$selectedSecond")
                }
            }, mHour, mMinute, true
        )
        timePickerDialog.show()
    }

    private fun validateEndTime(endTime : String){
        val intDiff = DateTimeUtils.getDifferentHours(stDateTimeFrom.get().toString(), endTime)
        when{
            intDiff < 0 -> createTaskInterface.onMessage("End Time less then Start Time, Please fill again ", ConstantObject.vSnackBarWithButton)
            intDiff < 1 -> createTaskInterface.onMessage("End Time less then one hour, Please fill again ", ConstantObject.vSnackBarWithButton)
            else -> stDateTimeInto.set(endTime.trim())
        }
    }

    fun validateTask(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> createTaskInterface.onAlertCreateTask(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, CreateTaskActivity.ALERT_CREATE_TASK_NO_CONNECTION)
            !validateSubmitTask() -> createTaskInterface.onMessage(context.getString(R.string.fill_in_the_blank), ConstantObject.vSnackBarWithButton)
            else -> createTaskInterface.onAlertCreateTask(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, CreateTaskActivity.ALERT_CREATE_TASK_CONFIRMATION)
        }
    }

    fun initDataChargeCode(){
        doAsync {
            val listTableChargeCode = mChargeCodeDao.getAllChargeCode()
            uiThread {
                when{listTableChargeCode.isNotEmpty() -> createTaskInterface.onLoadChargeCode(listTableChargeCode) }
            }
        }
    }

    fun findDataCreateTask(code : String){
        mChargeCode = code
        when(code.substring(0, 1)){
            "F" -> {
                mTypeTask = ConstantObject.vProjectTask
                isHiddenSolmanTv.set(true)
                isHiddenPMTv.set(false)
            }
            "E" -> {
                mTypeTask = ConstantObject.vSupportTask
                isHiddenSolmanTv.set(false)
                isHiddenPMTv.set(true)
            }
            else -> {
                isHiddenSolmanTv.set(true)
                isHiddenPMTv.set(true)
            }
        }

        doAsync {
            val listDtlChargeCode  = mChargeCodeDao.getDetailChargeCode(code.trim())
            uiThread {
                when{
                    listDtlChargeCode.isNotEmpty() ->{
                        val compName = listDtlChargeCode[0].mCompanyName.trim()
                        stChargeCodeValidFrom.set(listDtlChargeCode[0].mValidFrom.trim())
                        stChargeCodeValidInto.set(listDtlChargeCode[0].mValidInto.trim())
                        when{
                            compName.isNotEmpty() -> {
                                stCompanyName.set(compName)
                                isEnableCompanyNameTxt.set(false)
                            }
                            else -> isEnableCompanyNameTxt.set(true)
                        }

                        when(mTypeTask){
                            ConstantObject.vProjectTask -> {
                                val stDataPM = listDtlChargeCode[0].mProjectManagerName.trim()
                                stPMCode.set(listDtlChargeCode[0].mProjectManagerNik.trim())
                                when{
                                    stDataPM.isNotEmpty() -> {
                                        stPMTask.set(stDataPM.trim())
                                        isEnablePMTxt.set(false)
                                    }
                                    else -> isEnablePMTxt.set(true)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun validateTeam(itemUserId : String, itemName : String, stTeamStatus : String){
        if (itemUserId != "null" && itemName != "null" && stTeamStatus != "null"){
            val listSelectedTeam = mutableListOf<FriendModel>()
            val itemFriendModel = if(stTeamStatus.contains("Available")){
                FriendModel(itemUserId, itemName, false)
            }else {
                FriendModel(itemUserId, itemName, true)
            }

            listSelectedTeam.add(itemFriendModel)
            createTaskInterface.onLoadTeam(listSelectedTeam)
        }
    }
    
    private fun validateSubmitTask():Boolean{
        when{
                stDateTimeFrom.get().equals("")
                        || stDateTimeInto.get().equals("")
                        || stCompanyName.get().equals("")
                        || stDateTaskFrom.get().equals("")
                        || stContactPerson.get().equals("")
                        || stDescriptionTask.get().equals("")
                        || mTypeTask == "" -> return false
            mTypeTask == ConstantObject.vSupportTask -> { when{stSolmanNoTask.get().equals("") -> return false } }
            mTypeTask == ConstantObject.vProjectTask -> { when{stPMTask.get().equals("") -> return false } }
        }
        return true
    }

    fun submitTask(){
        isProgressCreateTask.set(true)
        createTaskInterface.onSuccessCreateTask()
    }

    fun onBackCreateTaskMenu(){
        val intent = Intent(context, MenuActivity::class.java)
        intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_ACTIVITY)
        context.startActivity(intent)
    }
}