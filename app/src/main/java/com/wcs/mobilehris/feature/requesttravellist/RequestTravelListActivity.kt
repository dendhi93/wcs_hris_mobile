package com.wcs.mobilehris.feature.requesttravellist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityRequestTravelListBinding
import com.wcs.mobilehris.feature.requesttravel.RequestTravelActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RequestTravelListActivity : AppCompatActivity(), ReqTravelListInterface {
    private lateinit var activityRequestTravelListBinding : ActivityRequestTravelListBinding
    private lateinit var requestTravelListAdapter : CustomTravelListAdapter
    private var arrReqTravel = ArrayList<TravelListModel>()
    private lateinit var intentTravelFrom : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRequestTravelListBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_travel_list)
        activityRequestTravelListBinding.viewModel = RequestTravelListViewModel(this, this)
        activityRequestTravelListBinding.rcReqTravelList.layoutManager = LinearLayoutManager(this)
        activityRequestTravelListBinding.rcReqTravelList.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
            intentTravelFrom = intent.getStringExtra(ConstantObject.extra_intent)
            when(intentTravelFrom){
                ConstantObject.extra_fromIntentRequest -> it.title = getString(R.string.req_travel_hist_activity)
                else -> it.title = getString(R.string.approval_travel_list_activity)
            }
        }
        requestTravelListAdapter = CustomTravelListAdapter(this, arrReqTravel, intentTravelFrom)
        activityRequestTravelListBinding.rcReqTravelList.adapter = requestTravelListAdapter
        activityRequestTravelListBinding.viewModel?.initDataTravel(ConstantObject.vLoadWithProgressBar, intentTravelFrom)
        activityRequestTravelListBinding.swReqTravelList.setOnRefreshListener {
            activityRequestTravelListBinding.viewModel?.initDataTravel(ConstantObject.vLoadWithoutProgressBar, intentTravelFrom)
        }
    }

    override fun onLoadTravelList(reqTravList: List<TravelListModel>, typeLoading: Int) {
        arrReqTravel.clear()
        arrReqTravel.addAll(reqTravList)
        requestTravelListAdapter.notifyDataSetChanged()

        hideUI(ConstantObject.vGlobalUI)
        showUI(ConstantObject.vRecylerViewUI)
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> hideUI(ConstantObject.vProgresBarUI)
            else -> onHideSwipeTravelList()
        }
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
        }
    }

    override fun onAlertReqTravelList(alertMessage: String,alertTitle: String,intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_REQ_TRAVEL_HIST_NO_CONNECTION -> { MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)}
        }
    }

    override fun onHideSwipeTravelList() { activityRequestTravelListBinding.swReqTravelList.isRefreshing = false }

    override fun intentToRequest() {
        startActivity(Intent(this, RequestTravelActivity::class.java))
        finish()
    }

    override fun hideUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> activityRequestTravelListBinding.pbReqTravelList.visibility = View.GONE
            ConstantObject.vRecylerViewUI -> activityRequestTravelListBinding.rcReqTravelList.visibility = View.GONE
            ConstantObject.vGlobalUI -> activityRequestTravelListBinding.tvReqTravelListEmpty.visibility = View.GONE
        }
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> activityRequestTravelListBinding.pbReqTravelList.visibility = View.VISIBLE
            ConstantObject.vRecylerViewUI -> activityRequestTravelListBinding.rcReqTravelList.visibility = View.VISIBLE
            ConstantObject.vGlobalUI -> activityRequestTravelListBinding.tvReqTravelListEmpty.visibility = View.VISIBLE
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
    companion object{
        const val ALERT_REQ_TRAVEL_HIST_NO_CONNECTION = 1
    }
}
