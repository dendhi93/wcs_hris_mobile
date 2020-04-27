package com.wcs.mobilehris.feature.benefitclaim.list.listheader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityBenefitClaimListBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BenefitClaimListActivity : AppCompatActivity(),
    BenefitListInterface {
    private lateinit var activityBenefitClaimListBinding: ActivityBenefitClaimListBinding
    private lateinit var benefitAdapter : CustomBenefitListAdapter
    private var intentBenefFrom = ""
    private var arrBenefit = ArrayList<BenefitModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBenefitClaimListBinding = DataBindingUtil.setContentView(this,R.layout.activity_benefit_claim_list)
        activityBenefitClaimListBinding.viewModel = BenefitListViewModel(this, this)
        activityBenefitClaimListBinding.rcBenefList.layoutManager = LinearLayoutManager(this)
        activityBenefitClaimListBinding.rcBenefList.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
//            intentBenefFrom = intent.getStringExtra(ConstantObject.extra_intent)
            intentBenefFrom = ConstantObject.extra_fromIntentRequest
            when(intentBenefFrom){
                ConstantObject.extra_fromIntentRequest -> it.title = getString(R.string.req_benefit_hist_activity)
                else -> it.title = getString(R.string.approval_benefit_list_activity)
            }
        }
        benefitAdapter = CustomBenefitListAdapter(this, arrBenefit, intentBenefFrom)
        activityBenefitClaimListBinding.rcBenefList.adapter = benefitAdapter
    }

    override fun onLoadBenefitList(benefitList: List<BenefitModel>, typeLoading: Int) {
        if (arrBenefit.size > 0){arrBenefit.removeAll(benefitList)}
        arrBenefit.clear()
        arrBenefit.addAll(benefitList)
        benefitAdapter.notifyDataSetChanged()

        hideUI(ConstantObject.vGlobalUI)
        showUI(ConstantObject.vRecylerViewUI)
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> hideUI(ConstantObject.vProgresBarUI)
            else -> onHideSwipeBenefitList()
        }
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
        }
    }

    override fun onAlertBenefitList(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_BENEFIT_LIST_NO_CONNECTION -> { MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)}
        }
    }

    override fun onHideSwipeBenefitList() {activityBenefitClaimListBinding.swBenefList.isRefreshing = false}

    override fun hideUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> activityBenefitClaimListBinding.pbBenefList.visibility = View.GONE
            ConstantObject.vRecylerViewUI -> activityBenefitClaimListBinding.rcBenefList.visibility = View.GONE
            ConstantObject.vGlobalUI -> activityBenefitClaimListBinding.tvBenefListEmpty.visibility = View.GONE
        }
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> activityBenefitClaimListBinding.pbBenefList.visibility = View.VISIBLE
            ConstantObject.vRecylerViewUI -> activityBenefitClaimListBinding.rcBenefList.visibility = View.VISIBLE
            ConstantObject.vGlobalUI -> activityBenefitClaimListBinding.tvBenefListEmpty.visibility = View.VISIBLE
        }
    }

    companion object{
        const val ALERT_BENEFIT_LIST_NO_CONNECTION = 1
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activityBenefitClaimListBinding.viewModel?.onBackBenefitListMenu(intentBenefFrom)
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        activityBenefitClaimListBinding.viewModel?.onBackBenefitListMenu(intentBenefFrom)
        finish()
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        activityBenefitClaimListBinding.viewModel?.validateDataBenefit(ConstantObject.vLoadWithProgressBar, intentBenefFrom.trim())
        activityBenefitClaimListBinding.swBenefList.setOnRefreshListener {
            activityBenefitClaimListBinding.viewModel?.validateDataBenefit(ConstantObject.vLoadWithoutProgressBar, intentBenefFrom.trim())
        }
    }
}
