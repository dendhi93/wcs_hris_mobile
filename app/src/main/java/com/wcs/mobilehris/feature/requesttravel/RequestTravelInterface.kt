package com.wcs.mobilehris.feature.requesttravel

import com.wcs.mobilehris.database.entity.ChargeCodeEntity
import com.wcs.mobilehris.database.entity.ReasonTravelEntity
import com.wcs.mobilehris.database.entity.TransportTypeEntity
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.utilinterface.DialogInterface

interface RequestTravelInterface : DialogInterface {
    fun onLoadTeam(listTeam : List<FriendModel>)
    fun onMessage(message : String, messageType : Int)
    fun onAlertReqTravel(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onLoadChargeCode(listChargeCode : List<ChargeCodeEntity>)
    fun onLoadTransport(listTransport : List<TransportTypeEntity>)
    fun onLoadReason(listReason : List<ReasonTravelEntity>)
    fun getTeamData()
    fun getDataDepart()
    fun getDataReturn()
    fun onSuccessRequestTravel()
    fun onHideSoftKeyboard()
    fun onChangeButtonBackground(isTravelSelected : Boolean)
    fun onLoadCitiesTravel(listCity : List<ReqTravelModel>)
    fun onResetCities()
    fun onShowConflict(listConflicted : List<ConflictedFriendModel>)
}