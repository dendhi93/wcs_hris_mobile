package com.wcs.mobilehris.feature.preparation.splash

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.database.daos.*
import com.wcs.mobilehris.database.entity.ChargeCodeEntity
import com.wcs.mobilehris.database.entity.ReasonTravelEntity
import com.wcs.mobilehris.database.entity.TransportTypeEntity
import com.wcs.mobilehris.database.entity.UpdateMasterEntity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class SplashViewModel(private var _context : Context, private var _splashInterface: SplashInterface) : ViewModel() {

    val stErrDownload = ObservableField<String>("")
    val isPrgBarVisible = ObservableField<Boolean>(false)
    val isBtnVisible = ObservableField<Boolean>(false)
    private lateinit var mUpdateMasterDataDao : UpdateMasterDao
    private lateinit var mChargeCodeDao : ChargeCodeDao
    private lateinit var mTransTypeDao : TransTypeDao
    private lateinit var mReasonTravelDao : ReasonTravelDao
    private val arrJsonUpdateMaster = mutableListOf<UpdateMasterEntity>()
    private val arrJsonChargeCode = mutableListOf<ChargeCodeEntity>()
    private val arrJsonTransType = mutableListOf<TransportTypeEntity>()
    private val arrJsonReasonTravel = mutableListOf<ReasonTravelEntity>()
    private var countDataChargeCode = 0
    private var countDataTransType = 0
    private var countDataReasonTravel = 0
    private var transTime = DateTimeUtils.getCurrentTime()

    fun processDownload(){
        when{
            !ConnectionObject.isNetworkAvailable(_context) -> {
                _splashInterface.onAlertSplash(_context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection,SplashActivity.DIALOG_NO_INTERNET)
            }
            else -> validateMaster()
        }
    }

    private fun successDownload(){ _splashInterface.successSplash() }

    private fun validateMaster(){
        isPrgBarVisible.set(true)
        //arraylist master data dari json
        arrJsonUpdateMaster.add(UpdateMasterEntity(1,"mCharge_code",DateTimeUtils.getCurrentDate()))
        arrJsonUpdateMaster.add(UpdateMasterEntity(2,"mTrans_type",DateTimeUtils.getCurrentDate()))
        arrJsonUpdateMaster.add(UpdateMasterEntity(2,"mReason_Travel",DateTimeUtils.getCurrentDate()))

        //arraylist master chargecode dari json
        arrJsonChargeCode.add(ChargeCodeEntity(1, "A-1003-096",
            "BUSINESS DEVELOPMENT FOR MOBILITY ACTIVITY",
            "PT Wilmar Consultancy Services", "","",transTime))
        arrJsonChargeCode.add(ChargeCodeEntity(2, "A-1003-097",
            "HCM DEMO FOR PRESALES ACTIVITY",
            "PT Wilmar Consultancy Services", "","",transTime))
        arrJsonChargeCode.add(ChargeCodeEntity(3, "B-1014-001",
            "TRAINING FOR FRESH GRADUATE",
            "PT Wilmar Consultancy Services", "","",transTime))
        arrJsonChargeCode.add(ChargeCodeEntity(4, "B-1014-006",
            "TRAINING SAP - OUTSYSTEM",
            "","", "",transTime))
        arrJsonChargeCode.add(ChargeCodeEntity(5, "C-1003-006",
            "GENERAL MANAGEMENT INTL",
            "PT Wilmar Consultancy Services", "","",transTime))
        arrJsonChargeCode.add(ChargeCodeEntity(6, "C-1014-001",
            "GENERAL MANAGEMENT INTL - SALES FILLING AND DOCUMENTATION",
            "PT Wilmar Consultancy Services", "","",transTime))
        arrJsonChargeCode.add(ChargeCodeEntity(7, "D-1001-002",
            "ANNUAL LEAVE",
            "", "","",transTime))
        arrJsonChargeCode.add(ChargeCodeEntity(8, "D-1001-003",
            "SICK LEAVE",
            "", "","",transTime))
        arrJsonChargeCode.add(ChargeCodeEntity(9, "F-0014-017",
            "MILLS MOBILITY APPLICATION",
            "PT Heinz ABC", "Johnson","534343",transTime))
        arrJsonChargeCode.add(ChargeCodeEntity(10, "F-0014-018",
            "SAP IMPLEMENTATION TO LION SUPER INDO",
            "PT SUPER INDO", "Jesaja Waterkamp","634343",transTime))

        //arraylist master transtype from json
        arrJsonTransType.add(TransportTypeEntity(1, "BS", "BUS", transTime))
        arrJsonTransType.add(TransportTypeEntity(2, "CCR", "COMPANY CAR", transTime))
        arrJsonTransType.add(TransportTypeEntity(3, "COP", "COP VECHILE", transTime))
        arrJsonTransType.add(TransportTypeEntity(4, "PBL", "OTHER PUBLIC", transTime))
        arrJsonTransType.add(TransportTypeEntity(5, "OCR", "OWN CAR", transTime))
        arrJsonTransType.add(TransportTypeEntity(6, "PLN", "PLANE", transTime))
        arrJsonTransType.add(TransportTypeEntity(7, "SHP", "SHIP", transTime))
        arrJsonTransType.add(TransportTypeEntity(8, "TX", "TAXI", transTime))
        arrJsonTransType.add(TransportTypeEntity(9, "TRN", "TRAIN", transTime))
        arrJsonTransType.add(TransportTypeEntity(10, "TRV", "TRAVEL", transTime))

        //arraylist master reason from json
        arrJsonReasonTravel.add(ReasonTravelEntity(1, "IMPL", "Implementasi", transTime))
        arrJsonReasonTravel.add(ReasonTravelEntity(2, "AUDT", "Audit", transTime))
        arrJsonReasonTravel.add(ReasonTravelEntity(3, "MEET", "Meeting", transTime))
        arrJsonReasonTravel.add(ReasonTravelEntity(4, "TRA", "Training/Seminar/Workshop", transTime))
        arrJsonReasonTravel.add(ReasonTravelEntity(5, "VIS", "Customer Visit / Area Visit", transTime))
        arrJsonReasonTravel.add(ReasonTravelEntity(6, "EXHB", "Exhibition", transTime))
        arrJsonReasonTravel.add(ReasonTravelEntity(7, "SOC", "Social Responsibilities", transTime))
        arrJsonReasonTravel.add(ReasonTravelEntity(8, "ROU", "Routine Duty", transTime))
        arrJsonReasonTravel.add(ReasonTravelEntity(9, "OTH", "Others", transTime))

        mUpdateMasterDataDao = WcsHrisApps.database.updateMasterDao()
        mChargeCodeDao = WcsHrisApps.database.chargeCodeDao()
        mTransTypeDao = WcsHrisApps.database.transTypeDao()
        mReasonTravelDao = WcsHrisApps.database.reasonTravelDao()
        doAsync{
            val listUpdate = mUpdateMasterDataDao.getDataUpdate()
            when{
                listUpdate.isNotEmpty() -> validateDataMaster(listUpdate)
                else -> insertDataMaster()
            }
        }
    }
    //insert first time
    private fun insertDataMaster(){
        var tableMasters : UpdateMasterEntity
        for(i in arrJsonUpdateMaster.indices){
            tableMasters = UpdateMasterEntity(arrJsonUpdateMaster[i].id
                , arrJsonTransType[i].mTransTypeDescription.trim(),
                arrJsonUpdateMaster[i].mUpdateDate.trim())
            mUpdateMasterDataDao.insertUpdateMaster(tableMasters)
        }
        insertDeleteTableData(true, "")
    }

    private fun validateDataMaster(listData : List<UpdateMasterEntity>){
        for(i in listData.indices){
            val updatedDate = listData[i].mUpdateDate.trim()
            val tableDesc = listData[i].mTableDescription.trim()
            for(j in arrJsonUpdateMaster.indices){
                val jsonTableDesc = arrJsonUpdateMaster[i].mTableDescription
                val jsonUpdatedDate = arrJsonUpdateMaster[i].mUpdateDate
                when(tableDesc){
                    jsonTableDesc -> {
                        when{
                            updatedDate != jsonUpdatedDate -> { insertDeleteTableData(false, tableDesc) }
                        }
                    }
                }
            }
        }

        doAsync {
            countDataChargeCode = mChargeCodeDao.getCountChargeCode()
            countDataTransType = mTransTypeDao.getCountTransType()
            countDataReasonTravel = mReasonTravelDao.getCountReasonTravel()
            Log.d("###", "chargeCode qty $countDataChargeCode")
            Log.d("###", "transType qty $countDataTransType")
            Log.d("###", "reasonTravel qty $countDataReasonTravel")
            uiThread {
                when{
                    countDataChargeCode > 0 &&
                    countDataTransType > 0 &&
                    countDataReasonTravel > 0  -> successDownload()
                    else -> _splashInterface.onErrorMessage("Data not yet success to save ", ConstantObject.vToastError)
                }
            }
        }
    }

    //hrs diinsert pertable
    private fun insertDeleteTableData(isFirstInsert : Boolean, tableDesc : String){
        var insertChargeCodeModel : ChargeCodeEntity
        var insertTransTypeModel : TransportTypeEntity
        var insertReasonTravelModel : ReasonTravelEntity
        //insert charge code
        when(tableDesc){
            "mCharge_code" -> {
                doAsync {
                    mChargeCodeDao.deleteAllChargeCode()
                }
                for(i in arrJsonChargeCode.indices){
                    insertChargeCodeModel = ChargeCodeEntity(arrJsonChargeCode[i].id,
                        arrJsonChargeCode[i].mChargeCodeNo.trim(),
                        arrJsonChargeCode[i].mDescriptionChargeCode.trim(),
                        arrJsonChargeCode[i].mCompanyName.trim(),
                        arrJsonChargeCode[i].mProjectManagerName.trim(),
                        arrJsonChargeCode[i].mProjectManagerNik.trim(),
                        arrJsonChargeCode[i].mUpdateDate.trim())
                    mChargeCodeDao.insertChargeCode(insertChargeCodeModel)
                }
                Log.d("###","Success Insert Data Charge code")
            }
            "mTrans_type" -> {
                //insert transtype
                doAsync {
                    mTransTypeDao.deleteAllTransType()
                }
                for(j in arrJsonTransType.indices){
                    insertTransTypeModel = TransportTypeEntity(arrJsonTransType[j].id,
                        arrJsonTransType[j].mTransCode.trim(),
                        arrJsonTransType[j].mTransTypeDescription.trim(),
                        arrJsonTransType[j].mTransUpdateDate.trim())
                    mTransTypeDao.insertTransType(insertTransTypeModel)
                }
                Log.d("###","Success Insert Data Transtype")
            }
            "mReason_Travel" -> {
                doAsync {
                    mReasonTravelDao.deleteAllReasonTravel()
                }
                for(k in arrJsonReasonTravel.indices){
                    insertReasonTravelModel = ReasonTravelEntity(arrJsonReasonTravel[k].id,
                        arrJsonReasonTravel[k].mReasonCode.trim(),
                        arrJsonReasonTravel[k].mReasonDescription.trim(),
                        arrJsonReasonTravel[k].mReasonUpdateDate.trim()
                    )
                    mReasonTravelDao.insertReasonTravel(insertReasonTravelModel)
                }
                Log.d("###","Success Insert Data reason travel")
            }
            else -> {
                for(i in arrJsonChargeCode.indices){
                    insertChargeCodeModel = ChargeCodeEntity(arrJsonChargeCode[i].id,
                        arrJsonChargeCode[i].mChargeCodeNo.trim(),
                        arrJsonChargeCode[i].mDescriptionChargeCode.trim(),
                        arrJsonChargeCode[i].mCompanyName.trim(),
                        arrJsonChargeCode[i].mProjectManagerName.trim(),
                        arrJsonChargeCode[i].mProjectManagerNik.trim(),
                        arrJsonChargeCode[i].mUpdateDate.trim())
                    mChargeCodeDao.insertChargeCode(insertChargeCodeModel)
                }
                Log.d("###","Success Insert Data Transtype")
                for(j in arrJsonTransType.indices){
                    insertTransTypeModel = TransportTypeEntity(arrJsonTransType[j].id,
                        arrJsonTransType[j].mTransCode.trim(),
                        arrJsonTransType[j].mTransTypeDescription.trim(),
                        arrJsonTransType[j].mTransUpdateDate.trim())
                    mTransTypeDao.insertTransType(insertTransTypeModel)
                }
                Log.d("###","Success Insert Data Transtype")
                for(k in arrJsonReasonTravel.indices){
                    insertReasonTravelModel = ReasonTravelEntity(arrJsonReasonTravel[k].id,
                        arrJsonReasonTravel[k].mReasonCode.trim(),
                        arrJsonReasonTravel[k].mReasonDescription.trim(),
                        arrJsonReasonTravel[k].mReasonUpdateDate.trim()
                    )
                    mReasonTravelDao.insertReasonTravel(insertReasonTravelModel)
                }
                Log.d("###","Success Insert Data reason travel")
            }
        }

        doAsync {
            countDataChargeCode = mChargeCodeDao.getCountChargeCode()
            countDataTransType = mTransTypeDao.getCountTransType()
            countDataReasonTravel = mReasonTravelDao.getCountReasonTravel()
            uiThread {
                Log.d("###", "chargeCode qty $countDataChargeCode")
                Log.d("###", "transType qty $countDataTransType")
                Log.d("###", "reasonTravel qty $countDataReasonTravel")
                when{
                    countDataChargeCode > 0
                            && countDataTransType > 0
                            && countDataReasonTravel > 0
                            && isFirstInsert -> successDownload()
                    else -> _splashInterface.onErrorMessage("Data not yet success to save ", ConstantObject.vToastError)
                }
            }
        }
    }
}