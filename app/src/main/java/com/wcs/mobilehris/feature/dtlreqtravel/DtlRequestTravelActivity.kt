package com.wcs.mobilehris.feature.dtlreqtravel

import android.graphics.Color
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityDtlRequestTravelBinding
import com.wcs.mobilehris.feature.dtltask.CustomDetailTaskAdapter
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.requesttravel.CustomReqTravelAdapter
import com.wcs.mobilehris.feature.requesttravel.ReqTravelModel
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import kotlinx.android.synthetic.main.custom_bottom_sheet_reject.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DtlRequestTravelActivity : AppCompatActivity(), DtlTravelInterface, TransRejectInterface {
    private lateinit var dtlTravelActivityBinding : ActivityDtlRequestTravelBinding
    private lateinit var dtlTravelAdapter : CustomDetailTaskAdapter
    private lateinit var citiesAdapter : CustomReqTravelAdapter
    private var arrListTeamTravel = ArrayList<FriendModel>()
    private var arrListCitiesTravel = ArrayList<ReqTravelModel>()
    private lateinit var intentFromForm : String
    private lateinit var intentTravelId : String
    private var onKeyAlertActive = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dtlTravelActivityBinding = DataBindingUtil.setContentView(this,R.layout.activity_dtl_request_travel)
        dtlTravelActivityBinding.viewModel = DtlTravelViewModel(this, this)
        dtlTravelActivityBinding.rcDtlReqTravel.layoutManager = LinearLayoutManager(this)
        dtlTravelActivityBinding.rcDtlReqTravel.setHasFixedSize(true)
        dtlTravelActivityBinding.rcDtlReqTravelCities.layoutManager = LinearLayoutManager(this)
        dtlTravelActivityBinding.rcDtlReqTravelCities.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
        }
        intentFromForm = intent.getStringExtra(ConstantObject.extra_intent)
        intentTravelId = intent.getStringExtra(extraTravelId)
        when(intentFromForm){
            ConstantObject.extra_fromIntentDtlTravel -> supportActionBar?.title = ConstantObject.extra_fromIntentDtlTravel
            ConstantObject.extra_fromIntentConfirmTravel -> supportActionBar?.title = ConstantObject.extra_fromIntentConfirmTravel
        }

        dtlTravelAdapter = CustomDetailTaskAdapter(this, arrListTeamTravel, ConstantObject.vNotCreateEdit)
        dtlTravelActivityBinding.rcDtlReqTravel.adapter = dtlTravelAdapter
        citiesAdapter = CustomReqTravelAdapter(this, arrListCitiesTravel, ConstantObject.vNotCreateEdit)
        dtlTravelActivityBinding.rcDtlReqTravelCities.adapter = citiesAdapter
        dtlTravelActivityBinding.viewModel?.validateDataTravel(intentFromForm, intentTravelId)
    }

    override fun onLoadTeam(listTeam: List<FriendModel>) {
        arrListTeamTravel.clear()
        arrListTeamTravel.addAll(listTeam)
        dtlTravelAdapter.notifyDataSetChanged()
    }

    override fun onMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
            ConstantObject.vToastSuccess -> MessageUtils.toastMessage(this, message, ConstantObject.vToastSuccess)
            else -> MessageUtils.snackBarMessage(message,this, ConstantObject.vSnackBarWithButton)
        }
    }

    override fun onAlertDtlReqTravel(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_DTL_REQ_TRAVEL_NO_CONNECTION -> MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)
            ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT -> {
                onKeyAlertActive = ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_DTL_REQ_TRAVEL_CONFIRMATION_REJECT -> {
                onKeyAlertActive = ALERT_DTL_REQ_TRAVEL_CONFIRMATION_REJECT
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
        }
    }

    override fun onLoadCitiesTravel(listCities: List<ReqTravelModel>) {
        arrListCitiesTravel.clear()
        arrListCitiesTravel.addAll(listCities)
        citiesAdapter.notifyDataSetChanged()
    }

    override fun onSuccessDtlTravel(message: String) {
        dtlTravelActivityBinding.viewModel?.isProgressDtlReqTravel?.set(false)
        onMessage(message, ConstantObject.vToastSuccess)
        finish()
    }

    override fun selectedTravelWayRadio(booleanTravelWay: Boolean?) {
        dtlTravelActivityBinding.rgDtlReqTravelTypeWay.clearCheck()
        booleanTravelWay?.let {
            when{
                it -> dtlTravelActivityBinding.rbDtlReqTravelTb.isChecked = true
                else -> dtlTravelActivityBinding.rbDtlReqTravelNonTb.isChecked = true
            }
        }
    }

    override fun onChangeButtonBackground(booleanCityView: Boolean?) {
        when (booleanCityView) {
            true -> {
                dtlTravelActivityBinding.btnDtlReqTravelListCities.setBackgroundResource(R.drawable.bg_blue_button)
                dtlTravelActivityBinding.btnDtlReqTravelListCities.setTextColor(Color.WHITE)
                dtlTravelActivityBinding.btnDtlReqTravelListFriend.setBackgroundResource(R.drawable.bg_transparent_button)
                dtlTravelActivityBinding.btnDtlReqTravelListFriend.setTextColor(Color.BLACK)
            }
            else -> {
                dtlTravelActivityBinding.btnDtlReqTravelListCities.setBackgroundResource(R.drawable.bg_transparent_button)
                dtlTravelActivityBinding.btnDtlReqTravelListCities.setTextColor(Color.BLACK)
                dtlTravelActivityBinding.btnDtlReqTravelListFriend.setBackgroundResource(R.drawable.bg_blue_button)
                dtlTravelActivityBinding.btnDtlReqTravelListFriend.setTextColor(Color.WHITE)
            }
        }
    }

    override fun onRejectTravel(notesReject: String) {
        dtlTravelActivityBinding.viewModel?.stDtlTravelNotes?.set(notesReject.trim())
        dtlTravelActivityBinding.viewModel?.onProcessConfirm(ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT)
    }

    override fun onPositiveClick(o: Any) {
        when(onKeyAlertActive){
            ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT -> dtlTravelActivityBinding.viewModel?.onProcessConfirm(ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT)
            ALERT_DTL_REQ_TRAVEL_CONFIRMATION_REJECT -> {
                CustomBottomSheetDialogFragment(this).show(supportFragmentManager, "Dialog")
            }
        }
    }

    override fun onNegativeClick(o: Any) {}

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
        const val ALERT_DTL_REQ_TRAVEL_NO_CONNECTION = 1
        const val ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT = 3
        const val ALERT_DTL_REQ_TRAVEL_CONFIRMATION_REJECT = 5
        const val extraTravelId = "travel_id"
    }

    class CustomBottomSheetDialogFragment(private val callback:TransRejectInterface) : BottomSheetDialogFragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            val v: View = inflater.inflate(R.layout.custom_bottom_sheet_reject, container, false)
            val btnSubmitReject = v.findViewById<View>(R.id.btn_customBottomSheet_reject) as Button
            val txtNotes = v.findViewById<View>(R.id.txt_customBottomSheet_notes) as TextInputEditText
            btnSubmitReject.setOnClickListener{
                when {
                    txtNotes.text.toString().trim() == "" -> MessageUtils.toastMessage(requireContext(),
                        requireContext().getString(R.string.fill_in_the_blank), ConstantObject.vToastInfo)
                    else -> {callback.onRejectTravel(txtNotes.text.toString().trim())}
                }
            }
            return v
        }
    }
}
