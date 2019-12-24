package com.wcs.mobilehris.feature.completed

import com.wcs.mobilehris.feature.plan.ContentTaskModel
import com.wcs.mobilehris.utilinterface.ActionInterface

interface CompletedInterface : ActionInterface.ShowHideUI {
    fun onDisplayCompletedList(completedList : List<ContentTaskModel>, typeLoading : Int)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertCompleted(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
}