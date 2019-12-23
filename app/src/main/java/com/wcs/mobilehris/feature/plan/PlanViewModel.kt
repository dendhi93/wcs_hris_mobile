package com.wcs.mobilehris.feature.plan

import android.content.Context
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
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
            PlanFragment.LOAD_WITH_PROGRESSBAR -> _planInterface.showUI(ConstantObject.vProgresBarUI)
        }

        _planInterface.hideUI(ConstantObject.vRecylerViewUI)
        _planInterface.showUI(ConstantObject.vGlobalUI)
        var listPlan = mutableListOf<ContentPlanModel>()
        var _planModel = ContentPlanModel("Prospect",
            "Michael",
            "18/12/2019 11.24",
            "Cibitung",
            "PT Sukanda",
            "08:00",
            "11:00",
            "Plan",
            "20/12/2019")
        listPlan.add(_planModel)
        _planModel = ContentPlanModel("Sales",
            "Windy",
            "19/12/2019 11.24",
            "Jakarta Selatan",
            "PT Heinz ABC",
            "13:00",
            "17:00",
            "Plan",
            "20/12/2019")
        listPlan.add(_planModel)

        Handler().postDelayed({
            _planInterface.onLoadList(listPlan, typeLoading)
        }, 2000)
    }

}