package com.wcs.mobilehris.feature.city

import com.wcs.mobilehris.utilinterface.ActionInterface

interface CityInterface : ActionInterface.ShowHideUI  {
    fun onLoadCity(cityList : List<CityModel>, typeLoading : Int)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertCity(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onHideSwipeRefreshCity()
}