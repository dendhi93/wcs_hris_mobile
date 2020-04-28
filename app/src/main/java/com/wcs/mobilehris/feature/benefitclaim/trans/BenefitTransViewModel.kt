package com.wcs.mobilehris.feature.benefitclaim.trans

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class BenefitTransViewModel(private val context: Context,
                            private val benefitTransactionInterface: BenefitTransactionInterface):ViewModel(){
    val isVisibleBenefitTransProgress = ObservableField(false)
    val isVisibleBenefitButton = ObservableField(false)
    val stBenefitDate = ObservableField("")
    val stBenefitTransName = ObservableField("")
    val stBenefitTransPerson = ObservableField("")
    val stBenefitTransDiagnose = ObservableField("")
    val intBenefitTransAmount = ObservableField(0)
    val intBenefitPaidAmount = ObservableField(0)
    val stBenefitTransDescription = ObservableField("")

    fun onClickAddBenefit(){}
}