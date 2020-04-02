package com.wcs.mobilehris.feature.requesttravel

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.database.daos.ChargeCodeDao
import com.wcs.mobilehris.database.daos.ReasonTravelDao
import com.wcs.mobilehris.database.daos.TransTypeDao
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
import com.wcs.mobilehris.util.Preference
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class RequestTravelViewModel (private val context : Context,
                              private val requestTravelInterface: RequestTravelInterface,
                              private val apiRepo: ApiRepo): ViewModel(){
    val stDepartDate = ObservableField("")
    val stReturnDate = ObservableField("")
    val stDepartFrom = ObservableField("")
    val stTravelInto = ObservableField("")
    val stChargeCode = ObservableField("")
    val stTravelDescription = ObservableField("")
    val stTravelCheckIn = ObservableField("")
    val stTravelCheckOut = ObservableField("")
    val stTransTypeCode = ObservableField("")
    val stReasonCode = ObservableField("")
    val stAddTeamButton = ObservableField("")
    val stHotelName = ObservableField("")
    val isTravelSelected = ObservableField(true)
    val isProgressReqTravel = ObservableField(false)
    val isSetTravel = ObservableField(false)
    val isNonTB = ObservableField(false)
    private val listSelectedTeam = mutableListOf<FriendModel>()
    private lateinit var mTransTypeDao : TransTypeDao
    private lateinit var mChargeCodeDao : ChargeCodeDao
    private lateinit var mReasonTravelDao : ReasonTravelDao
    private val calendar : Calendar = Calendar.getInstance()
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0
    private var preference = Preference(context)

    fun initTravelDateFrom(){displayCalendar(RequestTravelActivity.chooseDateFrom)}
    fun initTravelDateInto(){
        when{
            stDepartDate.get().isNullOrEmpty() -> requestTravelInterface.onMessage("Please Fill Depart Date first ", ConstantObject.vSnackBarWithButton)
            else -> displayCalendar(RequestTravelActivity.chooseDateInto)
        }
    }

    fun initTravelCheckIn(){
        when{
            stDepartDate.get().isNullOrEmpty() || stReturnDate.get().isNullOrEmpty() -> requestTravelInterface.onMessage("Please Fill " +
                    "Depart Date or Return date first ", ConstantObject.vSnackBarWithButton)
            else -> displayCalendar(RequestTravelActivity.chooseDateCheckIn)
        }
    }
    fun initTravelCheckOut(){
        when{
            stDepartDate.get().isNullOrEmpty() || stReturnDate.get().isNullOrEmpty() -> requestTravelInterface.onMessage("Please Fill " +
                    "Depart Date or Return date first ", ConstantObject.vSnackBarWithButton)
            stTravelCheckIn.get().isNullOrEmpty() -> requestTravelInterface.onMessage("Please Fill Check In Date first ", ConstantObject.vSnackBarWithButton)
            else -> displayCalendar(RequestTravelActivity.chooseDateCheckOut)
        }
    }

    private fun displayCalendar(chooseFrom : String){
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedMonth: String = if (month < 10) { "0" + (month + 1) } else { month.toString() }
                val selectedDay: String = if (dayOfMonth < 10) { "0$dayOfMonth" } else { dayOfMonth.toString() }
                val finalDate = "$year-$selectedMonth-$selectedDay"
                when(chooseFrom){
                    RequestTravelActivity.chooseDateFrom -> validateDateFromTravel(finalDate.trim())
                    RequestTravelActivity.chooseDateInto  -> validateDateInto(finalDate.trim())
                    RequestTravelActivity.chooseDateCheckIn -> validateCheckInCheckOut(RequestTravelActivity.chooseDateCheckIn ,finalDate)
                    else -> validateCheckInCheckOut(RequestTravelActivity.chooseDateCheckOut , finalDate.trim())
                }
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun validateDateFromTravel(selectedDate : String){
        val intDiffDateFrom = DateTimeUtils.getDifferentDate(DateTimeUtils.getCurrentDate(), selectedDate.trim())
        when{
            intDiffDateFrom < 0 -> requestTravelInterface.onMessage("Depart Date should not less than today", ConstantObject.vSnackBarWithButton)
            else -> stDepartDate.set(selectedDate.trim())
        }
    }

    private fun validateDateInto(selectedEndDate : String){
        val intDiffDate = DateTimeUtils.getDifferentDate(stDepartDate.get().toString(), selectedEndDate.trim())
        when{
            intDiffDate < 0 -> requestTravelInterface.onMessage("Return Date should not less then Depart Date  ", ConstantObject.vSnackBarWithButton)
            else -> stReturnDate.set(selectedEndDate.trim())
        }
    }

    private fun validateCheckInCheckOut(selectedFrom : String, selectedDate : String){
        val intDiffDepartDate = DateTimeUtils.getDifferentDate(stDepartDate.get().toString(), selectedDate.trim())
        val intDiffReturnDate = DateTimeUtils.getDifferentDate(stReturnDate.get().toString(), selectedDate.trim())
        when{
            intDiffDepartDate < 0 -> requestTravelInterface.onMessage("Selected date should not less than Depart Date  ", ConstantObject.vSnackBarWithButton)
            intDiffReturnDate > 0 -> requestTravelInterface.onMessage("Selected date should not more than Return Date  ", ConstantObject.vSnackBarWithButton)
            else -> {
                when(selectedFrom){
                    RequestTravelActivity.chooseDateCheckIn -> stTravelCheckIn.set(selectedDate.trim())
                    RequestTravelActivity.chooseDateCheckOut -> stTravelCheckOut.set(selectedDate)
                }
            }
        }
    }

    fun initDataChargeCode(){
        mChargeCodeDao = WcsHrisApps.database.chargeCodeDao()
        doAsync {
            val listTableChargeCode = mChargeCodeDao.getAllChargeCode()
            uiThread { when{listTableChargeCode.isNotEmpty() -> requestTravelInterface.onLoadChargeCode(listTableChargeCode) } }
        }
    }

    fun getDataTransport(){
        mTransTypeDao = WcsHrisApps.database.transTypeDao()
        doAsync {
            val listDataTrans = mTransTypeDao.getAllTransType()
            uiThread { when{listDataTrans.isNotEmpty() -> requestTravelInterface.onLoadTransport(listDataTrans) } }
        }
    }

    fun getDataReason(){
        mReasonTravelDao = WcsHrisApps.database.reasonTravelDao()
        doAsync {
            val getReasonList = mReasonTravelDao.getAllReasonTravel()
            uiThread {
                when{getReasonList.isNotEmpty() -> requestTravelInterface.onLoadReason(getReasonList) }
            }
        }
    }

    fun validateTravelTeam(itemUserId : String, itemName : String, stTeamStatus : String){
        if (itemUserId != "null" && itemName != "null" && stTeamStatus != "null"){
            val itemFriendModel = if(stTeamStatus.contains("Available")){
                FriendModel(itemUserId, itemName, false)
            }else {
                FriendModel(itemUserId, itemName, true)
            }

            listSelectedTeam.add(itemFriendModel)
            requestTravelInterface.onLoadTeam(listSelectedTeam)
        }
    }

    fun addTeamTravel(){
        when{
            isTravelSelected.get() == true -> requestTravelInterface.getTeamData()
            else -> {
                when{
                    !ConnectionObject.isNetworkAvailable(context) -> requestTravelInterface.onAlertReqTravel(context.getString(R.string.alert_no_connection),
                        ConstantObject.vAlertDialogNoConnection, RequestTravelActivity.ALERT_REQ_TRAVEL_NO_CONNECTION)
                    !validateCity() -> requestTravelInterface.onMessage(context.getString(R.string.fill_in_the_blank), ConstantObject.vSnackBarWithButton)
                    else -> saveDestination()
                }
            }
        }
    }

    private fun validateCity() : Boolean{
        return when{
            stDepartFrom.get().equals("") ||
                    stTravelInto.get().equals("")||
                    stTravelCheckIn.get().equals("")||
                    stTravelCheckOut.get().equals("")||
                    stTransTypeCode.get().equals("")||
                    stHotelName.get().equals("") -> false
            else -> true
        }
    }

    private fun saveDestination(){
            val travelDestinationList = mutableListOf<ReqTravelModel>()
            val saveModel = ReqTravelModel(stDepartFrom.get().toString().trim(),
                stTravelInto.get().toString().trim(),
                stDepartDate.get().toString().trim(),
                stReturnDate.get().toString().trim(),
                stTransTypeCode.get().toString().trim(),
                stHotelName.get().toString().trim())
            travelDestinationList.add(saveModel)
            requestTravelInterface.onLoadCitiesTravel(travelDestinationList)
    }

    fun submitSetTravel(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> requestTravelInterface.onAlertReqTravel(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, RequestTravelActivity.ALERT_REQ_TRAVEL_NO_CONNECTION)
            else -> {
                when{
                    !validateTravel() -> requestTravelInterface.onMessage(context.getString(R.string.fill_in_the_blank), ConstantObject.vSnackBarWithButton)
                    else -> requestTravelInterface.onAlertReqTravel(context.getString(R.string.transaction_alert_confirmation),
                        ConstantObject.vAlertDialogConfirmation, RequestTravelActivity.ALERT_REQ_TRAVEL_SET_TRAVEL)
                }
            }
        }
    }

    fun actionSubmitTravel(){
        isProgressReqTravel.set(true)
        requestTravelInterface.onSuccessRequestTravel()
    }

    fun actionSetTravel(){
        isSetTravel.set(true)
        requestTravelInterface.onMessage("Travel successfully set", ConstantObject.vToastSuccess)
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
                stChargeCode.get().equals("")||
                        stReasonCode.get().equals("")||
                        stTravelDescription.get().equals("")||
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

    fun actionEditTravel(listCity : List<ReqTravelModel>){
        when{
            isSetTravel.get() == false -> requestTravelInterface.onMessage("You have not set travel yet", ConstantObject.vToastInfo)
            else -> {
                when{listCity.isNotEmpty() -> requestTravelInterface.onResetCities() }
                isSetTravel.set(false)
                requestTravelInterface.onMessage("Edit Travel Success", ConstantObject.vToastSuccess)
            }
        }
    }

    fun actionGenerateTravel(listCities : List<ReqTravelModel>){
        when{
            isSetTravel.get() == false -> requestTravelInterface.onMessage("please set travel first", ConstantObject.vToastInfo)
            else -> {
                when{listCities.isEmpty() -> requestTravelInterface.onMessage("Please set your destination", ConstantObject.vToastInfo)
                    else -> getGenerateTravel()
                }
            }
        }
    }

    private fun getGenerateTravel(){
        isProgressReqTravel.set(true)
        Handler().postDelayed({
            requestTravelInterface.onSuccessRequestTravel()
        }, 2000)
    }

    fun onBackReqTravelMenu(){
        val intent = Intent(context, MenuActivity::class.java)
        intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_REQUEST)
        context.startActivity(intent)
    }

    private fun initjObjReqTravel(listTeam: List<FriendModel>, listCity: List<ReqTravelModel>) : JSONObject{
        val reqTravelParam  = JSONObject()
        val jObjTravelHeader = JSONObject()
        val jObjTravelDtl = JSONObject()
        val jArrayTravelHeaders = JSONArray()
        val jArrayTravelDtls = JSONArray()


        for(i in listTeam.indices){
            jObjTravelHeader.put("ID",0)
            jObjTravelHeader.put("ID_TR_HEADER",0)
            jObjTravelHeader.put("REASON",0)
            jObjTravelHeader.put("DESCRIPTION",0)
            jObjTravelHeader.put("CHARGE_CD",stChargeCode.get().toString())
            if(isNonTB.get() == false){
                jObjTravelHeader.put("TRAVEL_TYPE_CD","TB")
            }else {
                jObjTravelHeader.put("TRAVEL_TYPE_CD","NTB")
            }

            jObjTravelHeader.put("REQUESTOR_NIK",0)
            jObjTravelHeader.put("DURATION",0)
            jObjTravelHeader.put("DEPART_DATE",0)
            jObjTravelHeader.put("RETURN_DATE",0)
            jObjTravelHeader.put("DOCUMENT_NUMBER",0)
            jObjTravelHeader.put("DOCUMENT_DATE",0)
            jObjTravelHeader.put("TRIP_ADVANCE",0)
            jObjTravelHeader.put("STATUS_CD",0)
            jObjTravelHeader.put("REMARK_REJECTED",0)
            jObjTravelHeader.put("CREATED_BY",0)
            jObjTravelHeader.put("CREATED_DT",0)
            jObjTravelHeader.put("MODIFIED_BY",0)
            jObjTravelHeader.put("MODIFIED_DT",0)
            jObjTravelHeader.put("APPROVED_BY",0)
            jObjTravelHeader.put("APPROVED_DT",0)
            jObjTravelHeader.put("REJECTED_BY",0)
            jObjTravelHeader.put("REJECTED_DT",0)
            jObjTravelHeader.put("ISDELETED",0)
            jObjTravelHeader.put("ISMEMBER_CONFIRM",0)
            jObjTravelHeader.put("ISAPPROVED",0)
            jObjTravelHeader.put("ISMEMBER_REJECTED",0)
            jObjTravelHeader.put("ISREJECTED",0)
            jObjTravelHeader.put("ISCOMPLETED",0)

            jArrayTravelHeaders.put(jObjTravelHeader)
        }

        for(i in listCity.indices){
            jObjTravelDtl.put("ID",0)
            jObjTravelDtl.put("ID_TR_HEADER",0)
            jObjTravelDtl.put("TRANSPORT_TYPE_CODE",0)
            jObjTravelDtl.put("TRANSPORT_NAME",0)
            jObjTravelDtl.put("TRANSPORT_NUMBER",0)
            jObjTravelDtl.put("TRANSPORT_FROM",0)
            jObjTravelDtl.put("TRANSPORT_TO",0)
            jObjTravelDtl.put("TRANSPORT_DATE",0)
            jObjTravelDtl.put("TRANSPORT_TIME",0)
            jObjTravelDtl.put("DESTINATION_FROM",0)
            jObjTravelDtl.put("DESTINATION_TO",0)
            jObjTravelDtl.put("START_DATE",0)
            jObjTravelDtl.put("END_DATE",0)
            jObjTravelDtl.put("DURATION",0)
            jObjTravelDtl.put("ACCOMODATION_NAME",0)
            jObjTravelDtl.put("ACCOMODATION_LOCATION",0)
            jObjTravelDtl.put("CHECK_IN",0)
            jObjTravelDtl.put("CHECK_OUT",0)
            jObjTravelDtl.put("REMARK",0)
            jObjTravelDtl.put("CREATED_BY",0)
            jObjTravelDtl.put("MODIFIED_BY",0)
            jObjTravelDtl.put("MODIFIED_BY",0)
            jObjTravelDtl.put("CREATED_DT",0)
            jObjTravelDtl.put("ISDELETED","False")

            jArrayTravelDtls.put(jObjTravelDtl)
        }

        reqTravelParam.put("TravelRequestHeader",jArrayTravelHeaders)
        reqTravelParam.put("TravelRequestDetail",jArrayTravelDtls)

        return reqTravelParam
    }

    fun getConflictedFriend(selectedNik :String){
        isProgressReqTravel.set(true)
        val listConflictFriend = mutableListOf<ConflictedFriendModel>()
        var conflictedFriendModel : ConflictedFriendModel
        Log.d("###",""+selectedNik)
        apiRepo.searchDataTeam(ConstantObject.extra_fromIntentSearchConflict,
            selectedNik.trim(),
            stDepartDate.get().toString().trim(),
            stReturnDate.get().toString().trim(),
            context, object : ApiRepo.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        val responseConflict = it.getString(ConstantObject.vResponseData)
                        val jArrayConflict = JSONArray(responseConflict)
                        for(i in 0 until jArrayConflict.length()){
                            val jObjConflict = jArrayConflict.getJSONObject(i)
                            conflictedFriendModel = ConflictedFriendModel(
                                jObjConflict.getString("CUSTOMER_NAME"),
                                jObjConflict.getString("SUBJECT"),
                                jObjConflict.getString("CHARGE_CD"),
                                jObjConflict.getString("DATE_FROM"),
                                jObjConflict.getString("DATE_TO")
                            )
                            listConflictFriend.add(conflictedFriendModel)
                        }

                        when(listConflictFriend.size){
                            0 ->{
                                requestTravelInterface.onMessage(context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
                                isProgressReqTravel.set(false)
                            }
                            else ->{
                                isProgressReqTravel.set(false)
                                requestTravelInterface.onShowConflict(listConflictFriend)
                            }
                        }
                    }
                }

                override fun onDataError(error: String?) {
                    isProgressReqTravel.set(false)
                    requestTravelInterface.onMessage(error.toString(), ConstantObject.vToastError)
                }
            })
    }
}