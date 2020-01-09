package com.wcs.mobilehris.feature.plan

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.createtask.CreateTaskActivity
import com.wcs.mobilehris.feature.dtltask.DetailTaskActivity
import com.wcs.mobilehris.util.ConstantObject

class PlanViewModel (var _context : Context, var _planInterface : PlanInterface ) : ViewModel(){

    fun initPlan(typeOfLoading : Int){
        when{
            !ConnectionObject.isNetworkAvailable(_context) -> {
                _planInterface.onAlertPlan(_context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, PlanFragment.ALERT_PLAN_NO_CONNECTION)
            }
            else -> getPlanData(typeOfLoading)
        }
    }

    private fun getPlanData(typeLoading : Int){
        when(typeLoading){
            ConstantObject.loadWithProgressBar -> _planInterface.showUI(ConstantObject.vProgresBarUI)
        }

        _planInterface.hideUI(ConstantObject.vRecylerViewUI)
        _planInterface.showUI(ConstantObject.vGlobalUI)
        val listPlan = mutableListOf<ContentTaskModel>()
        var _planModel = ContentTaskModel("Prospect",
            "Michael",
            "18/12/2019 11.24",
            "Cibitung",
            "PT Sukanda",
            "08:00",
            "11:00",
            "Plan",
            "20/12/2019",
            "40")
        listPlan.add(_planModel)
        _planModel = ContentTaskModel("PreSales",
            "Windy",
            "19/12/2019 11.24",
            "Jakarta Selatan",
            "PT Heinz ABC",
            "13:00",
            "17:00",
            "Plan",
            "20/12/2019",
            "41")
        listPlan.add(_planModel)

        when{
            listPlan.size > 0 -> {
                Handler().postDelayed({
                    _planInterface.onLoadList(listPlan, typeLoading)
                }, 2000)
            }
            else -> {
                _planInterface.showUI(ConstantObject.vGlobalUI)
                _planInterface.hideUI(ConstantObject.vRecylerViewUI)

                when(typeLoading){
                    ConstantObject.loadWithProgressBar -> _planInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> _planInterface.onHideSwipeRefresh()
                }
                _planInterface.onErrorMessage(_context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
            }
        }
    }

    fun fabPlanClick(){
        _context.startActivity(Intent(_context, CreateTaskActivity::class.java))
    }
}