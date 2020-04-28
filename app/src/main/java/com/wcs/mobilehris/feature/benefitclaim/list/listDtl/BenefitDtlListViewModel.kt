package com.wcs.mobilehris.feature.benefitclaim.list.listDtl

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.benefitclaim.trans.BenefitTransActivity
import com.wcs.mobilehris.util.ConstantObject

class BenefitDtlListViewModel(private val context: Context,
                              private val benefitListDtlInterface: BenefitDtlInterface) : ViewModel(){
    val isVisibleBottomLinear = ObservableField(false)
    val isVisibleBenefitDtlProgress = ObservableField(false)
    val stBenefitDocNo = ObservableField("")
    val stBenefitFrom = ObservableField("")
    val isVisibleRecyler = ObservableField(false)

    fun validateDataBenefitDtl(transType : String, intentFrom : String){
        when{
            !ConnectionObject.isNetworkAvailable(context) ->
                benefitListDtlInterface.onAlertBenefitList(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, BenefitDtlListActivity.ALERT_BENEFITDTL_LIST_NO_CONNECTION)
            else -> getBenefDtlData(intentFrom, transType)
        }
    }

    private fun getBenefDtlData(intentFrom : String, transBenefitType : String){
        val mutableBenefitDtlList = mutableListOf<BenefitDtlModel>()
        var benefitDtlModel : BenefitDtlModel
        when(intentFrom){
            ConstantObject.extra_fromIntentRequest ->{
                isVisibleBottomLinear.set(false)
                if(transBenefitType.trim() != ConstantObject.vNew){
                    benefitDtlModel = BenefitDtlModel("1", "2020-04-15",
                        "MEDICAL","RAWAT JALAN",
                        "ANDIKA","100.000 IDR",
                        "100.000 IDR","DEMAM","ISPA")
                    mutableBenefitDtlList.add(benefitDtlModel)
                    benefitDtlModel = BenefitDtlModel("1", "2020-04-15",
                        "MEDICAL","RAWAT JALAN",
                        "KHANIF","100.000 IDR",
                        "100.000 IDR","DEMAM","ISPA")
                    mutableBenefitDtlList.add(benefitDtlModel)
                    benefitDtlModel = BenefitDtlModel("1", "2020-04-15",
                        "MEDICAL","RAWAT JALAN",
                        "MICHAEL","100.000 IDR",
                        "100.000 IDR","DEMAM","ISPA")
                    mutableBenefitDtlList.add(benefitDtlModel)
                }
            }
            else ->{
                isVisibleBottomLinear.set(true)
                benefitDtlModel = BenefitDtlModel("1", "2020-04-15",
                    "MEDICAL","RAWAT JALAN",
                    "ANDIKA","100.000 IDR",
                    "100.000 IDR","DEMAM","ISPA")
                mutableBenefitDtlList.add(benefitDtlModel)
                benefitDtlModel = BenefitDtlModel("1", "2020-04-15",
                    "MEDICAL","RAWAT JALAN",
                    "KHANIF","100.000 IDR",
                    "100.000 IDR","DEMAM","ISPA")
                mutableBenefitDtlList.add(benefitDtlModel)
                benefitDtlModel = BenefitDtlModel("1", "2020-04-15",
                    "MEDICAL","RAWAT JALAN",
                    "MICHAEL","100.000 IDR",
                    "100.000 IDR","DEMAM","ISPA")
                mutableBenefitDtlList.add(benefitDtlModel)
            }
        }
        if (mutableBenefitDtlList.size > 0){
            isVisibleBenefitDtlProgress.set(true)
            Handler().postDelayed({
                benefitListDtlInterface.onLoadBenefitDtlList(mutableBenefitDtlList)
            },2000)
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
        intent.putExtra(BenefitTransActivity.extraTransType, BenefitTransActivity.extraValueTransDtlType)
        context.startActivity(intent)
    }

    fun onClickApproveBenefit(){}

    fun onClickRejectBenefit(){}
}