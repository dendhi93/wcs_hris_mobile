package com.wcs.mobilehris.feature.requesttravel

import android.app.DatePickerDialog
import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.database.daos.ChargeCodeDao
import com.wcs.mobilehris.database.daos.TransTypeDao
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.util.ConstantObject
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class RequestTravelViewModel (private val context : Context, private val requestTravelInterface: RequestTravelInterface): ViewModel(){
    val isProgressReqTravel = ObservableField<Boolean>(false)
    val stDepartDate = ObservableField<String>("")
    val stReturnDate = ObservableField<String>("")
    val stDepartFrom = ObservableField<String>("")
    val stTravelInto = ObservableField<String>("")
    val stTypeTrip = ObservableField<String>("")
    val stChargeCode = ObservableField<String>("")
    val stTransTypeCode = ObservableField<String>("")
    private lateinit var mTransTypeDao : TransTypeDao
    private lateinit var mChargeCodeDao : ChargeCodeDao
    private val calendar : Calendar = Calendar.getInstance()
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0

    fun initTravelDateFrom(){initTravelDate(RequestTravelActivity.chooseDateFrom)}
    fun initTravelDateInto(){initTravelDate(RequestTravelActivity.chooseDateInto)}

    private fun initTravelDate(chooseFrom : String){
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedMonth: String = if (month < 10) { "0" + (month + 1) } else { month.toString() }
                val selectedDay: String = if (dayOfMonth < 10) { "0$dayOfMonth" } else { dayOfMonth.toString() }
                when(chooseFrom){
                    RequestTravelActivity.chooseDateFrom -> stDepartDate.set("$year-$selectedMonth-$selectedDay")
                    RequestTravelActivity.chooseDateInto  -> stReturnDate.set("$year-$selectedMonth-$selectedDay")
                }
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    fun initDataChargeCode(){
        mChargeCodeDao = WcsHrisApps.database.chargeCodeDao()
        doAsync {
            val listTableChargeCode = mChargeCodeDao.getAllChargeCode()
            uiThread {
                when{listTableChargeCode.isNotEmpty() -> requestTravelInterface.onLoadChargeCode(listTableChargeCode) }
            }
        }
    }

    fun getDataTransport(){
        mTransTypeDao = WcsHrisApps.database.transTypeDao()
        doAsync {
            val listDataTrans = mTransTypeDao.getAllTransType()
            uiThread {
                when{
                    listDataTrans.isNotEmpty() -> requestTravelInterface.onLoadTransport(listDataTrans)
                }
            }
        }
    }

    fun validateTravelTeam(itemUserId : String, itemName : String){
        if (itemUserId != "null" && itemName != "null"){
            val listSelectedTeam = mutableListOf<FriendModel>()
            val itemFriendModel = FriendModel(itemUserId, itemName, "Free", false)

            listSelectedTeam.add(itemFriendModel)
            requestTravelInterface.onLoadTeam(listSelectedTeam)
        }
    }

    fun addTeamTravel(){requestTravelInterface.getTeamData()}

    fun submitTravel(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> {
                requestTravelInterface.onAlertReqTravel(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, RequestTravelActivity.ALERT_REQ_TRAVEL_NO_CONNECTION)
            }
            else -> requestTravelInterface.onAlertReqTravel(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, RequestTravelActivity.ALERT_REQ_TRAVEL_CONFIRMATION)
        }
    }

    fun onClickDepart(){requestTravelInterface.getDataDepart()}
    fun onClickReturn(){requestTravelInterface.getDataReturn()}

    fun validateCityReturn(cityReturn : String){
        when{
            !stDepartFrom.get().equals("") -> {
                when(cityReturn){
                    stDepartFrom.get() -> requestTravelInterface.onMessage("The city cannot be the same ", ConstantObject.vSnackBarWithButton)
                    else -> stTravelInto.set(cityReturn.trim())
                }
            }
        }
    }

}