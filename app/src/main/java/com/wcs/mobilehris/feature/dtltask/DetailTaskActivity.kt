package com.wcs.mobilehris.feature.dtltask

import android.os.Bundle
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityDetailTaskBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils


class DetailTaskActivity : AppCompatActivity(), DtlTaskInterface {
    private lateinit var dtlTaskBinding : ActivityDetailTaskBinding
    private lateinit var dtlTaskAdapter : CustomDetailTaskAdapter
    private var arrTeamTaskList = ArrayList<FriendModel>()
    private var intentTaskId : String? = ""
    private var intentDtlTaskChargeCode : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dtlTaskBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_task)
        dtlTaskBinding.viewModel = DtlTaskViewModel(this, this)
        dtlTaskBinding.rcDtlTask.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        dtlTaskBinding.rcDtlTask.setHasFixedSize(true)
        dtlTaskAdapter = CustomDetailTaskAdapter(this, arrTeamTaskList, ConstantObject.vNotCreateEdit)
        dtlTaskBinding.rcDtlTask.adapter = dtlTaskAdapter
        intentTaskId = intent.getStringExtra(extraTaskId)
        intentDtlTaskChargeCode = intent.getStringExtra(extraCode)

        when{
            intentTaskId != "" && intentDtlTaskChargeCode != "" ->{
                val dtlTaskSplitChargeCode = intentDtlTaskChargeCode.toString().trim().split("|")
                dtlTaskBinding.viewModel?.initDataDtl(intentTaskId.toString().trim(), dtlTaskSplitChargeCode[0].trim())
                supportActionBar?.title = getString(R.string.dtl_task_activity)
            }
        }
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
        }
    }

    override fun loadTeam(listTeam: List<FriendModel>) {
        when{
            listTeam.isNotEmpty() -> {
                arrTeamTaskList.clear()
                arrTeamTaskList.addAll(listTeam)
                dtlTaskAdapter.notifyDataSetChanged()

                dtlTaskBinding.viewModel?.isHiddenRv?.set(false)
            }
            else -> {
                val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                dtlTaskBinding.rlDtlTop.layoutParams = lp
            }
        }
        dtlTaskBinding.viewModel?.isProgressDtl?.set(false)
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
        }
    }

    override fun onAlertDtlTask(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_DTL_TASK_NO_CONNECTION -> { MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)}
        }
    }

    override fun onSetCheckedRadio(isOnsite: Boolean) {
        dtlTaskBinding.rgDtlTaskIsOnsite.clearCheck()
        when(isOnsite){
            true -> dtlTaskBinding.rbDtltaskOnSite.isChecked = true
            else -> dtlTaskBinding.rbDtltaskOffSite.isChecked = true
        }
        dtlTaskBinding.rbDtltaskOnSite.isEnabled = false
        dtlTaskBinding.rbDtltaskOffSite.isEnabled = false
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
        const val ALERT_DTL_TASK_NO_CONNECTION = 1
        const val extraTaskId = "extra_task_id"
        const val extraCode = "extra_charge_code"
    }
}

