package com.wcs.mobilehris.feature.createtask

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.database.daos.ChargeCodeDao
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.util.ConstantObject
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class CreateTaskViewModel(private val context : Context, private val createTaskInterface : CreateTaskInterface) :
    ViewModel(){
    val isProgressCreateTask = ObservableField<Boolean>(false)
    val isHiddenRv = ObservableField<Boolean>(false)
    val isHiddenSolmanTv = ObservableField<Boolean>(false)
    val isHiddenPMTv = ObservableField<Boolean>(false)
    val isEnableCompanyNameTv = ObservableField<Boolean>(false)
    val stTypeOnsite = ObservableField<String>("")
    val stDateTask = ObservableField<String>("")
    val stDateTimeFrom = ObservableField<String>("")
    val stDateTimeInto = ObservableField<String>("")
    val stCompanyName = ObservableField<String>("")
    val stCompanyAddress = ObservableField<String>("")
    val stContactPerson = ObservableField<String>("")
    val stDescriptionTask = ObservableField<String>("")
    val stSolmanNoTask = ObservableField<String>("")
    val stPMTask = ObservableField<String>("")
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

    fun initDate(){
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, OnDateSetListener { view, year, month, dayOfMonth ->
                    val selectedMonth: String = if (month < 10) { "0" + (month + 1) } else { month.toString() }
                    val selectedDay: String = if (dayOfMonth < 10) { "0$dayOfMonth" } else { dayOfMonth.toString() }
                    stDateTask.set("$year-$selectedMonth-$selectedDay")
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    fun initTime(){
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
                when{
                    stDateTimeFrom.get().isNullOrEmpty() -> stDateTimeFrom.set("$selectedHour:$selectedMinutes:$selectedSecond")
                    else -> stDateTimeInto.set("$selectedHour:$selectedMinutes:$selectedSecond")
                }
            }, mHour, mMinute, true
        )
        timePickerDialog.show()
    }

    fun onAddTeam(){ createTaskInterface.getTeamData() }

    fun validateTask(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> {
                createTaskInterface.onAlertCreateTask(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, CreateTaskActivity.ALERT_CREATE_TASK_NO_CONNECTION)
            }
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

    fun findCompany(code : String){
        mChargeCode = code
        var compName : String
        doAsync {
            compName  = mChargeCodeDao.getCompName(code.trim())
            uiThread {
                        when{
                            compName.isEmpty() -> {
                                isEnableCompanyNameTv.set(false)
                                stCompanyName.set(compName)
                            }
                            else ->{
                                isEnableCompanyNameTv.set(true)
                                stCompanyName.set("")
                            }
                        }
            }
        }
    }

    fun getTypeTask(selectedType : String){
        when(selectedType.trim()){
            ConstantObject.projectTask -> {
                isHiddenSolmanTv.set(true)
                isHiddenPMTv.set(false)
            }
            ConstantObject.supportTask -> {
                isHiddenSolmanTv.set(false)
                isHiddenPMTv.set(true)
            }
            else -> {
                isHiddenSolmanTv.set(true)
                isHiddenPMTv.set(true)
            }
        }
        mTypeTask = selectedType
    }

    fun validateTeam(itemUserId : String, itemName : String){
        if (itemUserId != "null" && itemName != "null"){
            val listSelectedTeam = mutableListOf<FriendModel>()
            val itemFriendModel = FriendModel(itemUserId, itemName, "Free", false)

            listSelectedTeam.add(itemFriendModel)
            createTaskInterface.onLoadTeam(listSelectedTeam)
        }
    }
    
    private fun validateSubmitTask():Boolean{
        when{
                stDateTimeFrom.get().equals("")
                        || stDateTimeInto.get().equals("")
                        || stCompanyName.get().equals("")
                        || stCompanyAddress.get().equals("")
                        || stDateTask.get().equals("")
                        || stContactPerson.get().equals("")
                        || stDescriptionTask.get().equals("")
                        || stTypeOnsite.get().equals("")
                        || mTypeTask == "" -> return false
            mTypeTask == ConstantObject.supportTask -> { when{stSolmanNoTask.get().equals("") -> return false } }
            mTypeTask == ConstantObject.projectTask -> { when{stPMTask.get().equals("") -> return false } }
        }
        return true
    }

    fun submitTask(){
        isProgressCreateTask.set(true)
        createTaskInterface.onSuccessCreateTask()
    }
}