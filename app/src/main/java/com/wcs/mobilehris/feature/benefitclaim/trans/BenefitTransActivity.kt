package com.wcs.mobilehris.feature.benefitclaim.trans

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityBenefitTransBinding

class BenefitTransActivity : AppCompatActivity(), BenefitTransactionInterface {
    private lateinit var activityBenefitTransBinding: ActivityBenefitTransBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBenefitTransBinding = DataBindingUtil.setContentView(this, R.layout.activity_benefit_trans)
        activityBenefitTransBinding.viewModel = BenefitTransViewModel(this, this)
    }

    override fun onMessage(message: String, messageType: Int) {
        TODO("Not yet implemented")
    }

    override fun onAlertBenefitTrans(
        alertMessage: String,
        alertTitle: String,
        intTypeActionAlert: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onSelectedSpinnerTransName(selectedTransName: String) {
        TODO("Not yet implemented")
    }

    override fun onSelectedSpinnerPerson(selectedPerson: String) {
        TODO("Not yet implemented")
    }

    override fun onSelectedSpinnerDiagnose(selectedDiagnose: String) {
        TODO("Not yet implemented")
    }

    override fun onSelectedSpinnerCurrency(selectedCurrency: String) {
        TODO("Not yet implemented")
    }

    override fun onSuccessAddBenefit() {
        TODO("Not yet implemented")
    }
}
