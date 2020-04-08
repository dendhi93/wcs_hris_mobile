package com.wcs.mobilehris.feature.team.fragment

import com.wcs.mobilehris.utilinterface.ActionInterface

interface TeamInterface: ActionInterface.ShowHideUI {
    fun onLoadTeam(teamList : List<TeamModel>)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertTeam(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onClearTeamList()
}