package com.wcs.mobilehris.feature.login

import com.wcs.mobilehris.utilsinterface.ActionInterface

interface LoginInterface: ActionInterface.showHideUI, ActionInterface {
    fun onSuccessLogin()
    fun onAlertLogin(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onErrorMessage(message : String, messageType : Int)
}