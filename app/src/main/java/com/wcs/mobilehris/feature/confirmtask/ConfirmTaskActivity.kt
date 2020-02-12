package com.wcs.mobilehris.feature.confirmtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityConfirmTaskBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import com.wcs.mobilehris.utilinterface.DialogInterface

class ConfirmTaskActivity : AppCompatActivity(), ConfirmTaskInterface, DialogInterface {
    private lateinit var activityConfirmBinding : ActivityConfirmTaskBinding
    private var intentConfirmTaskId : String? = ""
    private var intentConfirmTaskChargeCode : String? = ""
    private var intentConfirmTaskFromMenu : String? = ""
    private var confirmActiveDialog : Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityConfirmBinding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_task)
        activityConfirmBinding.viewModel = ConfirmTaskViewModel(this, this)
    }

    override fun onStart() {
        super.onStart()
        activityConfirmBinding.viewModel?.isProgressConfirmTask?.set(false)
        intentConfirmTaskId = intent.getStringExtra(intentExtraTaskId)
        intentConfirmTaskChargeCode = intent.getStringExtra(intentExtraChargeCode)
        intentConfirmTaskFromMenu = intent.getStringExtra(ConstantObject.extra_intent)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
            it.title = intentConfirmTaskFromMenu.toString().trim()
        }
        activityConfirmBinding.viewModel?.stButtonName?.set(intentConfirmTaskFromMenu.toString().trim())
        when{
            intentConfirmTaskId != "" && intentConfirmTaskChargeCode != "" ->{
                val confirmTaskSplitChargeCode = intentConfirmTaskChargeCode.toString().trim().split("|")
                activityConfirmBinding.viewModel?.onLoadConfirmData(intentConfirmTaskId.toString().trim(),
                    confirmTaskSplitChargeCode[0].trim())
            }
        }
        initConfirmRadio()
    }

    private fun initConfirmRadio(){
        activityConfirmBinding.rgConfirmTaskIsOnsite.setOnCheckedChangeListener{ group, checkedId ->
            val radio: RadioButton? = findViewById(checkedId)
            when("${radio?.text}"){
                getString(R.string.travel_business) -> activityConfirmBinding.viewModel?.isOnSiteConfirmTask?.set(true)
                else -> activityConfirmBinding.viewModel?.isOnSiteConfirmTask?.set(false)
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

    override fun onAlertMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
            ConstantObject.vSnackBarWithButton -> MessageUtils.snackBarMessage(message, this, ConstantObject.vSnackBarWithButton)
            else -> MessageUtils.toastMessage(this, message, ConstantObject.vToastSuccess)
        }
    }


    override fun onAlertConfirmTask(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_CONFIRM_TASK_NO_CONNECTION -> MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)
            ALERT_CONFIRM_TASK_CONFIRMATION -> {
                confirmActiveDialog = ALERT_CONFIRM_TASK_CONFIRMATION
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
        }
    }

    override fun onCheckConfirmRadio(isOnsite: Boolean) {
        activityConfirmBinding.rgConfirmTaskIsOnsite.clearCheck()
        when(isOnsite){
            true -> activityConfirmBinding.rbConfirmTaskOnSite.isChecked = true
            else -> activityConfirmBinding.rbConfirmTaskOffSite.isChecked = true
        }
    }

    override fun onSuccessConfirm() {
        Handler().postDelayed({
            onAlertMessage("Transaction Success", ConstantObject.vToastSuccess)
            activityConfirmBinding.viewModel?.isProgressConfirmTask?.set(false)
            finish()
        }, 2000)
    }

    override fun onPositiveClick(o: Any) {
        when(confirmActiveDialog){
            ALERT_CONFIRM_TASK_CONFIRMATION ->activityConfirmBinding.viewModel?.submitConfirmTask()
        }
    }

    override fun onNegativeClick(o: Any) {}

    companion object{
        const val ALERT_CONFIRM_TASK_NO_CONNECTION = 1
        const val intentExtraTaskId = "extra_task_id"
        const val intentExtraChargeCode = "extra_charge_code"
        const val ALERT_CONFIRM_TASK_CONFIRMATION = 3
    }
}
