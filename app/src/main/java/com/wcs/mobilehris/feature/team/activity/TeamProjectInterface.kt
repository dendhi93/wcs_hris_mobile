package com.wcs.mobilehris.feature.team.activity


interface TeamProjectInterface {
    fun onLoadTeamProject(teamList : List<TeamProjectModel>)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertTeamProject(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun clearList()
}