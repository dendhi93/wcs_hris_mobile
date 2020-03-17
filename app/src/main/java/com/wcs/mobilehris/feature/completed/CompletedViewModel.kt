package com.wcs.mobilehris.feature.completed

import android.content.Context
import android.content.Intent
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

class CompletedViewModel(private val _context : Context,
                         private val _completedInterface : CompletedInterface,
                        private val apiRepo: ApiRepo) : ViewModel() {
    private var preference: Preference = Preference(_context)

    fun initCompleted(typeOfLoading : Int){
        when{
            !ConnectionObject.isNetworkAvailable(_context) -> {
                _completedInterface.onAlertCompleted(_context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, CompletedFragment.ALERT_COMPLETED_NO_CONNECTION)
            }
            else -> getCompletedData(typeOfLoading)
        }
    }

    private fun getCompletedData(typeLoading : Int){
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> _completedInterface.showUI(ConstantObject.vProgresBarUI)
        }
        _completedInterface.hideUI(ConstantObject.vRecylerViewUI)
        _completedInterface.showUI(ConstantObject.vGlobalUI)
        val listCompleted = mutableListOf<ContentTaskModel>()
        apiRepo.getDataActivity(preference.getUn(),
            DateTimeUtils.getCurrentDate(),
            ConstantObject.vCompletedTask,
            _context, object  : ApiRepo.ApiCallback<JSONObject> {
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        val responseCompletedData = it.getString(ConstantObject.vResponseData)
                        val jArrayDataCompleted = JSONArray(responseCompletedData)
                        for (i in 0 until jArrayDataCompleted.length()) {
                            val jObjDataCompleted = jArrayDataCompleted.getJSONObject(i)
                            val completedModel = ContentTaskModel(
                                jObjDataCompleted.getString("CHARGE_CD") +"|"
                                        +jObjDataCompleted.getString("CHARGE_CD_NAME"),
                                jObjDataCompleted.getString("CREATED_BY_NAME"),
                                jObjDataCompleted.getString("CREATED_DATE"),
                                jObjDataCompleted.getString("CUSTOMER_NAME"),
                                jObjDataCompleted.getString("TIME_FROM"),
                                jObjDataCompleted.getString("TIME_TO"),
                                jObjDataCompleted.getString("STATUS"),
                                jObjDataCompleted.getString("DT_FROM") +" - "
                                        + jObjDataCompleted.getString("DT_TO"),
                                jObjDataCompleted.getString("ACTIVITY_HEADER_ID"),
                                0,
                                true,
                                ""
                            )
                            listCompleted.add(completedModel)
                        }
                        when{
                            listCompleted.size > 0 -> _completedInterface.onDisplayCompletedList(listCompleted, typeLoading)
                            else -> {
                                _completedInterface.showUI(ConstantObject.vGlobalUI)
                                _completedInterface.hideUI(ConstantObject.vRecylerViewUI)

                                when(typeLoading){
                                    ConstantObject.vLoadWithProgressBar -> _completedInterface.hideUI(ConstantObject.vProgresBarUI)
                                    else -> _completedInterface.onHideSwipeRefresh()
                                }
                                _completedInterface.onErrorMessage(_context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
                            }
                        }
                    }
                }

                override fun onDataError(error: String?) {
                    _completedInterface.hideUI(ConstantObject.vRecylerViewUI)
                    _completedInterface.showUI(ConstantObject.vGlobalUI)
                    _completedInterface.onErrorMessage(" err "
                            +error.toString().trim(), ConstantObject.vToastError)
                    when(typeLoading){
                        ConstantObject.vLoadWithProgressBar -> _completedInterface.hideUI(ConstantObject.vProgresBarUI)
                        else -> _completedInterface.onHideSwipeRefresh()
                    }
                }
            })
    }

    fun fabCompletedClick(){
        _context.startActivity(Intent(_context, CreateTaskActivity::class.java))
    }


}