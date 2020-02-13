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
import com.wcs.mobilehris.database.daos.TravelRequestDao
import com.wcs.mobilehris.database.entity.TravelRequestEntity
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
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
    val isAddDestination = ObservableField<Boolean>(false)
    val isTravelSelected = ObservableField<Boolean>(true)
    private val listSelectedTeam = mutableListOf<FriendModel>()
    private lateinit var mTransTypeDao : TransTypeDao
    private lateinit var mChargeCodeDao : ChargeCodeDao
    private lateinit var travelRequestDao : TravelRequestDao
    private val calendar : Calendar = Calendar.getInstance()
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0

    fun initTravelDateFrom(){displayCalendar(RequestTravelActivity.chooseDateFrom)}
    fun initTravelDateInto(){
        when{
            stDepartDate.get().isNullOrEmpty() -> requestTravelInterface.onMessage("Please Fill Depart Date first ", ConstantObject.vSnackBarWithButton)
            else -> displayCalendar(RequestTravelActivity.chooseDateInto)
        }
    }

    private fun displayCalendar(chooseFrom : String){
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedMonth: String = if (month < 10) { "0" + (month + 1) } else { month.toString() }
                val selectedDay: String = if (dayOfMonth < 10) { "0$dayOfMonth" } else { dayOfMonth.toString() }
                when(chooseFrom){
                    RequestTravelActivity.chooseDateFrom -> stDepartDate.set("$year-$selectedMonth-$selectedDay")
                    RequestTravelActivity.chooseDateInto  -> validateDateInto("$year-$selectedMonth-$selectedDay")
                }
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun validateDateInto(selectedEndDate : String){
        val intDiffDate = DateTimeUtils.getDifferentDate(stDepartDate.get().toString(), selectedEndDate.trim())
        when{
            intDiffDate < 0 -> requestTravelInterface.onMessage("Return Date should not less then Depart Date  ", ConstantObject.vSnackBarWithButton)
            else -> stReturnDate.set(selectedEndDate.trim())
        }
    }

    fun initDataChargeCode(){
        mChargeCodeDao = WcsHrisApps.database.chargeCodeDao()
        travelRequestDao = WcsHrisApps.database.travelReqDao()
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
            val itemFriendModel = FriendModel(itemUserId, itemName, "Free", false)

            listSelectedTeam.add(itemFriendModel)
            requestTravelInterface.onLoadTeam(listSelectedTeam)
        }
    }

    fun addTeamTravel(){requestTravelInterface.getTeamData()}
    fun addDestination(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> requestTravelInterface.onAlertReqTravel(context.getString(R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, RequestTravelActivity.ALERT_REQ_TRAVEL_NO_CONNECTION)
            !validateTravel() -> requestTravelInterface.onMessage(context.getString(R.string.fill_in_the_blank), ConstantObject.vSnackBarWithButton)
            else -> saveDestination()
        }
    }

    private fun saveDestination(){
        isProgressReqTravel.set(true)
        doAsync {
            var stFriend : String? = ""
            val maxDestinationId = travelRequestDao.getTravelMaxId() + 1
            when(listSelectedTeam.size){
                0 -> stFriend = ""
                else -> {
                    for(i in listSelectedTeam.indices){
                        stFriend = if (i == 0) listSelectedTeam[i].teamName +"-"+listSelectedTeam[i].friendId
                        else stFriend + "|"+listSelectedTeam[i].teamName +"-"+listSelectedTeam[i].friendId
                    }
                }
            }
            val model = TravelRequestEntity(maxDestinationId, stChargeCode.get().toString(),
                stDepartDate.get().toString(), stReturnDate.get().toString(),
                stDepartFrom.get().toString(), stTravelInto.get().toString(),stFriend.toString().trim())
            travelRequestDao.insertTravel(model)

            uiThread {
                stDepartDate.set("")
                stReturnDate.set("")
                stDepartFrom.set("")
                stTravelInto.set("")
                listSelectedTeam.clear()
                requestTravelInterface.onClearListTeam()
                requestTravelInterface.disableUI(ConstantObject.vGlobalUI)
                requestTravelInterface.disableUI(ConstantObject.vEditTextUI)
                requestTravelInterface.onMessage(context.getString(R.string.alert_transaction_success), ConstantObject.vToastSuccess)
                isProgressReqTravel.set(false)
            }
        }
    }

    fun submitTravel(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> requestTravelInterface.onAlertReqTravel(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, RequestTravelActivity.ALERT_REQ_TRAVEL_NO_CONNECTION)
            else -> {
                when(stTypeTrip.get().toString()){
                    context.getString(R.string.multiple_destination) -> {
                        doAsync{
                            val countData = travelRequestDao.getCountTravel(stChargeCode.get().toString())
                            uiThread {
                                when{
                                    countData <= 1 -> requestTravelInterface.onMessage("Please choose Add Destination before you save travel", ConstantObject.vSnackBarWithButton)
                                    else -> requestTravelInterface.onIntentMultipleDestination()
                                }
                            }
                        }
                    }
                    else ->  {
                        when{
                            !validateTravel() -> requestTravelInterface.onMessage(context.getString(R.string.fill_in_the_blank), ConstantObject.vSnackBarWithButton)
                            else -> requestTravelInterface.onAlertReqTravel(context.getString(R.string.transaction_alert_confirmation),
                                ConstantObject.vAlertDialogConfirmation, RequestTravelActivity.ALERT_REQ_TRAVEL_CONFIRMATION)
                        }
                    }
                }
            }
        }
    }

    fun actionSubmitTravel(){
        isProgressReqTravel.set(true)
        requestTravelInterface.onSuccessRequestTravel()
    }

    fun onClickDepart(){requestTravelInterface.getDataDepart()}
    fun onClickReturn(){requestTravelInterface.getDataReturn()}

    fun validateCityReturn(cityReturn : String){
        when{
            !stDepartFrom.get().equals("") -> {
                requestTravelInterface.onHideSoftKeyboard()
                when(cityReturn){
                    stDepartFrom.get() -> requestTravelInterface.onMessage("The city cannot be the same ", ConstantObject.vSnackBarWithButton)
                    else -> stTravelInto.set(cityReturn.trim())
                }
            }
        }
    }

    private fun validateTravel() : Boolean{
        when{
            stTypeTrip.get().equals("")||
                    stChargeCode.get().equals("")||
                    stTransTypeCode.get().equals("")||
                    stDepartFrom.get().equals("")||
                    stTravelInto.get().equals("")||
                    stDepartDate.get().equals("")||
                    stReturnDate.get().equals("")
            -> return false
        }
        return true
    }

    fun clickSetTravel(){
        isTravelSelected.set(true)
        requestTravelInterface.onChangeButtonBackground(true)
    }
    fun clickSetDestination(){
        isTravelSelected.set(false)
        requestTravelInterface.onChangeButtonBackground(false)
    }
}