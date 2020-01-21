package com.wcs.mobilehris.feature.team

import com.wcs.mobilehris.utilinterface.ActionInterface

interface TeamInterface: ActionInterface.ShowHideUI {
    fun onLoadTeam(teamList : List<TeamModel>, typeLoading : Int)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertTeam(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onHideSwipeRefresh()
}