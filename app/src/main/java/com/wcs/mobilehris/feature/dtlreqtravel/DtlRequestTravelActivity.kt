package com.wcs.mobilehris.feature.dtlreqtravel

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.databinding.ActivityDtlRequestTravelBinding
import com.wcs.mobilehris.feature.dtltask.CustomDetailTaskAdapter
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.requesttravel.CustomReqTravelAdapter
import com.wcs.mobilehris.feature.requesttravel.ReqTravelModel
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import com.wcs.mobilehris.utilinterface.CustomBottomSheetInterface


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class DtlRequestTravelActivity : AppCompatActivity(), DtlTravelInterface,
    CustomBottomSheetInterface {
    private lateinit var dtlTravelActivityBinding : ActivityDtlRequestTravelBinding
    private lateinit var dtlTravelAdapter : CustomDetailTaskAdapter
    private lateinit var citiesAdapter : CustomReqTravelAdapter
    private var arrListTeamTravel = ArrayList<FriendModel>()
    private var arrListCitiesTravel = ArrayList<ReqTravelModel>()
    private lateinit var intentFromForm : String
    private lateinit var intentTravelId : String
    private var onKeyAlertActive = 0
    private var intentTravelRequestor : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dtlTravelActivityBinding = DataBindingUtil.setContentView(this,R.layout.activity_dtl_request_travel)
        dtlTravelActivityBinding.viewModel = DtlTravelViewModel(this, this, ApiRepo())
        dtlTravelActivityBinding.rcDtlReqTravel.layoutManager = LinearLayoutManager(this)
        dtlTravelActivityBinding.rcDtlReqTravel.setHasFixedSize(true)
        dtlTravelActivityBinding.rcDtlReqTravelCities.layoutManager = LinearLayoutManager(this)
        dtlTravelActivityBinding.rcDtlReqTravelCities.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()
        intentFromForm = intent.getStringExtra(ConstantObject.extra_intent)
        intentTravelId = intent.getStringExtra(extraTravelId)
        intentTravelRequestor = intent.getStringExtra(extraTravelRequestor)
        dtlTravelActivityBinding.viewModel?.stIntentTravelHeaderId?.set(intent.getStringExtra(extraTravelHeaderId))
        dtlTravelActivityBinding.viewModel?.stDocNumber?.set(intent.getStringExtra(extraTravelDocNumber).trim().trim())

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
            when(intentFromForm){
                ConstantObject.extra_fromIntentDtlTravel -> it.title = ConstantObject.extra_fromIntentDtlTravel
                ConstantObject.extra_fromIntentConfirmTravel -> it.title = ConstantObject.extra_fromIntentConfirmTravel
                ConstantObject.vEditTask -> it.title = ConstantObject.vEditTask
                else -> {
                    it.title = getString(R.string.approval_travel_activity)
                    it.subtitle = intentTravelRequestor.trim()
                }
            }
        }

        dtlTravelAdapter = CustomDetailTaskAdapter(this, arrListTeamTravel, ConstantObject.vNotCreateEdit)
        dtlTravelActivityBinding.rcDtlReqTravel.adapter = dtlTravelAdapter
        citiesAdapter = CustomReqTravelAdapter(this, arrListCitiesTravel, ConstantObject.vNotCreateEdit)
        dtlTravelActivityBinding.rcDtlReqTravelCities.adapter = citiesAdapter
        dtlTravelActivityBinding.viewModel?.validateDataTravel(intentFromForm, intentTravelId)
        onChangeButtonBackground(true)
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
            ALERT_DTL_APPROVE_TRAVEL_ACCEPT -> {
                onKeyAlertActive = ALERT_DTL_APPROVE_TRAVEL_ACCEPT
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_DTL_APPROVE_TRAVEL_REJECT -> {
                onKeyAlertActive = ALERT_DTL_APPROVE_TRAVEL_REJECT
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_DTL_APPROVE_TRAVEL_DELETE -> {
                onKeyAlertActive = ALERT_DTL_APPROVE_TRAVEL_DELETE
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_DTL_APPROVE_TRAVEL_EDIT -> {
                onKeyAlertActive = ALERT_DTL_APPROVE_TRAVEL_EDIT
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

    override fun selectedTravelWayRadio(stTravelWay: String) {
        dtlTravelActivityBinding.rgDtlReqTravelTypeWay.clearCheck()
        when(stTravelWay){
            "TB" -> dtlTravelActivityBinding.rbDtlReqTravelTb.isChecked = true
            else -> dtlTravelActivityBinding.rbDtlReqTravelNonTb.isChecked = true
        }
    }

    override fun onChangeButtonBackground(booleanCityView: Boolean?) {
        when (booleanCityView) {
            true -> {
                dtlTravelActivityBinding.btnDtlReqTravelListCities.setBackgroundResource(R.color.colorGray400)
                dtlTravelActivityBinding.btnDtlReqTravelListCities.setTextColor(Color.BLACK)
                dtlTravelActivityBinding.btnDtlReqTravelListFriend.setBackgroundResource(R.color.colorGray200)
                dtlTravelActivityBinding.btnDtlReqTravelListFriend.setTextColor(Color.GRAY)
            }
            else -> {
                dtlTravelActivityBinding.btnDtlReqTravelListCities.setBackgroundResource(R.color.colorGray200)
                dtlTravelActivityBinding.btnDtlReqTravelListCities.setTextColor(Color.GRAY)
                dtlTravelActivityBinding.btnDtlReqTravelListFriend.setBackgroundResource(R.color.colorGray400)
                dtlTravelActivityBinding.btnDtlReqTravelListFriend.setTextColor(Color.BLACK)
            }
        }
    }

    override fun onRejectTransaction(notesReject: String) {
        dtlTravelActivityBinding.viewModel?.stDtlTravelNotesReject?.set(notesReject.trim())
        when(intentFromForm){
            ConstantObject.extra_fromIntentConfirmTravel -> dtlTravelActivityBinding.viewModel?.onProcessConfirm(ALERT_DTL_REQ_TRAVEL_CONFIRMATION_REJECT)
            ConstantObject.extra_fromIntentApproval -> dtlTravelActivityBinding.viewModel?.onProcessConfirm(ALERT_DTL_APPROVE_TRAVEL_REJECT)
        }

    }

    override fun onPositiveClick(o: Any) {
        when(onKeyAlertActive){
            ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT -> dtlTravelActivityBinding.viewModel?.onProcessConfirm(ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT)
            ALERT_DTL_REQ_TRAVEL_CONFIRMATION_REJECT -> CustomBottomSheetDialogFragment(this).show(supportFragmentManager, "Dialog")
            ALERT_DTL_APPROVE_TRAVEL_ACCEPT -> dtlTravelActivityBinding.viewModel?.onProcessConfirm(ALERT_DTL_APPROVE_TRAVEL_ACCEPT)
            ALERT_DTL_APPROVE_TRAVEL_DELETE -> dtlTravelActivityBinding.viewModel?.onProcessConfirm(ALERT_DTL_APPROVE_TRAVEL_DELETE)
            ALERT_DTL_APPROVE_TRAVEL_EDIT -> dtlTravelActivityBinding.viewModel?.onProcessConfirm(ALERT_DTL_APPROVE_TRAVEL_EDIT)
            else -> CustomBottomSheetDialogFragment(this).show(supportFragmentManager, "Dialog")
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
        const val ALERT_DTL_APPROVE_TRAVEL_ACCEPT = 7
        const val ALERT_DTL_APPROVE_TRAVEL_REJECT = 9
        const val ALERT_DTL_APPROVE_TRAVEL_DELETE = 11
        const val ALERT_DTL_APPROVE_TRAVEL_EDIT = 13
        const val extraTravelId = "travel_id"
        const val extraTravelHeaderId = "travel_header_id"
        const val extraTravelRequestor = "requestor"
        const val extraTravelDocNumber = "doc_number"
        const val approvalAccept = "approval_accept"
        const val approvalReject = "approval_reject"
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
