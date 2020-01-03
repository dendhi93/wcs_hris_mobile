package com.wcs.mobilehris.feature.plan

import com.wcs.mobilehris.utilinterface.ActionInterface

interface PlanInterface : ActionInterface.ShowHideUI{

    fun onLoadList(planList : List<ContentTaskModel>, typeLoading : Int)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertPlan(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onHideSwipeRefresh()
}