package com.wcs.mobilehris.feature.benefitclaim.trans

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityBenefitTransBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class BenefitTransActivity : AppCompatActivity(), BenefitTransactionInterface {
    private lateinit var activityBenefitTransBinding: ActivityBenefitTransBinding
    private var intentBenefitFrom = ""
    private var intentBenefitTransType = ""
    private var intentTransType = ""
    private var intentCurrencyDesc = ""
    private var arrCurrencyCode = ArrayList<String>()
    private var arrCurrencyDesc = ArrayList<String>()

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
        activityBenefitTransBinding.viewModel?.stBenefitTransDescription ?.set(intent.getStringExtra(extraTransDescription))
        val transAmount = intent.getStringExtra(extraTransAmount)
        val paidAmount = intent.getStringExtra(extraTransPaidAmount)
        if(paidAmount != ""){ activityBenefitTransBinding.viewModel?.stBenefitPaidAmount?.set(paidAmount.split(" ")[0]) }
        activityBenefitTransBinding.viewModel?.initDataBenefit(intentTransType)
        activityBenefitTransBinding.viewModel?.onInitCurrency()
        if(transAmount != ""){
            activityBenefitTransBinding.viewModel?.stBenefitTransAmount?.set(transAmount.split(" ")[0])
            intentCurrencyDesc = transAmount.split(" ")[1]
            onSelectedSpinnerCurrency(intentCurrencyDesc)
        }
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

//    override fun onSelectedSpinnerTransName(selectedTransName: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onSelectedSpinnerPerson(selectedPerson: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onSelectedSpinnerDiagnose(selectedDiagnose: String) {
//        TODO("Not yet implemented")
//    }
//
    private fun onSelectedSpinnerCurrency(selectedCurrency: String) {
        when{
            arrCurrencyDesc.isNotEmpty() ->{
                for (i in arrCurrencyDesc.indices){
                    when(arrCurrencyDesc[i].trim()){
                        selectedCurrency -> activityBenefitTransBinding.spReqBenefitCurrency.setSelection(i)
                    }
                }
            }
        }
    }

    override fun onSuccessAddBenefit() {
        TODO("Not yet implemented")
    }

    override fun onLoadCurrencySpinner(curList: List<CurrencyModel>) {
        var index  = 0
        for(i in curList.indices){
            when(index){
                0 ->{
                    arrCurrencyCode.add("")
                    arrCurrencyDesc.add("Currency")
                    arrCurrencyCode.add(curList[i].currCode)
                    arrCurrencyDesc.add(curList[i].curDesc)
                }
                else ->{
                    arrCurrencyCode.add(curList[i].currCode)
                    arrCurrencyDesc.add(curList[i].curDesc)
                }
            }
            index += 1
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrCurrencyDesc)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        activityBenefitTransBinding.spReqBenefitCurrency.adapter = adapter
        activityBenefitTransBinding.spReqBenefitCurrency.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               if(position > 0){
                   val code = arrCurrencyCode[position].trim()
                   val descCurrency = arrCurrencyDesc[position].trim()
               }
            }
        }
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
