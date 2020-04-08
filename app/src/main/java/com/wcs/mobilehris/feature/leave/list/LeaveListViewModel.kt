package com.wcs.mobilehris.feature.leave.list

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.leave.transaction.LeaveTransactionActivity
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
import com.wcs.mobilehris.util.Preference
import org.json.JSONArray
import org.json.JSONObject

class LeaveListViewModel (private val context: Context,
                          private val leaveListInterface: LeaveListInterface,
                        private val apiRepo: ApiRepo):ViewModel(){
    val isVisibleFabLeave = ObservableField(false)
    private var preference: Preference = Preference(context)

    fun validateDataLeave(typeOfLoading : Int, intentLeaveFrom : String){
        when{
            !ConnectionObject.isNetworkAvailable(context) ->
                leaveListInterface.onAlertLeaveList(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, LeaveListActivity.ALERT_LEAVE_LIST_NO_CONNECTION)
            else -> getLeaveData(typeOfLoading, intentLeaveFrom)
        }
    }

    private fun getLeaveData(typeOfLoading : Int, intentLeaveFrom : String){
        when(typeOfLoading){ConstantObject.vLoadWithProgressBar -> leaveListInterface.showUI(ConstantObject.vProgresBarUI) }
        leaveListInterface.hideUI(ConstantObject.vRecylerViewUI)
        leaveListInterface.showUI(ConstantObject.vGlobalUI)

        val mutableLeaveList = mutableListOf<LeaveListModel>()
        var leaveListModel : LeaveListModel
        apiRepo.getLeaveList(preference.getUn(),intentLeaveFrom, context, object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseLeaveList = it.getString(ConstantObject.vResponseData)
                    val jArrayLeaveList = JSONArray(responseLeaveList)
                    when(intentLeaveFrom){
                        ConstantObject.extra_fromIntentRequest -> {
                            isVisibleFabLeave.set(true)
                            for(i in 0 until jArrayLeaveList.length()) {
                                val jObjReqLeave = jArrayLeaveList.getJSONObject(i)
                                val leaveDateFrom = DateTimeUtils.getChangeDateFormat(jObjReqLeave.getString("DATE_FROM"), ConstantObject.dateTimeFormat_1)
                                val leaveDateInto = DateTimeUtils.getChangeDateFormat(jObjReqLeave.getString("DATE_TO"), ConstantObject.dateTimeFormat_1)
                                leaveListModel = LeaveListModel(
                                    jObjReqLeave.getString("ID"),
                                    jObjReqLeave.getString("LEAVE_TYPE_NAME"),
                                    leaveDateFrom.toString(),
                                    leaveDateInto.toString(),
                                    jObjReqLeave.getString("LEAVE_STATUS"),
                                    ""
                                )
                                mutableLeaveList.add(leaveListModel)
                            }
                            funcLeaveList(mutableLeaveList, typeOfLoading)
                        }
                        else -> {
                            isVisibleFabLeave.set(false)
                            for(j in 0 until jArrayLeaveList.length()) {
                                val jObjApproveLeave = jArrayLeaveList.getJSONObject(j)
                                val leaveApproveDateFrom = DateTimeUtils.getChangeDateFormat(jObjApproveLeave.getString("DATE_FROM"), ConstantObject.dateTimeFormat_2)
                                val leaveApproveDateInto = DateTimeUtils.getChangeDateFormat(jObjApproveLeave.getString("DATE_TO"), ConstantObject.dateTimeFormat_2)
                                var nameRequestor = jObjApproveLeave.getString("NIK").split("(")[1]
                                nameRequestor = nameRequestor.split(")")[0]

                                leaveListModel = LeaveListModel(
                                    jObjApproveLeave.getString("ID"),
                                    jObjApproveLeave.getString("LEAVE_TYPE_NAME"),
                                    leaveApproveDateFrom.toString(),
                                    leaveApproveDateInto.toString(),
                                    "",
                                    nameRequestor.trim()
                                )
                                mutableLeaveList.add(leaveListModel)
                            }
                            funcLeaveList(mutableLeaveList, typeOfLoading)
                        }
                    }
                }
            }

            override fun onDataError(error: String?) {
                when(intentLeaveFrom){
                    ConstantObject.extra_fromIntentRequest -> isVisibleFabLeave.set(true)
                    else -> isVisibleFabLeave.set(false)
                }
                leaveListInterface.showUI(ConstantObject.vGlobalUI)
                leaveListInterface.hideUI(ConstantObject.vRecylerViewUI)
                when(typeOfLoading){
                    ConstantObject.vLoadWithProgressBar -> leaveListInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> leaveListInterface.onHideSwipeLeaveList()
                }
                leaveListInterface.onErrorMessage("Err Leave List " +error.toString().trim(), ConstantObject.vToastError)
            }
        })
    }

    private fun funcLeaveList(list : List<LeaveListModel>, typeOfLoading : Int){
        when(list.size){
            0 -> {
                leaveListInterface.showUI(ConstantObject.vGlobalUI)
                leaveListInterface.hideUI(ConstantObject.vRecylerViewUI)
                when(typeOfLoading){
                    ConstantObject.vLoadWithProgressBar -> leaveListInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> leaveListInterface.onHideSwipeLeaveList()
                }
                leaveListInterface.onErrorMessage(context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
            }
            else -> leaveListInterface.onLoadLeaveList(list, typeOfLoading)
        }
    }

    fun onClickFab(){
        val intent = Intent(context, LeaveTransactionActivity::class.java)
        intent.putExtra(ConstantObject.extra_intent, ConstantObject.extra_fromIntentRequest)
        intent.putExtra(LeaveTransactionActivity.extralLeaveId, "")
        intent.putExtra(LeaveTransactionActivity.extraLeaveRequestor, "")
        context.startActivity(intent)
    }

    fun onBackLeaveListMenu(intentFrom : String){
        val intent = Intent(context, MenuActivity::class.java)
        when(intentFrom){
            ConstantObject.extra_fromIntentApproval -> intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_APPROVAL)
            else -> intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_REQUEST)
        }
        context.startActivity(intent)
    }

}