package com.wcs.mobilehris.feature.approvallistofactivities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.databinding.ActivityApprovalActivityListBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

class AppActionListActivity : AppCompatActivity(), AppActionListInterface {
    private lateinit var appActivityListBinding : ActivityApprovalActivityListBinding
    private lateinit var activityAprovalAdapter : AppActionListAdapter
    private var arrActivityApproval = ArrayList<AppActionListModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appActivityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_approval_activity_list)
        appActivityListBinding.viewModel = AppActionListViewModel(this, this, ApiRepo())
        appActivityListBinding.rcAppListOfActivities.layoutManager = LinearLayoutManager(this)
        appActivityListBinding.rcAppListOfActivities.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
            it.title = getString(R.string.approval_action_list_activity)
        }

        activityAprovalAdapter = AppActionListAdapter(this, arrActivityApproval)
        appActivityListBinding.rcAppListOfActivities.adapter = activityAprovalAdapter
        appActivityListBinding.viewModel?.initDataActivities(ConstantObject.vLoadWithProgressBar)
        appActivityListBinding.swAppListOfActivities.setOnRefreshListener {
            appActivityListBinding.viewModel?.initDataActivities(ConstantObject.vLoadWithProgressBar)
        }
    }

    override fun onLoadApprovalListOfActivities(appActionListOfActivities: List<AppActionListModel>, typeLoading: Int) {
        arrActivityApproval.clear()
        arrActivityApproval.addAll(appActionListOfActivities)
        activityAprovalAdapter.notifyDataSetChanged()

        hideUI(ConstantObject.vGlobalUI)
        showUI(ConstantObject.vRecylerViewUI)
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> hideUI(ConstantObject.vProgresBarUI)
            else -> onHideSwipeActivitiesList()
        }
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
        }
    }

    override fun onAlertAppActivitiesList(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_ACTIVITY_APPROVAL_LIST_NO_CONNECTION -> { MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)}
        }
    }

    override fun onHideSwipeActivitiesList() {
        appActivityListBinding.swAppListOfActivities.isRefreshing = false
    }

    override fun hideUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> appActivityListBinding.pbAppListOfActivities.visibility = View.GONE
            ConstantObject.vRecylerViewUI -> appActivityListBinding.rcAppListOfActivities.visibility = View.GONE
            ConstantObject.vGlobalUI -> appActivityListBinding.tvAppListOfActivitiesEmpty.visibility = View.GONE
        }
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> appActivityListBinding.pbAppListOfActivities.visibility = View.VISIBLE
            ConstantObject.vRecylerViewUI -> appActivityListBinding.rcAppListOfActivities.visibility = View.VISIBLE
            ConstantObject.vGlobalUI -> appActivityListBinding.tvAppListOfActivitiesEmpty.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                appActivityListBinding.viewModel?.onBackLeaveListMenu()
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        appActivityListBinding.viewModel?.onBackLeaveListMenu()
        finish()
        super.onBackPressed()
    }

    companion object{
        const val ALERT_ACTIVITY_APPROVAL_LIST_NO_CONNECTION = 1
        const val extraDocumentNumber = "doc_no"
        const val extraActivityId = "activity_id"
    }
}
