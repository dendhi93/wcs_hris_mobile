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
    private var intentTransType = ""
    private var intentCurrencyDesc = ""
    private var arrCurrencyCode = ArrayList<String>()
    private var arrCurrencyDesc = ArrayList<String>()
    private var arrBenefNameCode = ArrayList<String>()
    private var arrBenefNameDesc = ArrayList<String>()
    private var arrPersonCode = ArrayList<String>()
    private var arrPersonName = ArrayList<String>()
    private var arrDiagnoseCode = ArrayList<String>()
    private var arrDiagnoseName = ArrayList<String>()

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
        activityBenefitTransBinding.viewModel?.onInitCurrency()
        activityBenefitTransBinding.viewModel?.initBenefitName()
        activityBenefitTransBinding.viewModel?.initPerson()
        activityBenefitTransBinding.viewModel?.initDiagnose()
        intentBenefit()
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

    private fun onSelectedSpinnerTransName(selectedTransname: String) {
        when{
            arrBenefNameDesc.isNotEmpty() ->{
                for (i in arrBenefNameDesc.indices){
                    when(arrBenefNameDesc[i].trim().split(" (")[0]){
                        selectedTransname -> activityBenefitTransBinding.spReqBenefitTransName.setSelection(i)
                    }
                }
            }
        }
    }

    private fun onSelectedSpinnerPerson(selectedPerson: String) {
        when{
            arrPersonName.isNotEmpty() ->{
                for (i in arrPersonName.indices){
                    when(arrPersonName[i].trim().split(" - ")[0]){
                        selectedPerson -> activityBenefitTransBinding.spReqBenefitPerson.setSelection(i)
                    }
                }
            }
        }
    }

    private fun onSelectedSpinnerDiagnose(selectedDiagnose: String) {
        when{
            arrDiagnoseName.isNotEmpty() ->{
                for (i in arrDiagnoseName.indices){
                    when(arrDiagnoseName[i].trim()){
                        selectedDiagnose -> activityBenefitTransBinding.spReqBenefitDiagnose.setSelection(i)
                    }
                }
            }
        }
    }

    private fun intentBenefit(){
        intentBenefitFrom = intent.getStringExtra(ConstantObject.extra_intent)
        intentTransType = intent.getStringExtra(extraTransType)
        activityBenefitTransBinding.viewModel?.stBenefitTransType?.set(intent.getStringExtra(extraBenefitTransType))
        activityBenefitTransBinding.viewModel?.stBenefitDocNoTrans?.set(intent.getStringExtra(extraBenefDocNo))
        activityBenefitTransBinding.viewModel?.stBenefitDate?.set(intent.getStringExtra(extraBenefitTransDate))
        activityBenefitTransBinding.viewModel?.stBenefitTransName?.set(intent.getStringExtra(extraBenefitTransName))
        activityBenefitTransBinding.viewModel?.stBenefitTransPerson?.set(intent.getStringExtra(extraBenefitTransPerson))
        activityBenefitTransBinding.viewModel?.stBenefitTransDiagnose?.set(intent.getStringExtra(extraBenefitTransDiagnose))
        activityBenefitTransBinding.viewModel?.stBenefitTransDescription ?.set(intent.getStringExtra(extraTransDescription))
        activityBenefitTransBinding.viewModel?.stBenefitTransId?.set(intent.getStringExtra(extraBenefitId))

        onSelectedSpinnerTransName(activityBenefitTransBinding.viewModel?.stBenefitTransName?.get().toString().trim())
        onSelectedSpinnerPerson(activityBenefitTransBinding.viewModel?.stBenefitTransPerson?.get().toString().trim())
        onSelectedSpinnerDiagnose(activityBenefitTransBinding.viewModel?.stBenefitTransDiagnose?.get().toString().trim())
        val paidAmount = intent.getStringExtra(extraTransPaidAmount)
        if(paidAmount != ""){ activityBenefitTransBinding.viewModel?.stBenefitPaidAmount?.set(paidAmount.split(" ")[0]) }
        val transAmount = intent.getStringExtra(extraTransAmount)
        if(transAmount != ""){
            activityBenefitTransBinding.viewModel?.stBenefitTransAmount?.set(transAmount.split(" ")[0])
            intentCurrencyDesc = transAmount.split(" ")[1]
            onSelectedSpinnerCurrency(intentCurrencyDesc)
        }
        activityBenefitTransBinding.viewModel?.initDataBenefit(intentTransType)
    }
    override fun onSuccessAddBenefit(message :String) {
        onMessage(message, ConstantObject.vToastSuccess)
        activityBenefitTransBinding.viewModel?.isVisibleBenefitTransProgress?.set(false)
        finish()
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
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               if(position > 0){
                   if(activityBenefitTransBinding.viewModel?.stBenefitTransAmount?.get().toString() == ""){
                       onMessage("Please fill amount claim first", ConstantObject.vSnackBarWithButton)
                       activityBenefitTransBinding.spReqBenefitCurrency.setSelection(0)
                   }else{
//                       val code = arrCurrencyCode[position].trim()
                       val descCurrency = arrCurrencyDesc[position].trim()
                       activityBenefitTransBinding.viewModel?.
                       stBenefitPaidAmount?.set(activityBenefitTransBinding.viewModel?.stBenefitTransAmount?.get().toString()+ " "+descCurrency.trim())
                   }
               }
            }
        }
    }

    override fun onLoadSpinnerTransName(benfNameList: List<BenefitNameModel>) {
        var index  = 0
        for(i in benfNameList.indices){
            when(index){
                0 ->{
                    arrBenefNameCode.add("")
                    arrBenefNameDesc.add("Transaction Name")
                    arrBenefNameCode.add(benfNameList[i].benefitCode)
                    if(benfNameList[i].benefitHeader != ""){ arrBenefNameDesc.add(benfNameList[i].benefitDesc +" ("+benfNameList[i].benefitHeader+")")
                    }else{arrBenefNameDesc.add(benfNameList[i].benefitDesc)}
                }
                else ->{
                    arrBenefNameCode.add(benfNameList[i].benefitCode)
                    if(benfNameList[i].benefitHeader != ""){ arrBenefNameDesc.add(benfNameList[i].benefitDesc +" ("+benfNameList[i].benefitHeader+")")
                    }else{arrBenefNameDesc.add(benfNameList[i].benefitDesc)}
                }
            }
            index += 1
        }
        val adapterBenefitName = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrBenefNameDesc)
        adapterBenefitName.setDropDownViewResource(android.R.layout.simple_spinner_item)
        activityBenefitTransBinding.spReqBenefitTransName.adapter = adapterBenefitName
        activityBenefitTransBinding.spReqBenefitTransName.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position > 0){
                    val nameCode = arrBenefNameCode[position].trim()
                    val benefdesc = arrBenefNameDesc[position].trim().split(" (")[0]
                    activityBenefitTransBinding.viewModel?.stBenefitTransName?.set(benefdesc.trim())
                }
            }
        }
    }

    override fun onLoadSpinnerPerson(personList: List<BenefitPersonModel>) {
        var index  = 0
        for(i in personList.indices){
            when(index){
                0 ->{
                    arrPersonCode.add("")
                    arrPersonName.add("Accept By")
                    arrPersonCode.add(personList[i].personId)
                    arrPersonName.add(personList[i].personName + " - " + personList[i].personRelation)
                }
                else ->{
                    arrPersonCode.add(personList[i].personId)
                    arrPersonName.add(personList[i].personName + " - " + personList[i].personRelation)
                }
            }
            index += 1
        }
        val adapterPerson = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrPersonName)
        adapterPerson.setDropDownViewResource(android.R.layout.simple_spinner_item)
        activityBenefitTransBinding.spReqBenefitPerson.adapter = adapterPerson
        activityBenefitTransBinding.spReqBenefitPerson.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position > 0){
                    val personCode = arrPersonCode[position].trim()
                    val personName = arrPersonName[position].trim().split(" - ")[0]
                    activityBenefitTransBinding.viewModel?.stBenefitTransPerson?.set(personName.trim())
                }
            }
        }
    }

    override fun onLoadSpinnerDiagnose(diagnoseList: List<DiagnoseModel>) {
        var index  = 0
        for(i in diagnoseList.indices){
            when(index){
                0 ->{
                    arrDiagnoseCode.add("")
                    arrDiagnoseName.add("Diagnose")
                    arrDiagnoseCode.add(diagnoseList[i].diagnoseCode)
                    arrDiagnoseName.add(diagnoseList[i].diagnoseDesc)
                }
                else ->{
                    arrDiagnoseCode.add(diagnoseList[i].diagnoseCode)
                    arrDiagnoseName.add(diagnoseList[i].diagnoseDesc)
                }
            }
            index += 1
        }
        val adapterDiagnose = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrDiagnoseName)
        adapterDiagnose.setDropDownViewResource(android.R.layout.simple_spinner_item)
        activityBenefitTransBinding.spReqBenefitDiagnose.adapter = adapterDiagnose
        activityBenefitTransBinding.spReqBenefitDiagnose.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position > 0){
                    val diagnoseCode = arrDiagnoseCode[position].trim()
                    val diagnoseDesc = arrDiagnoseName[position].trim()
                    activityBenefitTransBinding.viewModel?.stBenefitTransDiagnose?.set(diagnoseDesc)
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
        const val extraBenefitId = "benefit_id"
        const val extraValueTransDtlType = "trans_detail"
        const val extraValueTransEditType = "trans_edit"
        const val extraBenefDocNo = "doc_no"
    }
}
