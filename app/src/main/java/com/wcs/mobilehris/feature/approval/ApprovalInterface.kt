package com.wcs.mobilehris.feature.approval

import com.wcs.mobilehris.utilinterface.ActionInterface

interface ApprovalInterface {
    fun loadApprovalMenu(approvalMenuList: List<ApprovalModel>)
    fun onAlertApproval(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onMessage(message : String, messageType : Int)
}