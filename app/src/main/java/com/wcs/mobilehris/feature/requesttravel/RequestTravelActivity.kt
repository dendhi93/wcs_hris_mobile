package com.wcs.mobilehris.feature.requesttravel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.database.entity.ChargeCodeEntity
import com.wcs.mobilehris.database.entity.TransportTypeEntity
import com.wcs.mobilehris.databinding.ActivityRequestTravelBinding
import com.wcs.mobilehris.feature.city.CityActivity
import com.wcs.mobilehris.feature.dtltask.CustomDetailTaskAdapter
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.team.TeamActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils


class RequestTravelActivity : AppCompatActivity(), RequestTravelInterface {
    private lateinit var activityRequestTravelBinding : ActivityRequestTravelBinding
    private lateinit var travelAdapter : CustomDetailTaskAdapter
    private var arrTeamTravel = ArrayList<FriendModel>()
    private var keyDialogActive = 0
    private var arrChargeCode = ArrayList<String>()
    private var arrDescTransType = ArrayList<String>()
    private var arrTransTypeCode = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRequestTravelBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_travel)
        activityRequestTravelBinding.viewModel = RequestTravelViewModel(this, this)
        activityRequestTravelBinding.rcReqTravel.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
        }
        activityRequestTravelBinding.viewModel?.let {
            it.initDataChargeCode()
            it.getDataTransport()
        }
        initRadioTravel()
        activityRequestTravelBinding.viewModel?.initDataChargeCode()
        activityRequestTravelBinding.rcReqTravel.setHasFixedSize(true)
        travelAdapter = CustomDetailTaskAdapter(this, arrTeamTravel)
        activityRequestTravelBinding.rcReqTravel.adapter = travelAdapter
    }

    private fun initRadioTravel(){
        activityRequestTravelBinding.rgReqTravelTypeWay.setOnCheckedChangeListener{ group, checkedId ->
            val radio: RadioButton? = findViewById(checkedId)
            activityRequestTravelBinding.viewModel?.stTypeTrip?.set(radio?.text.toString())
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

    override fun onLoadTeam(listTeam: List<FriendModel>) {
        arrTeamTravel.addAll(listTeam)
        travelAdapter.notifyDataSetChanged()
    }

    override fun onMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
            ConstantObject.vToastSuccess -> MessageUtils.toastMessage(this, message, ConstantObject.vToastSuccess)
            else -> MessageUtils.snackBarMessage(message,this, ConstantObject.vSnackBarWithButton)
        }
    }

    override fun onAlertReqTravel(alertMessage: String,alertTitle: String,intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_REQ_TRAVEL_NO_CONNECTION ->  MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)
            ALERT_REQ_TRAVEL_CONFIRMATION -> {
                keyDialogActive = ALERT_REQ_TRAVEL_CONFIRMATION
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
        }
    }

    override fun onLoadChargeCode(listChargeCode: List<ChargeCodeEntity>) {
        arrChargeCode.clear()
        for(i in listChargeCode.indices){
            arrChargeCode.add(listChargeCode[i].mChargeCodeNo+"  "+listChargeCode[i].mDescriptionChargeCode)
        }

        val chargeCodeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrChargeCode)
        activityRequestTravelBinding.actReqTravelChargeCode.setAdapter(chargeCodeAdapter)
        activityRequestTravelBinding.actReqTravelChargeCode.threshold = 1
        activityRequestTravelBinding.actReqTravelChargeCode.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
            val splitChargeCode = selectedItem.split("  ")[0]
            activityRequestTravelBinding.viewModel?.stChargeCode?.set(splitChargeCode.trim())
        }
    }

    override fun onLoadTransport(listTransport: List<TransportTypeEntity>) {
        for(i in listTransport.indices){
            when(i) {
                0 -> {
                    arrDescTransType.add("Transportation Type")
                    arrTransTypeCode.add("")
                }
                else -> {
                    arrDescTransType.add(listTransport[i].mTransTypeDescription.trim())
                    arrTransTypeCode.add(listTransport[i].mTransCode.trim())
                }
            }
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrDescTransType)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        activityRequestTravelBinding.spReqTravelTransType.adapter = adapter
        activityRequestTravelBinding.spReqTravelTransType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when{
                    position > 0 -> {
                        val code = arrTransTypeCode[position].trim()
                        activityRequestTravelBinding.viewModel?.stTransTypeCode?.set(code)
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun getTeamData() {
        val intent = Intent(this, TeamActivity::class.java)
        intent.putExtra(ConstantObject.extra_intent, ConstantObject.extra_fromIntentCreateTravel)
        startActivityForResult(intent, RESULT_SUCCESS_CODE_TEAM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_SUCCESS_CODE_TEAM -> {
                val intentTeamName : String = data?.getStringExtra(RESULT_EXTRA_TRAVEL_TEAM_NAME).toString()
                val intentTeamUserId : String = data?.getStringExtra(RESULT_EXTRA__TRAVEL_TEAM_USER_ID).toString()
                activityRequestTravelBinding.viewModel?.validateTravelTeam(intentTeamUserId, intentTeamName)
            }
            RESULT_SUCCESS_DESTINATION_FROM -> {
                val intentCityDepart : String = data?.getStringExtra(RESULT_EXTRA__TRAVEL_CITY_DESC).toString()
                when{
                    intentCityDepart != "null" -> activityRequestTravelBinding.viewModel?.stDepartFrom?.set(intentCityDepart)
                }
            }
            RESULT_SUCCESS_DESTINATION_INTO -> {
                val intentCityReturn : String = data?.getStringExtra(RESULT_EXTRA__TRAVEL_CITY_DESC).toString()
                when{
                    intentCityReturn != "null" -> activityRequestTravelBinding.viewModel?.validateCityReturn(intentCityReturn)
                }
            }
        }
    }

    override fun getDataDepart() {
        val intent = Intent(this, CityActivity::class.java)
        intent.putExtra(extra_city_intent, extra_city_intentDepart)
        startActivityForResult(intent, RESULT_SUCCESS_DESTINATION_FROM)
    }

    override fun getDataReturn() {
        val intent = Intent(this, CityActivity::class.java)
        intent.putExtra(extra_city_intent, extra_city_intentReturn)
        startActivityForResult(intent, RESULT_SUCCESS_DESTINATION_INTO)
    }

    override fun onSuccessRequestTravel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPositiveClick(o: Any) {
        when(keyDialogActive){
            ALERT_REQ_TRAVEL_CONFIRMATION -> onMessage("Test", ConstantObject.vToastInfo)
        }
    }

    override fun onNegativeClick(o: Any) {}

    companion object{
        const val ALERT_REQ_TRAVEL_NO_CONNECTION = 1
        const val ALERT_REQ_TRAVEL_CONFIRMATION = 5
        const val RESULT_SUCCESS_DESTINATION_FROM = 11
        const val RESULT_SUCCESS_DESTINATION_INTO = 22
        const val RESULT_SUCCESS_CODE_TEAM = 33
        const val RESULT_EXTRA_TRAVEL_TEAM_NAME = "team_name"
        const val RESULT_EXTRA__TRAVEL_TEAM_USER_ID = "team_user_id"
        const val RESULT_EXTRA_TRAVEL_CITY_CODE = "city_code"
        const val RESULT_EXTRA__TRAVEL_CITY_DESC = "city_description"
        const val RESULT_EXTRA__TRAVEL_COUNTRY_CODE = "city_code"

        const val extra_city_intent = "extra_city_intent"
        const val extra_city_intentDepart = "extra_city_intent_depart"
        const val extra_city_intentReturn = "extra_city_intent_return"

        const val chooseDateFrom = "date_from"
        const val chooseDateInto = "date_into"
    }
}
