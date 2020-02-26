package com.wcs.mobilehris.feature.confirmation

import com.wcs.mobilehris.utilinterface.ActionInterface

interface ConfirmationInterface : ActionInterface.ShowHideUI {
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertConf(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun hideConfirmationSwipe()
    fun onLoadConfirmationMenu(listConf : List<ConfirmationModel>, typeLoading : Int)
}