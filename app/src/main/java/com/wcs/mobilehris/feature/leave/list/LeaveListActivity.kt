package com.wcs.mobilehris.feature.leave.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityLeaveListBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LeaveListActivity : AppCompatActivity(), LeaveListInterface {
    private lateinit var activityLeaveListBinding: ActivityLeaveListBinding
    private lateinit var intentLeaveFrom : String
    private lateinit var leaveAdapter : CustomLeaveListAdapter
    private var arrLeave = ArrayList<LeaveListModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLeaveListBinding = DataBindingUtil.setContentView(this, R.layout.activity_leave_list)
        activityLeaveListBinding.viewModel = LeaveListViewModel(this, this)
        activityLeaveListBinding.rcLeaveList.layoutManager = LinearLayoutManager(this)
        activityLeaveListBinding.rcLeaveList.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
            intentLeaveFrom = intent.getStringExtra(ConstantObject.extra_intent)
            when(intentLeaveFrom){
                ConstantObject.extra_fromIntentRequest -> it.title = getString(R.string.req_leave_hist_activity)
                else -> it.title = getString(R.string.approval_leave_list_activity)
            }
        }
        leaveAdapter = CustomLeaveListAdapter(this, arrLeave, intentLeaveFrom)
        activityLeaveListBinding.rcLeaveList.adapter = leaveAdapter
        activityLeaveListBinding.viewModel?.validateDataLeave(ConstantObject.vLoadWithProgressBar, intentLeaveFrom.trim())
        activityLeaveListBinding.swLeaveList.setOnRefreshListener {
            activityLeaveListBinding.viewModel?.validateDataLeave(ConstantObject.vLoadWithoutProgressBar, intentLeaveFrom.trim())
        }
    }

    override fun onLoadLeaveList(leaveList: List<LeaveListModel>, typeLoading: Int) {
        arrLeave.clear()
        arrLeave.addAll(leaveList)
        leaveAdapter.notifyDataSetChanged()

        hideUI(ConstantObject.vGlobalUI)
        showUI(ConstantObject.vRecylerViewUI)
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> hideUI(ConstantObject.vProgresBarUI)
            else -> onHideSwipeLeaveList()
        }
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
        }
    }

    override fun onAlertLeaveList(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_LEAVE_LIST_NO_CONNECTION -> { MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)}
        }
    }

    override fun onHideSwipeLeaveList() { activityLeaveListBinding.swLeaveList.isRefreshing = false }

    override fun hideUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> activityLeaveListBinding.pbLeaveList.visibility = View.GONE
            ConstantObject.vRecylerViewUI -> activityLeaveListBinding.rcLeaveList.visibility = View.GONE
            ConstantObject.vGlobalUI -> activityLeaveListBinding.tvLeaveListEmpty.visibility = View.GONE
        }
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> activityLeaveListBinding.pbLeaveList.visibility = View.VISIBLE
            ConstantObject.vRecylerViewUI -> activityLeaveListBinding.rcLeaveList.visibility = View.VISIBLE
            ConstantObject.vGlobalUI -> activityLeaveListBinding.tvLeaveListEmpty.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activityLeaveListBinding.viewModel?.onBackLeaveListMenu(intentLeaveFrom)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        activityLeaveListBinding.viewModel?.onBackLeaveListMenu(intentLeaveFrom)
        super.onBackPressed()
    }

    companion object{
        const val ALERT_LEAVE_LIST_NO_CONNECTION = 1
    }
}
