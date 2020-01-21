package com.wcs.mobilehris.feature.requesttravellist

import com.wcs.mobilehris.utilinterface.ActionInterface

interface ReqTravelListInterface : ActionInterface.ShowHideUI {
    fun onLoadTravelList(reqTravList : List<TravelListModel>, typeLoading : Int)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertReqTravelList(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onHideSwipeTravelList()
}