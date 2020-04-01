package com.wcs.mobilehris.feature.dtltask

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.databinding.ActivityDetailTaskBinding
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import kotlinx.android.synthetic.main.activity_detail_task.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class DetailTaskActivity : AppCompatActivity(), DtlTaskInterface {
    private lateinit var dtlTaskBinding : ActivityDetailTaskBinding
    private lateinit var dtlTaskAdapter : CustomDetailTaskAdapter
    private var arrTeamTaskList = ArrayList<FriendModel>()
    private var intentTaskId : String? = ""
    private var intentDtlTaskChargeCode : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dtlTaskBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_task)
        dtlTaskBinding.viewModel = DtlTaskViewModel(this, this, ApiRepo())
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
                dtlTaskBinding.viewModel?.initDataDtl(intentTaskId.toString().trim(), intentDtlTaskChargeCode.toString().trim())
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

    override fun enableUI() {
        dtlTaskBinding.btnDeleteTask.isEnabled = false
        dtlTaskBinding.txtDtlTaskDateTaskFrom.isEnabled = true
        dtlTaskBinding.txtDtlTaskDateTaskInto.isEnabled = true
        dtlTaskBinding.txtDtlTaskDateFrom.isEnabled = true
        dtlTaskBinding.txtDtlTaskDateInto.isEnabled = true
        dtlTaskBinding.txtDtlTaskContactPerson.isEnabled = true
        dtlTaskBinding.txtDtlTaskDescription.isEnabled = true
        dtlTaskBinding.btnEditTask.text = getString(R.string.finish_task)
        dtlTaskBinding.btnEditTask.setOnClickListener {
            dtlTaskBinding.viewModel!!.processEditTask()
        }
    }

    override fun disableUI() {
        dtlTaskBinding.btnDeleteTask.isEnabled = true
        dtlTaskBinding.txtDtlTaskDateTaskFrom.isEnabled = false
        dtlTaskBinding.txtDtlTaskDateTaskInto.isEnabled = false
        dtlTaskBinding.txtDtlTaskDateFrom.isEnabled = false
        dtlTaskBinding.txtDtlTaskDateInto.isEnabled = false
        dtlTaskBinding.txtDtlTaskContactPerson.isEnabled = false
        dtlTaskBinding.txtDtlTaskDescription.isEnabled = false
        dtlTaskBinding.btnEditTask.text = getString(R.string.edit_task)
    }

    override fun onSuccessEditTask() {
        disableUI()

        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_ACTIVITY)
        startActivity(intent)

    }

    override fun onSuccessDeleteTask() {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_ACTIVITY)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                dtlTaskBinding.viewModel?.onBackPressMenu()
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        dtlTaskBinding.viewModel?.onBackPressMenu()
        finish()
        super.onBackPressed()
    }

    companion object{
        const val ALERT_DTL_TASK_NO_CONNECTION = 1
        const val extraTaskId = "extra_task_id"
        const val extraCode = "extra_charge_code"
    }
}

