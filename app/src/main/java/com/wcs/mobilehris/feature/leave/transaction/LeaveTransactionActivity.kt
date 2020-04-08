package com.wcs.mobilehris.feature.leave.transaction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.database.entity.ReasonLeaveEntity
import com.wcs.mobilehris.databinding.ActivityLeaveTransactionBinding
import com.wcs.mobilehris.feature.leave.list.LeaveListActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import com.wcs.mobilehris.utilinterface.CustomBottomSheetInterface
import com.wcs.mobilehris.utilinterface.DialogInterface

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LeaveTransactionActivity : AppCompatActivity(), LeaveTransInterface,
    DialogInterface, CustomBottomSheetInterface {
    private lateinit var leaveActivityBinding : ActivityLeaveTransactionBinding
    private lateinit var intentLeaveId : String
    private lateinit var intentLeaveType : String
    private lateinit var intentRequestor : String
    private var arrTransDescLeaveType = ArrayList<String>()
    private var arrTransLeaveCode = ArrayList<String>()
    private var onKeyAlertActive = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leaveActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_leave_transaction)
        leaveActivityBinding.viewModel = LeaveTransViewModel(this, this, ApiRepo())
    }

    override fun onStart() {
        super.onStart()
        intentLeaveType = intent.getStringExtra(ConstantObject.extra_intent)
        intentLeaveId = intent.getStringExtra(extralLeaveId)
        intentRequestor = intent.getStringExtra(extraLeaveRequestor)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
            when(intentLeaveType){
                valueLeaveDtlType -> it.title = valueLeaveDtlType
                ConstantObject.extra_fromIntentRequest -> it.title = getString(R.string.req_leave_activity)
                ConstantObject.extra_fromIntentApproval -> {
                    it.title = getString(R.string.approval_leave_activity)
                    it.subtitle = intentRequestor.trim()
                }
                else -> {
                    it.title = ConstantObject.vEditTask
                }
            }
        }
        leaveActivityBinding.viewModel?.onInitLeaveData(intentLeaveType, intentLeaveId.trim())
        leaveActivityBinding.viewModel?.onValidateFromMenu(intentLeaveType)
        leaveActivityBinding.viewModel?.initReasonSpinner()
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
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
            ConstantObject.vToastSuccess -> MessageUtils.toastMessage(this, message, ConstantObject.vToastSuccess)
            else -> MessageUtils.snackBarMessage(message,this, ConstantObject.vSnackBarWithButton)
        }
    }

    override fun onAlertLeaveTrans(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_LEAVE_TRANS_NO_CONNECTION -> MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)
            ALERT_LEAVE_TRANS_REQUEST -> {
                onKeyAlertActive = ALERT_LEAVE_TRANS_REQUEST
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_LEAVE_TRANS_EDIT -> {
                onKeyAlertActive = ALERT_LEAVE_TRANS_EDIT
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_LEAVE_TRANS_DELETE -> {
                onKeyAlertActive = ALERT_LEAVE_TRANS_DELETE
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_LEAVE_TRANS_APPROVE -> {
                onKeyAlertActive = ALERT_LEAVE_TRANS_APPROVE
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_LEAVE_TRANS_REJECT -> {
                onKeyAlertActive = ALERT_LEAVE_TRANS_REJECT
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
        }
    }

    override fun onSuccessLeaveTrans(message: String) {
        leaveActivityBinding.viewModel?.isProgressLeaveTrans?.set(false)
        onMessage(message, ConstantObject.vToastSuccess)
        finish()
    }

    override fun onLoadReasonLeave(listLeave: List<ReasonLeaveEntity>) {
        var index  = 0
        for(i in listLeave.indices){
            when(index) {
                0 -> {
                    arrTransDescLeaveType.add("Leave Type")
                    arrTransLeaveCode.add("")
                    arrTransDescLeaveType.add(listLeave[i].mLeaveDescription.trim())
                    arrTransLeaveCode.add(listLeave[i].mLeaveCode.trim())
                }
                else -> {
                    arrTransDescLeaveType.add(listLeave[i].mLeaveDescription.trim())
                    arrTransLeaveCode.add(listLeave[i].mLeaveCode.trim())
                }
            }
            index += 1
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrTransDescLeaveType)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        leaveActivityBinding.spReqLeaveReason.adapter = adapter
        leaveActivityBinding.spReqLeaveReason.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when{
                    position > 0 -> {
                        val code = arrTransLeaveCode[position].trim()
                        val descTrans = arrTransDescLeaveType[position].trim()
                        leaveActivityBinding.viewModel?.validateReasonLeave(descTrans, code)
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun onSelectedSpinner(selectedReason: String) {
        when{
            arrTransDescLeaveType.isNotEmpty() -> {
                for(i in arrTransDescLeaveType.indices){
                    when(arrTransDescLeaveType[i].trim()){
                        selectedReason -> leaveActivityBinding.spReqLeaveReason.setSelection(i)
                    }
                }
            }
        }
    }

    override fun onRejectTransaction(notesReject: String) {
        leaveActivityBinding.viewModel?.stLeaveRejectNotes?.set(notesReject.trim())
        leaveActivityBinding.viewModel?.onSubmitLeave(ALERT_LEAVE_TRANS_REJECT)
    }

    override fun enableUI(typeUI: Int) {
        when(typeUI){
            columnCountTime -> leaveActivityBinding.txtLeaveTransQtyTime.isEnabled = true
            columnDateFrom -> leaveActivityBinding.txtLeaveTransDateFrom.isEnabled = true
            columnDateInto -> leaveActivityBinding.txtLeaveTransDateInto.isEnabled = true
            columnTimeFrom -> leaveActivityBinding.txtLeaveTransTimeFrom.isEnabled = true
            columnTimeInto -> leaveActivityBinding.txtLeaveTransTimeInto.isEnabled = true
        }
    }

    override fun disableUI(typeUI: Int) {
        when(typeUI){
            columnCountTime -> leaveActivityBinding.txtLeaveTransQtyTime.isEnabled = false
            columnDateFrom -> leaveActivityBinding.txtLeaveTransDateFrom.isEnabled = false
            columnDateInto -> leaveActivityBinding.txtLeaveTransDateInto.isEnabled = false
            columnTimeFrom -> leaveActivityBinding.txtLeaveTransTimeFrom.isEnabled = false
            columnTimeInto -> leaveActivityBinding.txtLeaveTransTimeInto.isEnabled = false
        }
    }

    override fun onPositiveClick(o: Any) {
        when(onKeyAlertActive){
            ALERT_LEAVE_TRANS_REQUEST -> leaveActivityBinding.viewModel?.onSubmitLeave(ALERT_LEAVE_TRANS_REQUEST)
            ALERT_LEAVE_TRANS_EDIT -> leaveActivityBinding.viewModel?.onSubmitLeave(ALERT_LEAVE_TRANS_EDIT)
            ALERT_LEAVE_TRANS_DELETE -> leaveActivityBinding.viewModel?.onSubmitLeave(ALERT_LEAVE_TRANS_DELETE)
            ALERT_LEAVE_TRANS_APPROVE -> leaveActivityBinding.viewModel?.onSubmitLeave(ALERT_LEAVE_TRANS_APPROVE)
            ALERT_LEAVE_TRANS_REJECT -> CustomBottomSheetDialogFragment(this).show(supportFragmentManager, "Dialog")
        }
    }

    override fun onNegativeClick(o: Any) {}

    companion object{
        const val ALERT_LEAVE_TRANS_NO_CONNECTION = 1
        const val ALERT_LEAVE_TRANS_REQUEST = 2
        const val ALERT_LEAVE_TRANS_EDIT = 3
        const val ALERT_LEAVE_TRANS_DELETE = 4
        const val ALERT_LEAVE_TRANS_APPROVE = 5
        const val ALERT_LEAVE_TRANS_REJECT = 6
        const val columnDateFrom = 11
        const val columnDateInto = 22
        const val columnTimeFrom = 33
        const val columnTimeInto = 44
        const val columnCountTime = 55
        const val extralLeaveId = "leave_id"
        const val extraLeaveRequestor = "requestor"
        const val valueLeaveDtlType = "DETAIL LEAVE"
        const val valueSickLeave = "Sick Leave"
        const val valueAnnualLeave = "Annual Leave"
        const val valueTwoHour = "2 Hour Leave"
        const val valueFourHours = "4 Hour Leave"
    }

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
}