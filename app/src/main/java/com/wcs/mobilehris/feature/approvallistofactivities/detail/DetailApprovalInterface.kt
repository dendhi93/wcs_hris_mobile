package com.wcs.mobilehris.feature.approvallistofactivities.detail

interface DetailApprovalInterface {
    fun onMessage(message : String, messageType : Int)
    fun onAlertApproval(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onSuccessApprovalTrans(message : String)
}