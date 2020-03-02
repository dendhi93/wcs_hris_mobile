package com.wcs.mobilehris.feature.leave.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.wcs.mobilehris.R
import com.wcs.mobilehris.database.entity.ReasonLeaveEntity
import com.wcs.mobilehris.databinding.ActivityLeaveTransactionBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LeaveTransactionActivity : AppCompatActivity(), LeaveTransInterface {
    private lateinit var leaveActivityBinding : ActivityLeaveTransactionBinding
    private lateinit var intentLeaveId : String
    private lateinit var intentLeaveType : String
    private lateinit var intentRequestor : String
    private var arrTransDescLeaveType = ArrayList<String>()
    private var arrTransLeaveCode = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leaveActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_leave_transaction)
        leaveActivityBinding.viewModel = LeaveTransViewModel(this, this)
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
                valueLeaveDtlType ->{
                    it.title = valueLeaveDtlType
                    leaveActivityBinding.viewModel?.isDtlLeaveMenu?.set(true)
                }
                ConstantObject.extra_fromIntentRequest -> {
                    it.title = getString(R.string.req_leave_activity)
                    leaveActivityBinding.viewModel?.isDtlLeaveMenu?.set(false)
                    leaveActivityBinding.viewModel?.stLeaveSubmitButton?.set(getString(R.string.save))
                }
                ConstantObject.extra_fromIntentApproval -> {
                    it.title = getString(R.string.approval_leave_activity)
                    it.subtitle = intentRequestor.trim()
                    leaveActivityBinding.viewModel?.isDtlLeaveMenu?.set(false)
                    leaveActivityBinding.viewModel?.stLeaveSubmitButton?.set(getString(R.string.save))
                }
                else -> {
                    it.title = ConstantObject.vEditTask
                    leaveActivityBinding.viewModel?.isDtlLeaveMenu?.set(false)
                    leaveActivityBinding.viewModel?.stLeaveSubmitButton?.set(ConstantObject.vEditTask)
                }
            }
        }
        leaveActivityBinding.viewModel?.onInitLeaveData(intentLeaveType)
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
            }
    }

    override fun onSuccessDtlTravel(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadReasonLeave(listLeave: List<ReasonLeaveEntity>) {
        var index  = 0
        for(i in listLeave.indices){
            when(index) {
                0 -> {
                    arrTransDescLeaveType.add("Leave Type")
                    arrTransLeaveCode.add("")
                }
                else -> {
                    arrTransDescLeaveType.add(listLeave[i-1].mLeaveDescription.trim())
                    arrTransLeaveCode.add(listLeave[i-1].mLeaveCode.trim())
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
                        val code = arrTransDescLeaveType[position].trim()
                        val descTrans = arrTransLeaveCode[position].trim()
//                        activityRequestTravelBinding.viewModel?.stTransTypeCode?.set("$code-$descTrans")
//                        onHideSoftKeyboard()
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

    companion object{
        const val ALERT_LEAVE_TRANS_NO_CONNECTION = 1
        const val extralLeaveId = "leave_id"
        const val extraLeaveRequestor = "requestor"
        const val valueLeaveDtlType = "DETAIL LEAVE"
        const val columnDateFrom = "column_date_from"
        const val columnDateInto = "column_date_into"
        const val columnTimeFrom = "column_time_from"
        const val columnTimeInto = "column_time_into"
    }
}
