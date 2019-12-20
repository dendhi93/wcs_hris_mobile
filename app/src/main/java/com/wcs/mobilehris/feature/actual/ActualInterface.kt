package com.wcs.mobilehris.feature.actual

import com.wcs.mobilehris.feature.plan.ContentTaskModel
import com.wcs.mobilehris.utilinterface.ActionInterface

interface ActualInterface:ActionInterface.showHideUI {
    fun onDisplayList(planList : List<ContentTaskModel>, typeLoading : Int)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertActual(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
}