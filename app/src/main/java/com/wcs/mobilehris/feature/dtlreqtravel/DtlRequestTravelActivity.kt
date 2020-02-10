package com.wcs.mobilehris.feature.dtlreqtravel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.database.entity.TransportTypeEntity
import com.wcs.mobilehris.databinding.ActivityDtlRequestTravelBinding
import com.wcs.mobilehris.feature.dtltask.CustomDetailTaskAdapter
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DtlRequestTravelActivity : AppCompatActivity(), DtlTravelInterface {
    private lateinit var dtlTravelActivityBinding : ActivityDtlRequestTravelBinding
    private lateinit var dtlTravelAdapter : CustomDetailTaskAdapter
    private var arrListTeamTravel = ArrayList<FriendModel>()
    private lateinit var intentFromForm : String
    private lateinit var intentTravelId : String
    private var arrListTransport =  ArrayList<String>()
    private var arrListTransportCode = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dtlTravelActivityBinding = DataBindingUtil.setContentView(this,R.layout.activity_dtl_request_travel)
        dtlTravelActivityBinding.viewModel = DtlTravelViewModel(this, this)
        dtlTravelActivityBinding.rcDtlReqTravel.layoutManager = LinearLayoutManager(this)
        dtlTravelActivityBinding.rcDtlReqTravel.setHasFixedSize(true)
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

        dtlTravelActivityBinding.viewModel?.getDataTransport()
        initRadioDtlTravel()
        dtlTravelAdapter = CustomDetailTaskAdapter(this, arrListTeamTravel)
        dtlTravelActivityBinding.rcDtlReqTravel.adapter = dtlTravelAdapter
    }

    private fun initRadioDtlTravel(){
        dtlTravelActivityBinding.rgDtlReqTravelTypeWay.setOnCheckedChangeListener{ group, checkedId ->
            val radio: RadioButton? = findViewById(checkedId)
            when(radio?.text){
                getString(R.string.multiple_destination) -> dtlTravelActivityBinding.viewModel?.stDtlTravelIsOneWay?.set(false)
                else -> dtlTravelActivityBinding.viewModel?.stDtlTravelIsOneWay?.set(true)
            }
        }
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
        }
    }

    override fun onLoadTransport(listTransport: List<TransportTypeEntity>) {
        for(i in listTransport.indices){
            when(i) {
                0 -> {
                    arrListTransport.add("Transportation Type")
                    arrListTransport.add("")
                }
                else -> {
                    arrListTransport.add(listTransport[i].mTransTypeDescription.trim())
                    arrListTransportCode.add(listTransport[i].mTransCode.trim())
                }
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrListTransport)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        dtlTravelActivityBinding.spDtlReqTravelTransType.adapter = adapter
        dtlTravelActivityBinding.spDtlReqTravelTransType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when{
                    position > 0 -> {
                        val code = arrListTransportCode[position].trim()
                        dtlTravelActivityBinding.viewModel?.stDtlTravelTransType?.set(code)
                        hidingKeyboard()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        when{
            arrListTransport.isNotEmpty() -> dtlTravelActivityBinding.viewModel?.validateDataTravel(intentFromForm, intentTravelId)
        }
    }

    override fun onSuccessDtlTravel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCityDepart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCityReturn() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun selectedTravelWayRadio(booleanTravelWay: Boolean?) {
        dtlTravelActivityBinding.rgDtlReqTravelTypeWay.clearCheck()
        booleanTravelWay?.let {
            when{
                it -> dtlTravelActivityBinding.rbDtlReqTravelOneWay.isChecked = true
                else -> dtlTravelActivityBinding.rbDtlReqTravelRoundTrip.isChecked = true
            }
        }
    }

    override fun selectedSpinner(selectedTransType: String) {
        when{
            arrListTransport.isNotEmpty() -> {
                for(i in arrListTransport.indices){
                    when(arrListTransport[i].trim()){
                        selectedTransType -> dtlTravelActivityBinding.spDtlReqTravelTransType.setSelection(i)
                    }
                }
            }
        }
    }

    override fun onPositiveClick(o: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    private fun hidingKeyboard(){
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    companion object{
        const val ALERT_DTL_REQ_TRAVEL_NO_CONNECTION = 1
        const val extraTravelId = "travel_id"
        const val selectDateFrom = "date_from"
        const val selectDateInto = "date_into"
    }
}
