package com.wcs.mobilehris.feature.createtask

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject
import java.util.*


class CreateTaskViewModel(private val context : Context, private val createTaskInterface : CreateTaskInterface) :
    ViewModel(){
    val isProgressCreateTask = ObservableField<Boolean>(false)
    val isHiddenRv = ObservableField<Boolean>(false)
    val isHiddenSolmanTv = ObservableField<Boolean>(false)
    val isHiddenPMTv = ObservableField<Boolean>(false)
    val isOnsiteTask = ObservableField<Boolean>(true)
    val stDateTask = ObservableField<String>("")
    val stDateTimeFrom = ObservableField<String>("")
    val stDateTimeInto = ObservableField<String>("")
    val calendar : Calendar = Calendar.getInstance()
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0
    private var mHour : Int = 0
    private var mMinute : Int = 0
    private var mSecond : Int = 0

    fun initUI(){
        isProgressCreateTask.set(false)
        isHiddenRv.set(true)
        isHiddenSolmanTv.set(true)
        isHiddenPMTv.set(true)
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

    fun onAddTeam(){
        createTaskInterface.onMessage("Coba", ConstantObject.vToastInfo)
    }

    fun validateTask(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> {
                createTaskInterface.onAlertCreateTask(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, CreateTaskActivity.ALERT_CREATE_TASK_NO_CONNECTION)
            }
            else -> createTaskInterface.onMessage("Coba 2", ConstantObject.vToastInfo)
        }
    }

}