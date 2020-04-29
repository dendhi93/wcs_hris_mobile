package com.wcs.mobilehris.feature.benefitclaim.list.listDtl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.database.daos.BenefitDtlDao
import com.wcs.mobilehris.databinding.ActivityBenefitDtlBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import com.wcs.mobilehris.utilinterface.CustomBottomSheetInterface
import com.wcs.mobilehris.utilinterface.DialogInterface
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BenefitDtlListActivity : AppCompatActivity(), BenefitDtlInterface,
    DialogInterface,
    CustomBottomSheetInterface,
    SelectedBenefInterface{
    private lateinit var activityBenefitDtlBinding: ActivityBenefitDtlBinding
    private lateinit var benefitDtlAdapter : CustomBenefitDtlAdapter
    private var intentBenefDtlFrom = ""
    private var intentBenefDtlRequestor = ""
    private var intentBenefDtlTypeTrans = ""
    private var arrBenefitDtl = ArrayList<BenefitDtlModel>()
    private var keyDialogActive = 0
    private lateinit var benefitDtlDao: BenefitDtlDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBenefitDtlBinding = DataBindingUtil.setContentView(this, R.layout.activity_benefit_dtl)
        activityBenefitDtlBinding.viewModel = BenefitDtlListViewModel(this, this)
        activityBenefitDtlBinding.rcBenefDtlList.layoutManager = LinearLayoutManager(this)
        activityBenefitDtlBinding.rcBenefDtlList.setHasFixedSize(true)
        benefitDtlDao = WcsHrisApps.database.benefitDtlDao()
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
        benefitDtlAdapter.initSelectedBenefCallback(this)
        activityBenefitDtlBinding.rcBenefDtlList.adapter = benefitDtlAdapter
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
            ConstantObject.vToastSuccess -> MessageUtils.toastMessage(this, message, ConstantObject.vToastSuccess)
            else -> MessageUtils.snackBarMessage(message,this, ConstantObject.vSnackBarWithButton)
        }
    }

    override fun onAlertBenefitList(alertMessage: String,alertTitle: String,intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_BENEFITDTL_LIST_NO_CONNECTION -> MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)
            ALERT_BENEFITDTL_LIST_APPROVED ->{
                keyDialogActive = ALERT_BENEFITDTL_LIST_APPROVED
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_BENEFITDTL_LIST_REJECT ->{
                keyDialogActive = ALERT_BENEFITDTL_LIST_REJECT
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
        }
    }

    override fun onSuccessBenefit(message: String) {
        onBenefitDtlMessage(message, ConstantObject.vToastSuccess)
        activityBenefitDtlBinding.viewModel?.isVisibleBenefitDtlProgress?.set(false)
        finish()
    }

    companion object{
        const val extraBenefitDocNo = "extra_benefit_doc_no"
        const val extraBenefitRequestor = "extra_benefit_requestor_name"
        const val extraBenefitTransType = "extra_benefit_trans_type"
        const val ALERT_BENEFITDTL_LIST_NO_CONNECTION = 1
        const val ALERT_BENEFITDTL_LIST_APPROVED = 3
        const val ALERT_BENEFITDTL_LIST_REJECT = 5
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        val inflater = menuInflater
        if(intentBenefDtlFrom == ConstantObject.extra_fromIntentRequest){inflater.inflate(R.menu.menu_benefit_dtl_save, menu)}
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                doAsync {
                    benefitDtlDao.deleteAlltBenefitDtl()
                    uiThread { finish() }
                }
                return true
            }
            R.id.mnu_custom_benefit_list_save ->{
                activityBenefitDtlBinding.viewModel?.onPublishBenefit()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPositiveClick(o: Any) {
        when(keyDialogActive){
            ALERT_BENEFITDTL_LIST_APPROVED ->activityBenefitDtlBinding.viewModel?.onProcessBenefit(ALERT_BENEFITDTL_LIST_APPROVED)
            ALERT_BENEFITDTL_LIST_REJECT -> CustomBottomSheetDialogFragment(this).show(supportFragmentManager, "Dialog")
        }
    }

    override fun onNegativeClick(o: Any) {}

    class CustomBottomSheetDialogFragment(private val callback: CustomBottomSheetInterface) : BottomSheetDialogFragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            val v: View = inflater.inflate(R.layout.custom_bottom_sheet_reject, container, false)
            val btnSubmitReject = v.findViewById<View>(R.id.btn_customBottomSheet_reject) as Button
            val txtNotes = v.findViewById<View>(R.id.txt_customBottomSheet_notes) as TextInputEditText
            btnSubmitReject.setOnClickListener{
                when {
                    txtNotes.text.toString().trim() == "" -> MessageUtils.toastMessage(requireContext(),
                        requireContext().getString(R.string.fill_in_the_blank), ConstantObject.vToastInfo)
                    else -> callback.onRejectTransaction(txtNotes.text.toString().trim())
                }
            }
            return v
        }
    }

    override fun onRejectTransaction(notesReject: String) {
        activityBenefitDtlBinding.viewModel?.stBenefitRejectNotes?.set(notesReject.trim())
        activityBenefitDtlBinding.viewModel?.onProcessBenefit(ALERT_BENEFITDTL_LIST_REJECT)
    }

    override fun onResume() {
        super.onResume()
        activityBenefitDtlBinding.viewModel?.validateDataBenefitDtl(intentBenefDtlTypeTrans, intentBenefDtlFrom)
    }

    override fun onBackPressed() {
        doAsync {
            benefitDtlDao.deleteAlltBenefitDtl()
            uiThread {
                super.onBackPressed()
                finish()
            }
        }

    }

    override fun selectedItemBenefit(benefitDtlModel: BenefitDtlModel) {
        doAsync {
            benefitDtlDao.deleteBenefitById(benefitDtlModel.benefitDtlId.toInt())
            uiThread {
                onBenefitDtlMessage("Success Deleted", ConstantObject.vToastSuccess)
                activityBenefitDtlBinding.viewModel?.validateDataBenefitDtl(intentBenefDtlTypeTrans, intentBenefDtlFrom)
            }
        }
    }
}
