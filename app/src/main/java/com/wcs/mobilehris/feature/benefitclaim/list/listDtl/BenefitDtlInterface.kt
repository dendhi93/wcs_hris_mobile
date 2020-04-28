package com.wcs.mobilehris.feature.benefitclaim.list.listDtl

interface BenefitDtlInterface {
    fun onLoadBenefitDtlList(benefitDtlList : List<BenefitDtlModel>)
    fun onBenefitDtlMessage(message : String, messageType : Int)
    fun onAlertBenefitList(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onSuccessBenefit(message : String)
}