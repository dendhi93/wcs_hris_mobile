package com.wcs.mobilehris.feature.multipletrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.database.entity.TravelRequestEntity
import com.wcs.mobilehris.databinding.ActivityMultipleTripBinding
import com.wcs.mobilehris.feature.requesttravel.RequestTravelActivity
import com.wcs.mobilehris.feature.requesttravellist.CustomTravelListAdapter
import com.wcs.mobilehris.feature.requesttravellist.TravelListModel
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MultipleTripActivity : AppCompatActivity(), MultiTripInterface {
    private lateinit var activityMultipleTripBinding: ActivityMultipleTripBinding
    private lateinit var multipleTripViewModel: MultipleTripViewModel
    private lateinit var intentChargeCode : String
    private lateinit var multiTripAdapter : CustomTravelListAdapter
    private var arrTravel = ArrayList<TravelListModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMultipleTripBinding = DataBindingUtil.setContentView(this, R.layout.activity_multiple_trip)
        multipleTripViewModel = MultipleTripViewModel(this, this)
        activityMultipleTripBinding.viewModel = multipleTripViewModel
        activityMultipleTripBinding.rcReqMultipleTrip.layoutManager = LinearLayoutManager(this)
        activityMultipleTripBinding.rcReqMultipleTrip.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
        }
        intentChargeCode = intent.getStringExtra(RequestTravelActivity.extra_intentChargeCode)
        when{
            intentChargeCode.isNotEmpty() -> activityMultipleTripBinding.viewModel?.initTripData(intentChargeCode.trim())
        }
        multiTripAdapter = CustomTravelListAdapter(this, arrTravel)
        activityMultipleTripBinding.rcReqMultipleTrip.adapter = multiTripAdapter
    }

    override fun onLoadTripList(multiTripList: List<TravelRequestEntity>) {
        arrTravel.clear()
        for(i in multiTripList.indices){
            arrTravel.add(TravelListModel(multiTripList[i].id.toString(),
                multiTripList[i].tDepartCity.trim(),
                multiTripList[i].tReturnCity.trim(),
                multiTripList[i].tdepartDate.trim(),
                multiTripList[i].tReturnDate.trim(),
                "",
                "Waiting"))
        }
        multiTripAdapter.notifyDataSetChanged()
        activityMultipleTripBinding.viewModel?.isProgressMultipleTrip?.set(false)
        activityMultipleTripBinding.viewModel?.isLoadData?.set(true)
    }

    override fun onMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
            ConstantObject.vToastSuccess -> MessageUtils.toastMessage(this, message, ConstantObject.vToastSuccess)
            else -> MessageUtils.snackBarMessage(message,this, ConstantObject.vSnackBarWithButton)
        }
    }

    override fun onAlertMultiTrip(alertMessage: String,alertTitle: String,intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_MULTITRIP_NO_CONNECTION -> MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)
        }
    }

    override fun onSuccessTravel() {
        onMessage(getString(R.string.alert_transaction_success), ConstantObject.vToastSuccess)
        activityMultipleTripBinding.viewModel?.isProgressMultipleTrip?.set(false)
        activityMultipleTripBinding.viewModel?.isLoadData?.set(false)
        finish()
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
        const val ALERT_MULTITRIP_NO_CONNECTION = 1
    }
}
