package com.wcs.mobilehris.feature.confirmtask

interface ConfirmTaskInterface {

    fun onAlertMessage(message : String, messageType : Int)
    fun onAlertConfirmTask(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onSuccessConfirm()
}