package com.wcs.mobilehris.feature.approvallistofactivities

import com.wcs.mobilehris.utilinterface.ActionInterface

interface AppActionListInterface : ActionInterface.ShowHideUI {
    fun onLoadApprovalListOfActivities(appActionListOfActivities : List<AppActionListModel>, typeLoading : Int)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertAppActivitiesList (alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onHideSwipeActivitiesList()
}