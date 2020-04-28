package com.wcs.mobilehris.feature.benefitclaim.trans

interface BenefitTransactionInterface {
    fun onMessage(message : String, messageType : Int)
    fun onAlertBenefitTrans(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onSelectedSpinnerTransName(selectedTransName : String)
    fun onSelectedSpinnerPerson(selectedPerson : String)
    fun onSelectedSpinnerDiagnose(selectedDiagnose : String)
    fun onSelectedSpinnerCurrency(selectedCurrency : String)
    fun onSuccessAddBenefit()
}