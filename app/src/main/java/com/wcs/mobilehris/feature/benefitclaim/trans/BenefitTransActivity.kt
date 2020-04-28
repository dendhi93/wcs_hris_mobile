package com.wcs.mobilehris.feature.benefitclaim.trans

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityBenefitTransBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BenefitTransActivity : AppCompatActivity(), BenefitTransactionInterface {
    private lateinit var activityBenefitTransBinding: ActivityBenefitTransBinding
    private var intentBenefitFrom = ""
    private var intentBenefitTransType = ""
    private var intentTransType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBenefitTransBinding = DataBindingUtil.setContentView(this, R.layout.activity_benefit_trans)
        activityBenefitTransBinding.viewModel = BenefitTransViewModel(this, this)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
        }
        intentBenefitFrom = intent.getStringExtra(ConstantObject.extra_intent)
        intentBenefitTransType = intent.getStringExtra(extraBenefitTransType)
        intentTransType = intent.getStringExtra(extraTransType)
        activityBenefitTransBinding.viewModel?.stBenefitDate?.set(intent.getStringExtra(extraBenefitTransDate))
        activityBenefitTransBinding.viewModel?.stBenefitTransName?.set(intent.getStringExtra(extraBenefitTransName))
        activityBenefitTransBinding.viewModel?.stBenefitTransPerson?.set(intent.getStringExtra(extraBenefitTransPerson))
        activityBenefitTransBinding.viewModel?.stBenefitTransDiagnose?.set(intent.getStringExtra(extraBenefitTransDiagnose))
        activityBenefitTransBinding.viewModel?.stBenefitTransAmount?.set(intent.getStringExtra(extraTransAmount))
        activityBenefitTransBinding.viewModel?.stBenefitPaidAmount?.set(intent.getStringExtra(extraTransPaidAmount))
        activityBenefitTransBinding.viewModel?.stBenefitTransDescription ?.set(intent.getStringExtra(extraTransDescription))
        activityBenefitTransBinding.viewModel?.initDataBenefit(intentTransType)
    }

    override fun onMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
            ConstantObject.vToastSuccess -> MessageUtils.toastMessage(this, message, ConstantObject.vToastSuccess)
            else -> MessageUtils.snackBarMessage(message,this, ConstantObject.vSnackBarWithButton)
        }
    }

    override fun onAlertBenefitTrans(alertMessage: String,alertTitle: String,intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_BENEFIT_TRANS_NO_CONNECTION -> MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object{
        const val ALERT_BENEFIT_TRANS_NO_CONNECTION = 1
        const val ALERT_BENEFIT_SAVE = 3
        const val extraBenefitTransDate = "trans_benefit_date"
        const val extraBenefitTransName = "trans_benefit_name"
        const val extraBenefitTransType = "trans_benefit_type"
        const val extraBenefitTransPerson= "trans_benefit_person"
        const val extraBenefitTransDiagnose = "trans_benefit_diagnose"
        const val extraTransAmount = "trans_benefit_amount"
        const val extraTransPaidAmount = "trans_benefit_paid_amount"
        const val extraTransDescription = "trans_benefit_description"
        const val extraTransType = "trans_type"
        const val extraValueTransDtlType = "trans_detail"
        const val extraValueTransEditType = "trans_edit"
    }
}
