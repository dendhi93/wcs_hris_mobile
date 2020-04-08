package com.wcs.mobilehris.feature.approvallistofactivities.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.databinding.ActivityApprovalActivityDetailBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import com.wcs.mobilehris.utilinterface.CustomBottomSheetInterface

import com.wcs.mobilehris.utilinterface.DialogInterface

class DetailApprovalActivity : AppCompatActivity(), DetailApprovalInterface, DialogInterface, CustomBottomSheetInterface {
    private lateinit var detailApprovalActivityBinding : ActivityApprovalActivityDetailBinding
    private lateinit var intentDocumentNo : String
    private lateinit var activityId : String
    private var onKeyAlertActive = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailApprovalActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_approval_activity_detail)
        detailApprovalActivityBinding.viewModel = DetailApprovalViewModel(this, this, ApiRepo())
    }

    override fun onStart() {
        super.onStart()

        intentDocumentNo = intent.getStringExtra(extraDocumentNumber)
        activityId = intent.getStringExtra(extraActivityId)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
            it.title = valueDetailApproval
        }

        detailApprovalActivityBinding.viewModel?.onInitApprovalData(activityId)
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

    override fun onMessage(message: String, messageType: Int) {
        when(messageType) {
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
            ConstantObject.vToastSuccess -> MessageUtils.toastMessage(this, message, ConstantObject.vToastSuccess)
            else -> MessageUtils.snackBarMessage(message,this, ConstantObject.vSnackBarWithButton)
        }
    }

    override fun onAlertApproval(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert) {
            ALERT_APPROVAL_TRANS_NO_CONNECTION -> MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)
            ALERT_APPROVAL_TRANS_APPROVE -> {
                onKeyAlertActive = ALERT_APPROVAL_TRANS_APPROVE
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_APPROVAL_TRANS_REJECT -> {
                onKeyAlertActive = ALERT_APPROVAL_TRANS_REJECT
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
        }
    }

    override fun onSuccessApprovalTrans(message: String) {
        detailApprovalActivityBinding.viewModel?.isProgressActiApprovalTrans?.set(false)
        onMessage(message, ConstantObject.vToastSuccess)
        finish()
    }

    override fun onPositiveClick(o: Any) {
        when(onKeyAlertActive){
            ALERT_APPROVAL_TRANS_APPROVE -> detailApprovalActivityBinding.viewModel?.onSubmitApproval(ALERT_APPROVAL_TRANS_APPROVE, intentDocumentNo)
            ALERT_APPROVAL_TRANS_REJECT -> CustomBottomSheetDialogFragment(this).show(supportFragmentManager, "Dialog")
        }
    }

    override fun onNegativeClick(o: Any) {}

    companion object{
        const val ALERT_APPROVAL_TRANS_NO_CONNECTION = 1
        const val ALERT_APPROVAL_TRANS_APPROVE = 2
        const val ALERT_APPROVAL_TRANS_REJECT = 3
        const val extraDocumentNumber = "doc_no"
        const val extraActivityId = "activity_id"
        const val valueDetailApproval = "Detail Approval"
    }

    override fun onRejectTransaction(notesReject: String) {
        detailApprovalActivityBinding.viewModel?.stApprovalRejectNotes?.set(notesReject.trim())
        detailApprovalActivityBinding.viewModel?.onSubmitApproval(ALERT_APPROVAL_TRANS_REJECT, intentDocumentNo)
    }

    class CustomBottomSheetDialogFragment(private val callback: CustomBottomSheetInterface) : BottomSheetDialogFragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
            val v: View = inflater.inflate(R.layout.custom_bottom_sheet_reject, container, false)
            val btnSubmitReject = v.findViewById<View>(R.id.btn_customBottomSheet_reject) as Button
            val txtNotes = v.findViewById<View>(R.id.txt_customBottomSheet_notes) as TextInputEditText

            btnSubmitReject.setOnClickListener{
                when {
                    txtNotes.text.toString().trim() == "" -> MessageUtils.toastMessage(requireContext(),
                        requireContext().getString(R.string.fill_in_the_blank),
                        ConstantObject.vToastInfo)

                    else -> callback.onRejectTransaction(txtNotes.text.toString().trim())
                }
            }
            return v
        }
    }
}
