package com.wcs.mobilehris.feature.leave.transaction

import com.wcs.mobilehris.database.entity.ReasonLeaveEntity
import com.wcs.mobilehris.utilinterface.ActionInterface

interface LeaveTransInterface : ActionInterface {
    fun onMessage(message : String, messageType : Int)
    fun onAlertLeaveTrans(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onSuccessDtlTravel(message : String)
    fun onLoadReasonLeave(listLeave : List<ReasonLeaveEntity>)
    fun onSelectedSpinner(selectedReason : String)
}