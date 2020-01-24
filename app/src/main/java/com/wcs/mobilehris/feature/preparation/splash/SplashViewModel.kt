package com.wcs.mobilehris.feature.preparation.splash

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.database.daos.ChargeCodeDao
import com.wcs.mobilehris.database.daos.UpdateMasterDao
import com.wcs.mobilehris.database.entity.ChargeCodeEntity
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
    private val arrJsonUpdateMaster = mutableListOf<UpdateMasterEntity>()
    private val arrJsonChargeCode = mutableListOf<ChargeCodeEntity>()
    private val arrMasterDataTable = ArrayList<UpdateMasterEntity>()
    private var countData = 0

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

        //arraylist master chargecode dari json
        arrJsonChargeCode.add(ChargeCodeEntity(1, "A-1003-096",
            "BUSINESS DEVELOPMENT FOR MOBILITY ACTIVITY",
            "PT Wilmar Consultancy Services", DateTimeUtils.getCurrentTime()))
        arrJsonChargeCode.add(ChargeCodeEntity(2, "A-1003-097",
            "HCM DEMO FOR PRESALES ACTIVITY",
            "PT Wilmar Consultancy Services", DateTimeUtils.getCurrentTime()))
        arrJsonChargeCode.add(ChargeCodeEntity(3, "B-1014-001",
            "TRAINING FOR FRESH GRADUATE",
            "PT Wilmar Consultancy Services", DateTimeUtils.getCurrentTime()))
        arrJsonChargeCode.add(ChargeCodeEntity(4, "B-1014-006",
            "TRAINING SAP - OUTSYSTEM",
            "", DateTimeUtils.getCurrentTime()))
        arrJsonChargeCode.add(ChargeCodeEntity(5, "C-1003-006",
            "GENERAL MANAGEMENT INTL",
            "PT Wilmar Consultancy Services", DateTimeUtils.getCurrentTime()))
        arrJsonChargeCode.add(ChargeCodeEntity(6, "C-1014-001",
            "GENERAL MANAGEMENT INTL - SALES FILLING AND DOCUMENTATION",
            "PT Wilmar Consultancy Services", DateTimeUtils.getCurrentTime()))
        arrJsonChargeCode.add(ChargeCodeEntity(7, "D-1001-002",
            "ANNUAL LEAVE",
            "", DateTimeUtils.getCurrentTime()))
        arrJsonChargeCode.add(ChargeCodeEntity(8, "D-1001-003",
            "SICK LEAVE",
            "", DateTimeUtils.getCurrentTime()))
        arrJsonChargeCode.add(ChargeCodeEntity(9, "F-0014-017",
            "MILLS MOBILITY APPLICATION",
            "PT Heinz ABC", DateTimeUtils.getCurrentTime()))
        arrJsonChargeCode.add(ChargeCodeEntity(10, "F-0014-018",
            "SAP IMPLEMENTATION TO LION SUPER INDO",
            "PT SUPER INDO", DateTimeUtils.getCurrentTime()))

        mUpdateMasterDataDao = WcsHrisApps.database.updateMasterDao()
        mChargeCodeDao = WcsHrisApps.database.chargeCodeDao()
        doAsync{
            val listUpdate = mUpdateMasterDataDao.getDataUpdate()
            when{
                listUpdate.isNotEmpty() -> validateDataMaster(arrMasterDataTable)
                else -> insertDataMaster()
            }
        }
    }
    //insert first time
    private fun insertDataMaster(){
        val tableMasters = UpdateMasterEntity(1, "mCharge_code",DateTimeUtils.getCurrentDate())
        mUpdateMasterDataDao.insertUpdateMaster(tableMasters)
        insertChargeCode(true)
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
                            updatedDate != jsonUpdatedDate -> {
                                //todo delete and insert charge code
                                doAsync {
                                    mChargeCodeDao.deleteAllChargeCode()
                                    insertChargeCode(false)
                                }
                            }
                            else -> Log.d("###","ke else")
                        }
                    }
                }
            }
        }
        doAsync {
            countData = mChargeCodeDao.getCountChargeCode()
            uiThread { when{countData > 0  -> successDownload() } }
        }
    }

    private fun insertChargeCode(isFirstInsert : Boolean){
        var insertChargeCodeModel : ChargeCodeEntity? = null
        for(i in arrJsonChargeCode.indices){
            insertChargeCodeModel = ChargeCodeEntity(arrJsonChargeCode[i].id,
                arrJsonChargeCode[i].mChargeCodeNo.trim(),
                arrJsonChargeCode[i].mDescriptionChargeCode.trim(),
                arrJsonChargeCode[i].mCompanyName.trim(),
                arrJsonChargeCode[i].mUpdateDate.trim())
            mChargeCodeDao.insertChargeCode(insertChargeCodeModel)
        }
        doAsync {
            countData = mChargeCodeDao.getCountChargeCode()
            uiThread {
                when{
                    countData > 0 && isFirstInsert -> successDownload()
                }
            }
        }
    }
}