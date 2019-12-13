package com.wcs.mobilehris.feature.preparation.splash

import com.wcs.mobilehris.utilsinterface.ActionInterface

interface SplashInterface : ActionInterface.showHideUI {
    fun successSplash()
    fun onAlertSplash(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onErrorMessage(message : String, messageType : Int)
}