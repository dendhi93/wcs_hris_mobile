package com.wcs.mobilehris.feature.actual

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.createtask.CreateTaskActivity
import com.wcs.mobilehris.feature.plan.ContentTaskModel
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
import com.wcs.mobilehris.util.Preference
import org.json.JSONArray
import org.json.JSONObject

class ActualViewModel(private var _context : Context, private var _actualnterface : ActualInterface, private var apiRepo: ApiRepo ) : ViewModel(){
    private var preference: Preference = Preference(_context)

    fun initActual(typeOfLoading : Int){
        when{
            !ConnectionObject.isNetworkAvailable(_context) -> {
                _actualnterface.onAlertActual(_context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, ActualFragment.ALERT_ACTUAL_NO_CONNECTION)
            }
            else -> getActualData(typeOfLoading)
        }
    }

    private fun getActualData(typeLoading : Int){
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> _actualnterface.showUI(ConstantObject.vProgresBarUI)
        }

        _actualnterface.hideUI(ConstantObject.vRecylerViewUI)
        _actualnterface.showUI(ConstantObject.vGlobalUI)
        val listActual = mutableListOf<ContentTaskModel>()
        apiRepo.getDataActivity(preference.getUn(),
            DateTimeUtils.getCurrentDate(),
            ConstantObject.vConfirmTask,
            _context, object  : ApiRepo.ApiCallback<JSONObject> {
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
                                "Confirm",
                                jObjDataPlan.getString("DT_FROM") +" - "
                                        + jObjDataPlan.getString("DT_TO"),
                                jObjDataPlan.getString("ACTIVITY_HEADER_ID"),
                                0,
                                true,
                                ""
                            )
                            listActual.add(planModel)
                        }
                        when{
                            listActual.size > 0 -> _actualnterface.onDisplayList(listActual, typeLoading)
                            else -> {
                                _actualnterface.showUI(ConstantObject.vGlobalUI)
                                _actualnterface.hideUI(ConstantObject.vRecylerViewUI)

                                when(typeLoading){
                                    ConstantObject.vLoadWithProgressBar -> _actualnterface.hideUI(ConstantObject.vProgresBarUI)
                                    else -> _actualnterface.onHideSwipeRefresh()
                                }
                                _actualnterface.onErrorMessage(_context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
                            }
                        }
                    }
                }

                override fun onDataError(error: String?) {
                    _actualnterface.hideUI(ConstantObject.vRecylerViewUI)
                    _actualnterface.showUI(ConstantObject.vGlobalUI)
                    _actualnterface.onErrorMessage(" err "
                            +error.toString().trim(), ConstantObject.vToastError)
                    when(typeLoading){
                        ConstantObject.vLoadWithProgressBar -> _actualnterface.hideUI(ConstantObject.vProgresBarUI)
                        else -> _actualnterface.onHideSwipeRefresh()
                    }
                }
            })
    }

    fun fabActualClick(){
        _context.startActivity(Intent(_context, CreateTaskActivity::class.java))
    }
}