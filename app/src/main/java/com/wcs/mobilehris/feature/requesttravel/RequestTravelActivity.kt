package com.wcs.mobilehris.feature.requesttravel

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.database.entity.ChargeCodeEntity
import com.wcs.mobilehris.database.entity.ReasonTravelEntity
import com.wcs.mobilehris.database.entity.TransportTypeEntity
import com.wcs.mobilehris.databinding.ActivityRequestTravelBinding
import com.wcs.mobilehris.feature.city.CityActivity
import com.wcs.mobilehris.feature.createtask.SelectedFriendInterface
import com.wcs.mobilehris.feature.dtltask.CustomDetailTaskAdapter
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.team.activity.TeamActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils


@Suppress("DEPRECATION", "UNREACHABLE_CODE",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class RequestTravelActivity : AppCompatActivity(), RequestTravelInterface,
    SelectedFriendInterface, SelectedTravelInterface {
    private lateinit var activityRequestTravelBinding : ActivityRequestTravelBinding
    private lateinit var travelAdapter : CustomDetailTaskAdapter
    private lateinit var citiesAdapter : CustomReqTravelAdapter
    private var arrTeamTravel = ArrayList<FriendModel>()
    private var arrCities = ArrayList<ReqTravelModel>()
    private var keyDialogActive = 0
    private var arrChargeCode = ArrayList<String>()
    private var arrDescTransType = ArrayList<String>()
    private var arrTransTypeCode = ArrayList<String>()
    private var arrReasonDesc = ArrayList<String>()
    private var arrReasonCode = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRequestTravelBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_travel)
        activityRequestTravelBinding.viewModel = RequestTravelViewModel(this, this, ApiRepo())
        activityRequestTravelBinding.rcReqTravelTeam.layoutManager = LinearLayoutManager(this)
        activityRequestTravelBinding.rcReqTravelCities.layoutManager = LinearLayoutManager(this)
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
            it.getDataReason()
        }
        activityRequestTravelBinding.viewModel?.initDataChargeCode()
        activityRequestTravelBinding.rcReqTravelTeam.setHasFixedSize(true)
        travelAdapter = CustomDetailTaskAdapter(this, arrTeamTravel, ConstantObject.vCreateEdit)
        travelAdapter.initSelectedTeamCallback(this)
        activityRequestTravelBinding.rcReqTravelTeam.adapter = travelAdapter

        activityRequestTravelBinding.rcReqTravelCities.setHasFixedSize(true)
        citiesAdapter = CustomReqTravelAdapter(this, arrCities, ConstantObject.vNotCreateEdit)
        citiesAdapter.initSelectedTravel(this)
        activityRequestTravelBinding.rcReqTravelCities.adapter = citiesAdapter
        onChangeButtonBackground(true)
        initReqTravelRadio()
    }

    private fun initReqTravelRadio(){
        activityRequestTravelBinding.rgReqTravelIsTB.setOnCheckedChangeListener{ group, checkedId ->
            val radio: RadioButton? = findViewById(checkedId)
            when("${radio?.text}"){
                getString(R.string.travel_business) -> activityRequestTravelBinding.viewModel?.isNonTB?.set(false)
                else -> activityRequestTravelBinding.viewModel?.isNonTB?.set(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onAlertReqTravel("Are you sure do you want to exit this transaction ?",
                    ConstantObject.vAlertDialogConfirmation, ALERT_REQ_TRAVEL_EXIT_CONF)
                return true
            }
            R.id.menu_save -> {
                activityRequestTravelBinding.viewModel?.submitSetTravel()
                return true
            }
            R.id.menu_set_travel -> {
                onAlertReqTravel(getString(R.string.transaction_alert_confirmation),
                    ConstantObject.vAlertDialogConfirmation, ALERT_REQ_TRAVEL_GENERATE_TRAVEL)
                return true
            }
            R.id.menu_edit -> {
                onAlertReqTravel(getString(R.string.transaction_alert_confirmation),
                    ConstantObject.vAlertDialogConfirmation, ALERT_REQ_TRAVEL_EDIT_TRAVEL)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        val inflater = menuInflater
        if(activityRequestTravelBinding.viewModel?.isTravelSelected?.get() == true){
            inflater.inflate(R.menu.req_travel_menu, menu)
        }else{
            inflater.inflate(R.menu.save_travel_menu, menu)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onLoadTeam(listTeam: List<FriendModel>) {
        arrTeamTravel.clear()
        arrTeamTravel.addAll(listTeam)
        travelAdapter.notifyDataSetChanged()
        activityRequestTravelBinding.viewModel?.onCopyListTeam(arrTeamTravel)
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
            ALERT_REQ_TRAVEL_SET_TRAVEL -> {
                keyDialogActive = ALERT_REQ_TRAVEL_SET_TRAVEL
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_REQ_TRAVEL_EDIT_TRAVEL -> {
                keyDialogActive = ALERT_REQ_TRAVEL_EDIT_TRAVEL
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_REQ_TRAVEL_GENERATE_TRAVEL -> {
                keyDialogActive = ALERT_REQ_TRAVEL_GENERATE_TRAVEL
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
            ALERT_REQ_TRAVEL_EXIT_CONF -> {
                keyDialogActive = ALERT_REQ_TRAVEL_EXIT_CONF
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
        var pos = 0
        for(i in listTransport.indices){
            when(pos) {
                0 -> {
                    arrDescTransType.add("Transportation Type")
                    arrTransTypeCode.add("")
                    arrDescTransType.add(listTransport[i].mTransTypeDescription.trim())
                    arrTransTypeCode.add(listTransport[i].mTransCode.trim())
                }
                else -> {
                    arrDescTransType.add(listTransport[i].mTransTypeDescription.trim())
                    arrTransTypeCode.add(listTransport[i].mTransCode.trim())
                }
            }
            pos += 1
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrDescTransType)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        activityRequestTravelBinding.spReqTravelTransType.adapter = adapter
        activityRequestTravelBinding.spReqTravelTransType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when{
                    position > 0 -> {
                        val code = arrTransTypeCode[position].trim()
                        val descTrans = arrDescTransType[position].trim()
                        activityRequestTravelBinding.viewModel?.stTransTypeCode?.set("$code-$descTrans")
                        onHideSoftKeyboard()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun onLoadReason(listReason: List<ReasonTravelEntity>) {
        var reasonPos = 0
        for(i in listReason.indices){
            when(reasonPos) {
                0 -> {
                    arrReasonCode.add("")
                    arrReasonDesc.add("Reason Type")
                    arrReasonCode.add(listReason[i].mReasonCode.trim())
                    arrReasonDesc.add(listReason[i].mReasonDescription.trim())
                }
                else -> {
                    arrReasonCode.add(listReason[i].mReasonCode.trim())
                    arrReasonDesc.add(listReason[i].mReasonDescription.trim())
                }
            }
            reasonPos += 1
        }
        val reasonAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrReasonDesc)
        reasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        activityRequestTravelBinding.spReqTravelReason.adapter = reasonAdapter
        activityRequestTravelBinding.spReqTravelReason.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when{
                    position > 0 -> {
                        val reasonCode = arrReasonCode[position].trim()
                        Log.d("###", "reasonCode $reasonCode")
                        activityRequestTravelBinding.viewModel?.stReasonCode?.set(reasonCode)
                        onHideSoftKeyboard()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun getTeamData() {
        val travelDepartDate = activityRequestTravelBinding.viewModel?.stDepartDate?.get()
        val travelReturnDate = activityRequestTravelBinding.viewModel?.stReturnDate?.get()
        if(travelDepartDate == "" || travelReturnDate == ""){
            onMessage("Please Fill depart or Return Date", ConstantObject.vToastInfo)
        }else{
            val intent = Intent(this, TeamActivity::class.java)
            intent.putExtra(ConstantObject.extra_intent, ConstantObject.extra_fromIntentCreateTravel)
            intent.putExtra(ConstantObject.extra_dateFrom_intent, travelDepartDate?.trim())
            intent.putExtra(ConstantObject.extra_dateInto_intent, travelReturnDate?.trim())
            startActivityForResult(intent, RESULT_SUCCESS_CODE_TEAM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_SUCCESS_CODE_TEAM -> {
                data?.let {
                    val intentTeamName : String = it.getStringExtra(RESULT_EXTRA_TRAVEL_TEAM_NAME).toString()
                    val intentTeamUserId : String = it.getStringExtra(RESULT_EXTRA_TRAVEL_TEAM_USER_ID).toString()
                    val intentTeamStatus : String = it.getStringExtra(RESULT_EXTRA_TRAVEL_STATUS_TEAM).toString()
                    activityRequestTravelBinding.viewModel?.validateTravelTeam(intentTeamUserId, intentTeamName,intentTeamStatus)
                }
            }
            RESULT_SUCCESS_DESTINATION_FROM -> {
                val intentCityDepart : String = data?.getStringExtra(RESULT_EXTRA_TRAVEL_CITY_DESC).toString()
                when{intentCityDepart != "null" -> activityRequestTravelBinding.viewModel?.stDepartFrom?.set(intentCityDepart) }
            }
            RESULT_SUCCESS_DESTINATION_INTO -> {
                val intentCityReturn : String = data?.getStringExtra(RESULT_EXTRA_TRAVEL_CITY_DESC).toString()
                when{
                    intentCityReturn != "null" -> { activityRequestTravelBinding.viewModel?.validateCityReturn(intentCityReturn) }
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
        onMessage(getString(R.string.alert_transaction_success), ConstantObject.vToastSuccess)
        activityRequestTravelBinding.viewModel?.isProgressReqTravel?.set(false)
        finish()
    }

    override fun onHideSoftKeyboard() {
        val inputManager:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onChangeButtonBackground(isTravelSelected: Boolean) {
        when{
            isTravelSelected -> {
                activityRequestTravelBinding.btnCreateTaskSetTravel.setBackgroundColor(resources.getColor(R.color.colorBlueMantab))
                activityRequestTravelBinding.btnCreateTaskSetTravel.setTextColor(Color.WHITE)
                activityRequestTravelBinding.btnCreateTaskAddAccomodation.setBackgroundColor(resources.getColor(R.color.colorWhite))
                activityRequestTravelBinding.btnCreateTaskAddAccomodation.setTextColor(Color.BLACK)
                activityRequestTravelBinding.viewModel?.stAddTeamButton?.set(getString(R.string.add_team))
            }
            else -> {
                activityRequestTravelBinding.btnCreateTaskSetTravel.setBackgroundColor(resources.getColor(R.color.colorWhite))
                activityRequestTravelBinding.btnCreateTaskSetTravel.setTextColor(Color.BLACK)
                activityRequestTravelBinding.btnCreateTaskAddAccomodation.setBackgroundColor(resources.getColor(R.color.colorBlueMantab))
                activityRequestTravelBinding.btnCreateTaskAddAccomodation.setTextColor(Color.WHITE)
                activityRequestTravelBinding.viewModel?.stAddTeamButton?.set(getString(R.string.add_destination))
            }
        }
    }

    override fun onLoadCitiesTravel(listCity: List<ReqTravelModel>) {
        when(arrCities.size){
            0 -> {
                arrCities.addAll(listCity)
                citiesAdapter.notifyDataSetChanged()
            }
            else -> {
                val selectedCityModel = listCity[0]
                //ngecek data yang sama di array
                val isMatch = arrCities.contains(selectedCityModel)
                if(isMatch){ onMessage("Data already on the list", ConstantObject.vToastInfo) }
                else{
                    arrCities.addAll(listCity)
                    citiesAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onResetCities() {
        arrCities.clear()
        citiesAdapter.notifyDataSetChanged()
    }

    override fun onShowConflict(listConflicted: List<ConflictedFriendModel>) {
        val adConflict = AlertDialog.Builder(this)
        adConflict.setTitle("INFO")
        adConflict.setIcon(R.mipmap.ic_logo_wcs)
        val arrayAdapter = ArrayAdapter<String>(this@RequestTravelActivity, android.R.layout.select_dialog_item)
        for(i in listConflicted.indices){
            arrayAdapter.add(listConflicted[i].custName+"\n"+
                    listConflicted[i].dateFrom+" - "+
                    listConflicted[i].dateInto)
        }
        adConflict.setAdapter(arrayAdapter, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {}
        })
        adConflict.show()
    }


    override fun onPositiveClick(o: Any) {
        when(keyDialogActive){
            ALERT_REQ_TRAVEL_CONFIRMATION -> activityRequestTravelBinding.viewModel?.actionSubmitConfirmTravel()
            ALERT_REQ_TRAVEL_SET_TRAVEL -> activityRequestTravelBinding.viewModel?.actionSetTravel()
            ALERT_REQ_TRAVEL_EDIT_TRAVEL -> activityRequestTravelBinding.viewModel?.actionEditTravel(arrCities)
            ALERT_REQ_TRAVEL_GENERATE_TRAVEL -> activityRequestTravelBinding.viewModel?.actionGenerateTravel(arrCities)
            ALERT_REQ_TRAVEL_EXIT_CONF -> {
                activityRequestTravelBinding.viewModel?.onBackReqTravelMenu()
                finish()
            }
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).setTitle(getString(R.string.confirmation_menu))
            .setMessage("Are you sure do you want to exit this transaction ? ")
            .setPositiveButton(android.R.string.ok){
                    dialog, _ ->
                activityRequestTravelBinding.viewModel?.onBackReqTravelMenu()
                finish()
                super.onBackPressed()
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel){
                    dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onNegativeClick(o: Any) {}
    override fun selectedItemFriend(friendModel: FriendModel) {
        when{
            activityRequestTravelBinding.viewModel?.isSetTravel?.get() == false ->{
                arrTeamTravel.remove(friendModel)
                travelAdapter.notifyDataSetChanged()
            }
            else -> onMessage("Request Travel was set, transaction not allowed", ConstantObject.vToastInfo)
        }
    }

    override fun selectedDisplayFriend(friendModel: FriendModel) {
        //todo search conflicted
        activityRequestTravelBinding.viewModel?.getConflictedFriend(friendModel.friendId.trim())
    }

    override fun selectedItemTravel(travelModel: ReqTravelModel) {
        arrCities.remove(travelModel)
        citiesAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
        when(activityRequestTravelBinding.viewModel?.isTravelSelected?.get()){
            true -> onChangeButtonBackground(true)
            else -> onChangeButtonBackground(false)
        }
    }


    companion object{
        const val ALERT_REQ_TRAVEL_NO_CONNECTION = 1
        const val ALERT_REQ_TRAVEL_CONFIRMATION = 5
        const val ALERT_REQ_TRAVEL_SET_TRAVEL = 7
        const val ALERT_REQ_TRAVEL_EDIT_TRAVEL = 9
        const val ALERT_REQ_TRAVEL_GENERATE_TRAVEL = 13
        const val ALERT_REQ_TRAVEL_EXIT_CONF = 15
        const val RESULT_SUCCESS_DESTINATION_FROM = 11
        const val RESULT_SUCCESS_DESTINATION_INTO = 22
        const val RESULT_SUCCESS_CODE_TEAM = 33
        const val RESULT_EXTRA_TRAVEL_TEAM_NAME = "team_name"
        const val RESULT_EXTRA_TRAVEL_TEAM_USER_ID = "team_user_id"
        const val RESULT_EXTRA_TRAVEL_STATUS_TEAM = "status_team_travel"
        const val RESULT_EXTRA_TRAVEL_CITY_CODE = "city_code"
        const val RESULT_EXTRA_TRAVEL_CITY_DESC = "city_description"
        const val RESULT_EXTRA_TRAVEL_COUNTRY_CODE = "city_code"
        const val extra_city_intent = "extra_city_intent"
        const val extra_city_intentDepart = "extra_city_intent_depart"
        const val extra_city_intentReturn = "extra_city_intent_return"
        const val extra_intentChargeCode = "extra_charge_code"

        const val chooseDateFrom = "date_from"
        const val chooseDateInto = "date_into"
        const val chooseDateCheckIn = "date_check_in"
        const val chooseDateCheckOut = "date_check_out"
    }
}
