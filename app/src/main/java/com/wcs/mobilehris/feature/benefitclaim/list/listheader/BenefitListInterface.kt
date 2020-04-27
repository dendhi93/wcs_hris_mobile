package com.wcs.mobilehris.feature.benefitclaim.list.listheader

import com.wcs.mobilehris.utilinterface.ActionInterface

interface BenefitListInterface : ActionInterface.ShowHideUI {
    fun onLoadBenefitList(benefitList : List<BenefitModel>, typeLoading : Int)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertBenefitList(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onHideSwipeBenefitList()
}