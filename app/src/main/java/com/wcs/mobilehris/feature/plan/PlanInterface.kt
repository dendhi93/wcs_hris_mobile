package com.wcs.mobilehris.feature.plan

import com.wcs.mobilehris.utilsinterface.ActionInterface

interface PlanInterface : ActionInterface.showHideUI{

    fun onLoadList(planList : List<ContentPlanModel>, typeLoading : Int)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertPlan(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
}