package com.wcs.mobilehris.feature.dashboard

import com.wcs.mobilehris.utilsinterface.ActionInterface

interface DashboardInterface :  ActionInterface.showHideUI {
    fun onLoadList(vListDash : List<DashboardModel>, typeLoading : Int)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertDash(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
}