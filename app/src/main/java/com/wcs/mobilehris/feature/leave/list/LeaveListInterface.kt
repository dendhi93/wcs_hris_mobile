package com.wcs.mobilehris.feature.leave.list

import com.wcs.mobilehris.utilinterface.ActionInterface

interface LeaveListInterface : ActionInterface.ShowHideUI{
    fun onLoadLeaveList(leaveList : List<LeaveListModel>, typeLoading : Int)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertLeaveList(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onHideSwipeLeaveList()
}