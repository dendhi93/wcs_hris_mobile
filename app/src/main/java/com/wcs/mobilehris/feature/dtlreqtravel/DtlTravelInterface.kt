package com.wcs.mobilehris.feature.dtlreqtravel

import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.requesttravel.ReqTravelModel
import com.wcs.mobilehris.utilinterface.DialogInterface

interface DtlTravelInterface : DialogInterface {
    fun onLoadTeam(listTeam : List<FriendModel>)
    fun onMessage(message : String, messageType : Int)
    fun onAlertDtlReqTravel(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onLoadCitiesTravel(listCities : List<ReqTravelModel>)
    fun onSuccessDtlTravel(message : String)
    fun selectedTravelWayRadio(booleanTravelWay: Boolean?)
    fun onChangeButtonBackground(booleanCityView : Boolean?)
}