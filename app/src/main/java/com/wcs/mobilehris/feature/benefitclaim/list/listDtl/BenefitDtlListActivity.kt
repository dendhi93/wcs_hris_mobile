package com.wcs.mobilehris.feature.benefitclaim.list.listDtl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityBenefitDtlBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BenefitDtlListActivity : AppCompatActivity(), BenefitDtlInterface {
    private lateinit var activityBenefitDtlBinding: ActivityBenefitDtlBinding
    private lateinit var benefitDtlAdapter : CustomBenefitDtlAdapter
    private var intentBenefDtlFrom = ""
    private var intentBenefDtlRequestor = ""
    private var intentBenefDtlTypeTrans = ""
    private var arrBenefitDtl = ArrayList<BenefitDtlModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBenefitDtlBinding = DataBindingUtil.setContentView(this, R.layout.activity_benefit_dtl)
        activityBenefitDtlBinding.viewModel = BenefitDtlListViewModel(this, this)
        activityBenefitDtlBinding.rcBenefDtlList.layoutManager = LinearLayoutManager(this)
        activityBenefitDtlBinding.rcBenefDtlList.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()
        intentBenefDtlFrom = intent.getStringExtra(ConstantObject.extra_intent)
        activityBenefitDtlBinding.viewModel?.stBenefitFrom?.set(intentBenefDtlFrom.trim())
        intentBenefDtlRequestor = intent.getStringExtra(extraBenefitRequestor)
        activityBenefitDtlBinding.viewModel?.stBenefitDocNo?.set(intent.getStringExtra(extraBenefitDocNo))
        intentBenefDtlTypeTrans = intent.getStringExtra(extraBenefitTransType)
        activityBenefitDtlBinding.viewModel?.isVisibleRecyler?.set(false)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
            when(intentBenefDtlFrom){
                ConstantObject.extra_fromIntentRequest -> {
                    if(activityBenefitDtlBinding.viewModel?.stBenefitDocNo?.get().toString().trim() == ""){
                        it.title = getString(R.string.req_benefit_dtl_activity)
                    }else{it.title = getString(R.string.req_benefit_hist_activity)}
                }
                else -> it.title = intentBenefDtlRequestor.trim()
            }
        }
        benefitDtlAdapter = CustomBenefitDtlAdapter(this, arrBenefitDtl, intentBenefDtlFrom,intentBenefDtlTypeTrans)
        activityBenefitDtlBinding.rcBenefDtlList.adapter = benefitDtlAdapter
        activityBenefitDtlBinding.viewModel?.validateDataBenefitDtl(intentBenefDtlTypeTrans, intentBenefDtlFrom)
    }

    override fun onLoadBenefitDtlList(benefitDtlList: List<BenefitDtlModel>) {
        arrBenefitDtl.clear()
        arrBenefitDtl.addAll(benefitDtlList)
        benefitDtlAdapter.notifyDataSetChanged()
        activityBenefitDtlBinding.viewModel?.isVisibleBenefitDtlProgress?.set(false)
        activityBenefitDtlBinding.viewModel?.isVisibleRecyler?.set(true)
    }

    override fun onBenefitDtlMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
        }
    }

    override fun onAlertBenefitList(alertMessage: String,alertTitle: String,intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_BENEFITDTL_LIST_NO_CONNECTION -> { MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)}
        }
    }

    companion object{
        const val extraBenefitDocNo = "extra_benefit_doc_no"
        const val extraBenefitRequestor = "extra_benefit_requestor_name"
        const val extraBenefitTransType = "extra_benefit_trans_type"
        const val ALERT_BENEFITDTL_LIST_NO_CONNECTION = 1
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
}
