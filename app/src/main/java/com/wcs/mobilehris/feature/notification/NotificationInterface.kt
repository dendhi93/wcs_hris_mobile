package com.wcs.mobilehris.feature.notification

interface NotificationInterface {
    fun onLoadNotif(vListDash : List<NotificationModel>)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertNotif(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
}