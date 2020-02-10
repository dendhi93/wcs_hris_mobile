package com.wcs.mobilehris.feature.dtlreqtravel

import com.wcs.mobilehris.database.entity.TransportTypeEntity
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.utilinterface.DialogInterface

interface DtlTravelInterface : DialogInterface {
    fun onLoadTeam(listTeam : List<FriendModel>)
    fun onMessage(message : String, messageType : Int)
    fun onAlertDtlReqTravel(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onLoadTransport(listTransport : List<TransportTypeEntity>)
    fun onSuccessDtlTravel()
    fun getCityDepart()
    fun getCityReturn()
    fun selectedTravelWayRadio(booleanTravelWay: Boolean?)
    fun selectedSpinner(selectedTransType : String)
}