package com.wcs.mobilehris.feature.dtlreqtravel

import android.app.DatePickerDialog
import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.database.daos.TransTypeDao
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class DtlTravelViewModel (private val context : Context, private val dtlTravelInterface: DtlTravelInterface) : ViewModel(){
    val isProgressDtlReqTravel = ObservableField<Boolean>(false)
    val isHideDtlTravelUI = ObservableField<Boolean>(false)
    val isConfirmTravelMenu = ObservableField<Boolean>(false)
    val stDtlTravelChargeCode = ObservableField<String>("")
    val stDtlTravelDepartDate = ObservableField<String>("")
    val stDtlTravelReturnDate = ObservableField<String>("")
    val stDtlTravelDepartCity = ObservableField<String>("")
    val stDtlTravelReturnCity = ObservableField<String>("")
    val stDtlTravelTransType = ObservableField<String>("")
    val stDtlTravelIsOneWay = ObservableField<Boolean>(false)
    private lateinit var mTransTypeDao : TransTypeDao

    private val calendar : Calendar = Calendar.getInstance()
    private var stIntentFromMenu : String? = null
    private var stIntentTravelId : String? = null
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0

    fun validateDataTravel(intentFrom : String, intentTravelId : String){
        when {
            !ConnectionObject.isNetworkAvailable(context) -> dtlTravelInterface.onAlertDtlReqTravel(context.getString(R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, DtlRequestTravelActivity.ALERT_DTL_REQ_TRAVEL_NO_CONNECTION)
            else -> getDataTravel(intentFrom, intentTravelId)
        }
    }

    private fun getDataTravel(intentFrom : String, intentTravelId : String){
        stIntentFromMenu = intentFrom.trim()
        stIntentTravelId = intentTravelId.trim()
        isProgressDtlReqTravel.set(true)
        isHideDtlTravelUI.set(true)
        when(intentFrom){
            ConstantObject.extra_fromIntentConfirmTravel -> isConfirmTravelMenu.set(true)
            else -> isConfirmTravelMenu.set(false)
        }

        Handler().postDelayed({
            stDtlTravelChargeCode.set("A-1003-096 BUSINESS DEVELOPMENT FOR MOBILITY ACTIVITY")
            stDtlTravelDepartDate.set("11-02-2020")
            stDtlTravelReturnDate.set("18-02-2020")
            stDtlTravelDepartCity.set("Jakarta")
            stDtlTravelReturnCity.set("Malaysia")
            stDtlTravelIsOneWay.set(false)
            dtlTravelInterface.selectedTravelWayRadio(stDtlTravelIsOneWay.get())
            dtlTravelInterface.selectedSpinner("PLANE")

            val dtlListFriend = mutableListOf<FriendModel>()
            var friendModel = FriendModel("62664930","Windy", "Free", false)
            dtlListFriend.add(friendModel)
            friendModel = FriendModel("62405890","Michael Saputra", "Conflict With Heinz ABC", true)
            dtlListFriend.add(friendModel)
            when{
                dtlListFriend.isNotEmpty() -> dtlTravelInterface.onLoadTeam(dtlListFriend)
            }

            isProgressDtlReqTravel.set(false)
            isHideDtlTravelUI.set(false)
        }, 2000)
    }

    fun getDataTransport(){
        mTransTypeDao = WcsHrisApps.database.transTypeDao()
        doAsync {
            val listDataTrans = mTransTypeDao.getAllTransType()
            uiThread {
                when{
                    listDataTrans.isNotEmpty() -> dtlTravelInterface.onLoadTransport(listDataTrans)
                }
            }
        }
    }

    fun getDtlDepartDate(){getDataCalendar(DtlRequestTravelActivity.selectDateFrom)}
    fun getDtlReturnDate(){getDataCalendar(DtlRequestTravelActivity.selectDateInto)}
    private fun getDataCalendar(selectFrom : String){
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val selectedMonth: String = if (month < 10) { "0" + (month + 1) } else { month.toString() }
            val selectedDay: String = if (dayOfMonth < 10) { "0$dayOfMonth" } else { dayOfMonth.toString() }
            when(selectFrom){
                DtlRequestTravelActivity.selectDateFrom -> stDtlTravelDepartDate.set("$year-$selectedMonth-$selectedDay")
                DtlRequestTravelActivity.selectDateInto  -> validateReturnDate("$year-$selectedMonth-$selectedDay")
            }
        }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun validateReturnDate(selectedEndDate : String){
        val intDiffDate = DateTimeUtils.getDifferentDate(stDtlTravelDepartDate.get().toString(), selectedEndDate.trim())
        when{
            intDiffDate < 0 -> dtlTravelInterface.onMessage("Return Date should not less then Depart Date  ", ConstantObject.vSnackBarWithButton)
            else -> stDtlTravelReturnDate.set(selectedEndDate.trim())
        }
    }

}