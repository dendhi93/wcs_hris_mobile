package com.wcs.mobilehris.feature.preparation.splash

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ApiRepo.ApiCallback
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.database.daos.*
import com.wcs.mobilehris.database.entity.*
import com.wcs.mobilehris.util.ConstantObject
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject


class SplashViewModel(private var _context : Context,
                      private var _splashInterface: SplashInterface,
                      private var apiRepo: ApiRepo) : ViewModel() {

    val stErrDownload = ObservableField("")
    val isPrgBarVisible = ObservableField(false)
    val isBtnVisible = ObservableField(false)
    private val arrJsonUpdateMaster = mutableListOf<UpdateMasterEntity>()
    private lateinit var mUpdateMasterDataDao : UpdateMasterDao
    private lateinit var mChargeCodeDao : ChargeCodeDao
    private lateinit var mTransTypeDao : TransTypeDao
    private lateinit var mReasonTravelDao : ReasonTravelDao
    private lateinit var mReasonLeaveDao : ReasonLeaveDao
    private var countDataChargeCode = 0
    private var countDataTransType = 0
    private var countDataReasonTravel = 0
    private var countDataReasonLeave = 0
    private var TAG = "Splash"

    fun validateUpdateMaster(){
        mUpdateMasterDataDao = WcsHrisApps.database.updateMasterDao()
        mChargeCodeDao = WcsHrisApps.database.chargeCodeDao()
        mTransTypeDao = WcsHrisApps.database.transTypeDao()
        mReasonTravelDao = WcsHrisApps.database.reasonTravelDao()
        mReasonLeaveDao = WcsHrisApps.database.reasonLeaveDao()
        isBtnVisible.set(false)

        doAsync{
            val listUpdate = mUpdateMasterDataDao.getDataUpdate()
            when{
                listUpdate.isNotEmpty() -> processDownload(false)
                else -> processDownload(true)
            }
        }
    }

    fun retryDownload(){ processDownload(false) }

    private fun processDownload(isFirstInsert : Boolean){
        when{
            !ConnectionObject.isNetworkAvailable(_context) -> _splashInterface.onAlertSplash(_context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection,SplashActivity.DIALOG_NO_INTERNET)
            else -> getDataUpdateDate(ConstantObject.keyChargeCode, isFirstInsert)
        }
    }

    private fun getDataUpdateDate(typeData : String, isFirst: Boolean){
        isPrgBarVisible.set(true)
        apiRepo.getUpdateTableMaster(typeData,_context, object : ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseData = it.getString(ConstantObject.vResponseResult)
                    val jObjDateMaster = JSONObject(responseData)
                    val updateDateTable = jObjDateMaster.getString("CREATED_DT")
                    val descTable = jObjDateMaster.getString("DESC")
                    when(typeData){
                        ConstantObject.keyChargeCode -> {
                            when{
                                isFirst -> {
                                    doAsync {
                                        val maxId = mUpdateMasterDataDao.getDataUpdateMaxId()
                                        val updateModel = UpdateMasterEntity(maxId+1, ConstantObject.keyChargeCode, updateDateTable)
                                        mUpdateMasterDataDao.insertUpdateMaster(updateModel)
                                        uiThread {
                                            Log.d("###","success insert date charge code" )
                                            getDataUpdateDate(ConstantObject.keyTransType, isFirst)
                                        }
                                    }
                                }
                                else -> {
                                    arrJsonUpdateMaster.add(UpdateMasterEntity(1, descTable, updateDateTable))
                                    getDataUpdateDate(ConstantObject.keyTransType, isFirst)
                                }

                            }
                        }
                        ConstantObject.keyTransType -> {
                            when{
                                isFirst -> {
                                    doAsync {
                                        val maxId = mUpdateMasterDataDao.getDataUpdateMaxId()
                                        val updateModel = UpdateMasterEntity(maxId+1, ConstantObject.keyTransType, updateDateTable)
                                        mUpdateMasterDataDao.insertUpdateMaster(updateModel)
                                        uiThread {
                                            Log.d("###","success insert date trans type" )
                                            getDataUpdateDate(ConstantObject.keyReasonTravel, isFirst)
                                        }
                                    }
                                }
                                else -> {
                                    arrJsonUpdateMaster.add(UpdateMasterEntity(2, descTable, updateDateTable))
                                    getDataUpdateDate(ConstantObject.keyReasonTravel, isFirst)
                                }
                            }
                        }
                        ConstantObject.keyReasonTravel -> {
                            when{
                                isFirst -> {
                                    doAsync {
                                        val maxId = mUpdateMasterDataDao.getDataUpdateMaxId()
                                        val updateModel = UpdateMasterEntity(maxId+1, ConstantObject.keyReasonTravel , updateDateTable)
                                        mUpdateMasterDataDao.insertUpdateMaster(updateModel)
                                        uiThread {
                                            Log.d("###","success insert date reason travel")
                                            getDataUpdateDate(ConstantObject.keyLeaveType, isFirst)
                                        }
                                    }
                                }
                                else ->{
                                    arrJsonUpdateMaster.add(UpdateMasterEntity(3, descTable, updateDateTable))
                                    getDataUpdateDate(ConstantObject.keyLeaveType, isFirst)
                                }
                            }
                        }
                        else -> {
                            when{
                                isFirst -> {
                                    doAsync {
                                        val maxId = mUpdateMasterDataDao.getDataUpdateMaxId()
                                        val updateModel = UpdateMasterEntity(maxId+1, ConstantObject.keyLeaveType, updateDateTable)
                                        mUpdateMasterDataDao.insertUpdateMaster(updateModel)
                                        uiThread {
                                            Log.d("###","success insert date leave" )
                                            processGetDataMaster("getlatestmasterchargecode", true)
                                        }
                                    }
                                }
                                else -> {
                                    arrJsonUpdateMaster.add(UpdateMasterEntity(4, descTable, updateDateTable))
                                    validateToTableUpdate(arrJsonUpdateMaster)
                                }
                            }
                        }
                    }
                }
            }

            override fun onDataError(error: String?) {
                Log.d(TAG, " err $error")
                _splashInterface.onErrorMessage(" err on " + typeData +
                        "\n" +error.toString().trim(), ConstantObject.vToastInfo)
                isPrgBarVisible.set(false)
            }
        })
    }

    private fun processGetDataMaster(masterType : String, isFirstInsert : Boolean){
        apiRepo.getDataMaster(masterType, _context, object  : ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseData = it.getString(ConstantObject.vResponseResult)
                    val jArrayDateMaster = JSONArray(responseData)
                    when(masterType){
                        "getlatestmasterchargecode" -> {
                            doAsync {
                                for (i in 0 until jArrayDateMaster.length()) {
                                    val responseDataChargeCode = jArrayDateMaster.getJSONObject(i)
                                    val chargeCodeModel = ChargeCodeEntity(i+1,
                                        responseDataChargeCode.getString("CHARGE_CD"),
                                        responseDataChargeCode.getString("CHARGE_NAME"),
                                        responseDataChargeCode.getString("CUSTOMER_NAME"),
                                        responseDataChargeCode.getString("PM_NAME"),
                                        responseDataChargeCode.getString("WCS_PM_NIK"),
                                        responseDataChargeCode.getString("VALID_FROM"),
                                        responseDataChargeCode.getString("VALID_TO"),
                                        responseDataChargeCode.getString("CREATED_DT")
                                    )
                                    mChargeCodeDao.insertChargeCode(chargeCodeModel)
                                }
                                uiThread {
                                    Log.d("###","success insert charge code")
                                    if(isFirstInsert){
                                        processGetDataMaster("getlatestmastertransport",true)
                                    }else{Log.d("###","1")}
                                }
                            }
                        }
                        "getlatestmastertransport" -> {
                            doAsync {
                                for (j in 0 until jArrayDateMaster.length()) {
                                    val responseDataTransport = jArrayDateMaster.getJSONObject(j)
                                    val transportModel = TransportTypeEntity(j+1,
                                        responseDataTransport.getString("TRANSPORT_CODE"),
                                        responseDataTransport.getString("TRANSPORT_NAME"),
                                        responseDataTransport.getString("CREATED_DT")
                                    )
                                    mTransTypeDao.insertTransType(transportModel)
                                }
                                uiThread {
                                    Log.d("###","success insert trans type")
                                    if(isFirstInsert){ processGetDataMaster("getlatestmasterreason",true)
                                    }else {Log.d("###","2")}
                                }
                            }
                        }
                        "getlatestmasterreason" ->{
                            doAsync {
                                for(m in 0 until jArrayDateMaster.length()){
                                    val responseDatamReason = jArrayDateMaster.getJSONObject(m)
                                    val reasonTravelModel = ReasonTravelEntity(
                                        m+1,
                                        responseDatamReason.getString("REASON_CODE"),
                                        responseDatamReason.getString("REASON_NAME"),
                                        responseDatamReason.getString("CREATED_DT")
                                    )
                                    mReasonTravelDao.insertReasonTravel(reasonTravelModel)
                                    uiThread {
                                        Log.d("###","success insert reason travel")
                                        if(isFirstInsert){ processGetDataMaster("getlatestmasterleavetype",true) }
                                    }
                                }
                            }
                        }
                        else -> {
                            doAsync {
                                for (k in 0 until jArrayDateMaster.length()) {
                                    val responseDataLeaveType = jArrayDateMaster.getJSONObject(k)
                                    val responseLeaveModel = ReasonLeaveEntity(
                                        k+1,
                                        responseDataLeaveType.getString("CHARGE_CD"),
                                        responseDataLeaveType.getString("LEAVE_TYPE_NAME"),
                                        responseDataLeaveType.getString("CREATED_DT")
                                    )
                                    mReasonLeaveDao.insertReasonLeave(responseLeaveModel)
                                }
                                uiThread {
                                    Log.d("###","success insert leave")
                                    successDownload()
                                }
                            }
                        }
                    }
                }
            }

            override fun onDataError(error: String?) {
                Log.d(TAG, " err $error")
                val errMessage = error.toString().trim()
                if (errMessage.contains("java.net.SocketTimeoutException")){
                    _splashInterface.onErrorMessage(" err on " + masterType +
                            "\nConnection Time Out, Please Try again", ConstantObject.vToastError)
                }else {
                    _splashInterface.onErrorMessage(" err on " + masterType +
                            "\n" +error.toString().trim(), ConstantObject.vToastError)
                }
                Log.d("###","err " +error.toString())
                isPrgBarVisible.set(false)
                isBtnVisible.set(true)
            }
        })
    }

    private fun successDownload(){
        doAsync {
            countDataChargeCode = mChargeCodeDao.getCountChargeCode()
            countDataTransType = mTransTypeDao.getCountTransType()
            countDataReasonTravel = mReasonTravelDao.getCountReasonTravel()
            countDataReasonLeave = mReasonLeaveDao.getCountReasonLeave()
            Log.d("###", "chargeCode qty $countDataChargeCode")
            Log.d("###", "transType qty $countDataTransType")
            Log.d("###", "reasonTravel qty $countDataReasonTravel")
            Log.d("###", "reasonLeave qty $countDataReasonLeave")
            uiThread {
                when{
                    countDataChargeCode > 0 &&
                            countDataTransType > 0 &&
                            countDataReasonTravel > 0 &&
                            countDataReasonLeave > 0 -> _splashInterface.successSplash()
                    else -> {
                        _splashInterface.onErrorMessage("Master Data not yet success to save.Please try again ", ConstantObject.vToastError)
                        isPrgBarVisible.set(false)
                        isBtnVisible.set(true)
                    }
                }
            }
        }
    }

    private fun validateToTableUpdate(jsonList : List<UpdateMasterEntity>){
        when{
            jsonList.isNotEmpty() -> {
                doAsync {
                    for(i in jsonList.indices){
                        val listTable = mUpdateMasterDataDao.getDataByDesc(jsonList[i].mTableDescription)
                        when(listTable[0].mUpdateDate){
                             jsonList[i].mUpdateDate -> {
                                when(i){
                                    3  -> successDownload()
                                    else -> Log.d("###","" +i)
                                }
                            }
                            else -> {
                                when{
                                    !ConnectionObject.isNetworkAvailable(_context)-> _splashInterface.onAlertSplash(_context.getString(R.string.alert_no_connection),
                                    ConstantObject.vAlertDialogNoConnection,SplashActivity.DIALOG_NO_INTERNET)
                                    else -> {
                                        when(jsonList[i].mTableDescription){
                                            ConstantObject.keyChargeCode ->{
                                                doAsync {
                                                    mChargeCodeDao.deleteAllChargeCode()
                                                }
                                             processGetDataMaster("getlatestmasterchargecode", false)
                                            }
                                            ConstantObject.keyTransType ->{
                                                doAsync {
                                                    mTransTypeDao.deleteAllTransType()
                                                }
                                                processGetDataMaster("getlatestmastertransport", false)
                                            }
                                            ConstantObject.keyReasonTravel -> {
                                                doAsync {
                                                    mReasonTravelDao.deleteAllReasonTravel()
                                                }
                                                processGetDataMaster("getlatestmasterreason", false)
                                            }
                                            else -> {
                                                doAsync {
                                                    mReasonLeaveDao.deleteAllReasonLeave()
                                                }
                                                processGetDataMaster("getlatestmasterleavetype", false)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}