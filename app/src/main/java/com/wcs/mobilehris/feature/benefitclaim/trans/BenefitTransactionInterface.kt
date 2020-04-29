package com.wcs.mobilehris.feature.benefitclaim.trans

interface BenefitTransactionInterface {
    fun onMessage(message : String, messageType : Int)
    fun onAlertBenefitTrans(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onSuccessAddBenefit(message :String)
    fun onLoadCurrencySpinner(curList : List<CurrencyModel>)
    fun onLoadSpinnerTransName(benfNameList : List<BenefitNameModel>)
    fun onLoadSpinnerPerson(personList : List<BenefitPersonModel>)
    fun onLoadSpinnerDiagnose(diagnoseList : List<DiagnoseModel>)
}