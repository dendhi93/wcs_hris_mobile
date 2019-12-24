package com.wcs.mobilehris.feature.preparation.splash

interface SplashInterface  {
    fun successSplash()
    fun onAlertSplash(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onErrorMessage(message : String, messageType : Int)
}