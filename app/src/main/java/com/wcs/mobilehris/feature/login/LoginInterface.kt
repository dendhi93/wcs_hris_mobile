package com.wcs.mobilehris.feature.login

interface LoginInterface {
    fun onSuccessLogin()
    fun onAlertLogin(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onErrorMessage(message : String, messageType : Int)
}