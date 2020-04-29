package com.wcs.mobilehris.feature.benefitclaim.list.listDtl

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.database.daos.BenefitDtlDao
import com.wcs.mobilehris.database.entity.BenefitDtlEntity
import com.wcs.mobilehris.feature.benefitclaim.trans.BenefitTransActivity
import com.wcs.mobilehris.util.ConstantObject
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class BenefitDtlListViewModel(private val context: Context,
                              private val benefitListDtlInterface: BenefitDtlInterface) : ViewModel(){
    val isVisibleBottomLinear = ObservableField(false)
    val isVisibleBenefitDtlProgress = ObservableField(false)
    val stBenefitDocNo = ObservableField("")
    val stBenefitRejectNotes = ObservableField("")
    val stBenefitFrom = ObservableField("")
    val isVisibleRecyler = ObservableField(false)
    val isVisibleBenefitFab = ObservableField(false)
    private val mutableBenefitDtlList = mutableListOf<BenefitDtlModel>()
    private lateinit var benefitDtlDao: BenefitDtlDao

    fun validateDataBenefitDtl(transType : String, intentFrom : String){
        benefitDtlDao = WcsHrisApps.database.benefitDtlDao()
        doAsync {
            val count = benefitDtlDao.getCountBenefitDtl()
            if(count > 0){
                val docNoBefore = benefitDtlDao.getBenefitDocData(stBenefitDocNo.get().toString().trim())
                if(stBenefitDocNo.get().toString().trim() != docNoBefore){
                    benefitDtlDao.deleteAlltBenefitDtl()
                    Log.d("###","deleted")
                }
            }
            when{
                !ConnectionObject.isNetworkAvailable(context) ->
                    benefitListDtlInterface.onAlertBenefitList(context.getString(R.string.alert_no_connection),
                        ConstantObject.vAlertDialogNoConnection, BenefitDtlListActivity.ALERT_BENEFITDTL_LIST_NO_CONNECTION)
                else -> getBenefDtlData(intentFrom, transType)
            }
        }
    }

    private fun getBenefDtlData(intentFrom : String, transBenefitType : String){
        mutableBenefitDtlList.clear()
        var benefitDtlModel : BenefitDtlModel
        var benefitDtlEntity : BenefitDtlEntity
        when(intentFrom){
            ConstantObject.extra_fromIntentRequest ->{
                isVisibleBottomLinear.set(false)
                isVisibleBenefitFab.set(true)
                if(transBenefitType.trim() != ConstantObject.vNew){
                    doAsync {
                        val count = benefitDtlDao.getCountBenefitDtl()
                        Log.d("###_2",""+count)
                        if(count == 0){
                            benefitDtlEntity = BenefitDtlEntity(1, "2020-04-15",
                                "MEDICAL","RAWAT JALAN",
                                "BUDI","100.000 IDR",
                                "100.000 IDR","ISPA","ISPA",
                                stBenefitDocNo.get().toString().trim())
                            benefitDtlDao.insertBenfitDtl(benefitDtlEntity)

                            benefitDtlEntity = BenefitDtlEntity(2, "2020-04-15",
                                "MEDICAL","RAWAT JALAN",
                                "BUDI","100.000 IDR",
                                "100.000 IDR","ISPA","ISPA",
                                stBenefitDocNo.get().toString().trim())
                            benefitDtlDao.insertBenfitDtl(benefitDtlEntity)
                        }

                        val mutableListDbBenfDtl = benefitDtlDao.getBenefitByDoc(stBenefitDocNo.get().toString().trim())
                        if(mutableListDbBenfDtl.isNotEmpty()){
                            for(i in mutableListDbBenfDtl.indices){
                                benefitDtlModel = BenefitDtlModel(""+(i+1).toString(),mutableListDbBenfDtl[i].tBenefitDtlDate,
                                    mutableListDbBenfDtl[i].tBenefitType, mutableListDbBenfDtl[i].tBenefitName,
                                    mutableListDbBenfDtl[i].tPersonalBenefit, mutableListDbBenfDtl[i].tAmountClaim,
                                    mutableListDbBenfDtl[i].tPaidClaim, mutableListDbBenfDtl[i].tDiagnoseDisease,mutableListDbBenfDtl[i].tBenefitDescription)
                                mutableBenefitDtlList.add(benefitDtlModel)
                            }
                        }
                        uiThread {
                            if (mutableBenefitDtlList.size > 0){
                                isVisibleBenefitDtlProgress.set(true)
                                benefitListDtlInterface.onLoadBenefitDtlList(mutableBenefitDtlList)
                            }
                        }
                    }
                }
            }
            else ->{
                isVisibleBottomLinear.set(true)
                isVisibleBenefitFab.set(false)
                doAsync {
                    val count = benefitDtlDao.getCountBenefitDtl()
                    if(count == 0){
                        benefitDtlEntity = BenefitDtlEntity(1, "2020-04-15",
                            "MEDICAL","RAWAT JALAN",
                            "BUDI","100.000 IDR",
                            "100.000 IDR","ISPA","ISPA",
                            stBenefitDocNo.get().toString().trim())
                        benefitDtlDao.insertBenfitDtl(benefitDtlEntity)

                        benefitDtlEntity = BenefitDtlEntity(2, "2020-04-15",
                            "MEDICAL","RAWAT JALAN",
                            "BUDI","100.000 IDR",
                            "100.000 IDR","ISPA","ISPA",
                            stBenefitDocNo.get().toString().trim())
                        benefitDtlDao.insertBenfitDtl(benefitDtlEntity)
                    }

                    val mutableListDbBenfDtl = benefitDtlDao.getBenefitByDoc(stBenefitDocNo.get().toString().trim())
                    if(mutableListDbBenfDtl.isNotEmpty()){
                        for(i in mutableListDbBenfDtl.indices){
                            benefitDtlModel = BenefitDtlModel(""+(i+1).toString(),mutableListDbBenfDtl[i].tBenefitDtlDate,
                                mutableListDbBenfDtl[i].tBenefitType, mutableListDbBenfDtl[i].tBenefitName,
                                mutableListDbBenfDtl[i].tPersonalBenefit, mutableListDbBenfDtl[i].tAmountClaim,
                                mutableListDbBenfDtl[i].tPaidClaim, mutableListDbBenfDtl[i].tDiagnoseDisease,mutableListDbBenfDtl[i].tBenefitDescription)
                            mutableBenefitDtlList.add(benefitDtlModel)
                        }
                    }
                    uiThread {
                        if (mutableBenefitDtlList.size > 0){
                            isVisibleBenefitDtlProgress.set(true)
                            benefitListDtlInterface.onLoadBenefitDtlList(mutableBenefitDtlList)
                        }
                    }
                }
            }
        }
    }

    fun onClickFabClickDtl(){
        val intent = Intent(context, BenefitTransActivity::class.java)
        intent.putExtra(BenefitTransActivity.extraBenefitTransDate, "")
        intent.putExtra(BenefitTransActivity.extraBenefitTransType, "")
        intent.putExtra(BenefitTransActivity.extraBenefitTransName, "")
        intent.putExtra(BenefitTransActivity.extraBenefitTransPerson, "")
        intent.putExtra(BenefitTransActivity.extraBenefitTransDiagnose, "")
        intent.putExtra(BenefitTransActivity.extraTransAmount, "")
        intent.putExtra(BenefitTransActivity.extraTransPaidAmount, "")
        intent.putExtra(BenefitTransActivity.extraTransDescription, "")
        intent.putExtra(ConstantObject.extra_intent, stBenefitFrom.get().toString().trim())
        intent.putExtra(BenefitTransActivity.extraTransType, ConstantObject.vNew)
        context.startActivity(intent)
    }

    fun onClickApproveBenefit(){
        when{
                !ConnectionObject.isNetworkAvailable(context) ->
                    benefitListDtlInterface.onAlertBenefitList(context.getString(R.string.alert_no_connection),
                        ConstantObject.vAlertDialogNoConnection, BenefitDtlListActivity.ALERT_BENEFITDTL_LIST_NO_CONNECTION)
                mutableBenefitDtlList.size == 0 -> benefitListDtlInterface.onBenefitDtlMessage("Please Add Benefit Fitst", ConstantObject.vSnackBarWithButton)
                else ->benefitListDtlInterface.onAlertBenefitList(context.getString(R.string.transaction_alert_confirmation),
                    ConstantObject.vAlertDialogConfirmation, BenefitDtlListActivity.ALERT_BENEFITDTL_LIST_APPROVED)
            }
    }

    fun onClickRejectBenefit(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> benefitListDtlInterface.onAlertBenefitList(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, BenefitDtlListActivity.ALERT_BENEFITDTL_LIST_NO_CONNECTION)
            mutableBenefitDtlList.size == 0 -> benefitListDtlInterface.onBenefitDtlMessage("Please Add Benefit Fitst", ConstantObject.vSnackBarWithButton)
            else ->benefitListDtlInterface.onAlertBenefitList(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, BenefitDtlListActivity.ALERT_BENEFITDTL_LIST_REJECT)
        }
    }

    fun onProcessBenefit(keyDialog : Int){
        isVisibleBenefitDtlProgress.set(true)
        when(keyDialog){
            BenefitDtlListActivity.ALERT_BENEFITDTL_LIST_APPROVED ->{
                Handler().postDelayed({
                    benefitListDtlInterface.onSuccessBenefit("Transaction success approved")
                },2000)
            }
            else -> {
                Handler().postDelayed({
                    benefitListDtlInterface.onSuccessBenefit("Transaction success reject")
                },2000)
            }
        }
    }

    fun onPublishBenefit(){
        when{
            mutableBenefitDtlList.size == 0 -> benefitListDtlInterface.onBenefitDtlMessage("Please add transaction first" , ConstantObject.vSnackBarWithButton)
            !ConnectionObject.isNetworkAvailable(context) ->
                benefitListDtlInterface.onAlertBenefitList(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, BenefitDtlListActivity.ALERT_BENEFITDTL_LIST_NO_CONNECTION)
            else ->{
                isVisibleBenefitDtlProgress.set(true)
                Handler().postDelayed({
                    benefitListDtlInterface.onSuccessBenefit(context.getString(R.string.alert_transaction_success))
                },2000)
            }
        }
    }
}