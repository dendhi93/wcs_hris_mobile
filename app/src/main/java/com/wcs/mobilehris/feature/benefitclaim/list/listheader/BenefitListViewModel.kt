package com.wcs.mobilehris.feature.benefitclaim.list.listheader

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.benefitclaim.list.listDtl.BenefitDtlListActivity
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.util.ConstantObject

class BenefitListViewModel(private val context: Context,
                           private val benefitListInterface: BenefitListInterface) : ViewModel(){
    val isVisibleFabBenefit = ObservableField(false)

    fun validateDataBenefit(typeOfLoading : Int, intentBenefitFrom : String){
        when{
            !ConnectionObject.isNetworkAvailable(context) ->
                benefitListInterface.onAlertBenefitList(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, BenefitClaimListActivity.ALERT_BENEFIT_LIST_NO_CONNECTION)
            else -> getBenefitData(typeOfLoading, intentBenefitFrom)
        }
    }

    private fun getBenefitData(typeOfLoading : Int, intentBenefitFrom : String){
        var benefitModel : BenefitModel
        val mutableBenefitList = mutableListOf<BenefitModel>()
        when(typeOfLoading){ConstantObject.vLoadWithProgressBar -> benefitListInterface.showUI(ConstantObject.vProgresBarUI) }
        benefitListInterface.hideUI(ConstantObject.vRecylerViewUI)
        benefitListInterface.showUI(ConstantObject.vGlobalUI)
        when(intentBenefitFrom){
            ConstantObject.extra_fromIntentRequest -> {
                isVisibleFabBenefit.set(true)
                benefitModel =
                    BenefitModel(
                        "BEN-001",
                        "",
                        ConstantObject.vWaitingTask,
                        "2020-04-30"
                    )
                mutableBenefitList.add(benefitModel)
                benefitModel =
                    BenefitModel(
                        "BEN-002",
                        "",
                        ConstantObject.vWaitingTask,
                        "2020-05-5"
                    )
                mutableBenefitList.add(benefitModel)
                benefitModel =
                    BenefitModel(
                        "BEN-002",
                        "",
                        ConstantObject.vLeaveApprove,
                        "2020-05-10"
                    )
                mutableBenefitList.add(benefitModel)
            }
            else -> {
                isVisibleFabBenefit.set(false)
                benefitModel =
                    BenefitModel(
                        "BEN-001",
                        "Deddy",
                        "",
                        "2020-04-30"
                    )
                mutableBenefitList.add(benefitModel)
                benefitModel =
                    BenefitModel(
                        "BEN-002",
                        "Andri",
                        "",
                        "2020-05-5"
                    )
                mutableBenefitList.add(benefitModel)
                benefitModel =
                    BenefitModel(
                        "BEN-002",
                        "Khanif",
                        "",
                        "2020-05-10"
                    )
                mutableBenefitList.add(benefitModel)
            }
        }
        when(mutableBenefitList.size){
            0 -> {
                benefitListInterface.showUI(ConstantObject.vGlobalUI)
                benefitListInterface.hideUI(ConstantObject.vRecylerViewUI)
                when(typeOfLoading){
                    ConstantObject.vLoadWithProgressBar -> benefitListInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> benefitListInterface.onHideSwipeBenefitList()
                }
                benefitListInterface.onErrorMessage(context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
            }
            else ->{
                Handler().postDelayed({
                    benefitListInterface.onLoadBenefitList(mutableBenefitList, typeOfLoading)
                },2000)
            }
        }
    }

    fun onClickFabBenef(){
        val intent = Intent(context, BenefitDtlListActivity::class.java)
        intent.putExtra(ConstantObject.extra_intent, ConstantObject.extra_fromIntentRequest)
        intent.putExtra(BenefitDtlListActivity.extraBenefitDocNo, "")
        intent.putExtra(BenefitDtlListActivity.extraBenefitRequestor, "")
        intent.putExtra(BenefitDtlListActivity.extraBenefitTransType, ConstantObject.vNew)
        context.startActivity(intent)
    }

    fun onBackBenefitListMenu(intentFrom : String){
        val intent = Intent(context, MenuActivity::class.java)
        when(intentFrom){
            ConstantObject.extra_fromIntentApproval -> intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_APPROVAL)
            else -> intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_REQUEST)
        }
        context.startActivity(intent)
    }
}