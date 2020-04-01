package com.wcs.mobilehris.feature.plan

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ApiRepo.ApiCallback
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.createtask.CreateTaskActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
import com.wcs.mobilehris.util.Preference
import org.json.JSONArray
import org.json.JSONObject

class PlanViewModel (private var _context : Context, private var _planInterface : PlanInterface, private var apiRepo: ApiRepo) : ViewModel(){
    private var preference: Preference = Preference(_context)

    fun initPlan(typeOfLoading : Int, selectedTenorDate : Int){
        when{
            !ConnectionObject.isNetworkAvailable(_context) ->
                _planInterface.onAlertPlan(_context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, PlanFragment.ALERT_PLAN_NO_CONNECTION)

            else -> getPlanData(typeOfLoading, selectedTenorDate)
        }
    }

    private fun getPlanData(typeLoading : Int, intTenorDate : Int ){
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> _planInterface.showUI(ConstantObject.vProgresBarUI)
        }
        _planInterface.hideUI(ConstantObject.vRecylerViewUI)
        _planInterface.showUI(ConstantObject.vGlobalUI)
        val listPlan = mutableListOf<ContentTaskModel>()

        apiRepo.getDataActivity(preference.getUn(),
            DateTimeUtils.getAdvancedDate(intTenorDate).toString(),
            ConstantObject.vPlanTask,
            _context, object  : ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        val responsePlanData = it.getString(ConstantObject.vResponseData)
                        val jArrayDataPlan = JSONArray(responsePlanData)
                        for (i in 0 until jArrayDataPlan.length()) {
                            val jObjDataPlan = jArrayDataPlan.getJSONObject(i)
                            val planModel = ContentTaskModel(
                                jObjDataPlan.getString("CHARGE_CD") +"|"
                                        +jObjDataPlan.getString("CHARGE_CD_NAME"),
                                jObjDataPlan.getString("CREATED_BY_NAME"),
                                jObjDataPlan.getString("CREATED_DATE"),
                                jObjDataPlan.getString("CUSTOMER_NAME"),
                                jObjDataPlan.getString("TIME_FROM"),
                                jObjDataPlan.getString("TIME_TO"),
                                "Plan",
                                jObjDataPlan.getString("DT_FROM") +" - "
                                        + jObjDataPlan.getString("DT_TO"),
                                jObjDataPlan.getString("ACTIVITY_HEADER_ID"),
                                0,
                                true,
                                ""
                            )
                            listPlan.add(planModel)
                        }
                        when{
                            listPlan.size > 0 -> _planInterface.onLoadList(listPlan, typeLoading)
                            else -> {
                                _planInterface.showUI(ConstantObject.vGlobalUI)
                                _planInterface.hideUI(ConstantObject.vRecylerViewUI)

                                when(typeLoading){
                                    ConstantObject.vLoadWithProgressBar -> _planInterface.hideUI(ConstantObject.vProgresBarUI)
                                    else -> _planInterface.onHideSwipeRefresh()
                                }
                                _planInterface.onErrorMessage(_context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
                            }
                        }
                    }
                }

                override fun onDataError(error: String?) {
                    _planInterface.hideUI(ConstantObject.vRecylerViewUI)
                    _planInterface.showUI(ConstantObject.vGlobalUI)
                    _planInterface.onErrorMessage(" err "
                            +error.toString().trim(), ConstantObject.vToastError)
                    when(typeLoading){
                        ConstantObject.vLoadWithProgressBar -> _planInterface.hideUI(ConstantObject.vProgresBarUI)
                        else -> _planInterface.onHideSwipeRefresh()
                    }
                }
            })
    }

    fun fabPlanClick(){ _context.startActivity(Intent(_context, CreateTaskActivity::class.java)) }
}