package com.wcs.mobilehris.feature.dashboard

import com.wcs.mobilehris.utilinterface.ActionInterface

interface DashboardInterface :  ActionInterface.ShowHideUI {
    fun onLoadList(vListDash : List<DashboardModel>, typeLoading : Int)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertDash(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun hideSwipeRefreshLayout()
}