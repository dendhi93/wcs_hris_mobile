package com.wcs.mobilehris.feature.leave.list

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.leave.transaction.LeaveTransactionActivity
import com.wcs.mobilehris.util.ConstantObject

class LeaveListViewModel (private val context: Context, private val leaveListInterface: LeaveListInterface):ViewModel(){
    val isVisibleFabLeave = ObservableField<Boolean>(false)

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
        var leaveListModel : LeaveListModel?
        when(intentLeaveFrom){
            ConstantObject.extra_fromIntentRequest -> {
                isVisibleFabLeave.set(true)
                leaveListModel = LeaveListModel("1",
                    "Annual Leave",
                    "25/02/2020",
                    "25/02/2020",
                    "Waiting",
                    "")
                mutableLeaveList.add(leaveListModel)
                leaveListModel = LeaveListModel("2",
                    "Annual Leave",
                    "26/02/2020",
                    "26/02/2020",
                    "True",
                    "")
                mutableLeaveList.add(leaveListModel)
                leaveListModel = LeaveListModel("3",
                    "Sick Leave",
                    "26/02/2020",
                    "26/02/2020",
                    "False",
                    "")
                mutableLeaveList.add(leaveListModel)
            }
            else -> {
                isVisibleFabLeave.set(false)
                leaveListModel = LeaveListModel("1",
                    "Annual Leave",
                    "25/02/2020",
                    "25/02/2020",
                    "",
                    "Deddy")
                mutableLeaveList.add(leaveListModel)
                leaveListModel = LeaveListModel("2",
                    "Annual Leave",
                    "26/02/2020",
                    "26/02/2020",
                    "",
                    "Windy")
                mutableLeaveList.add(leaveListModel)
                leaveListModel = LeaveListModel("3",
                    "Sick Leave",
                    "26/02/2020",
                    "26/02/2020",
                    "",
                    "Michael")
                mutableLeaveList.add(leaveListModel)
            }
        }

        when(mutableLeaveList.size){
            0 -> {
                leaveListInterface.showUI(ConstantObject.vGlobalUI)
                leaveListInterface.hideUI(ConstantObject.vRecylerViewUI)
                when(typeOfLoading){
                    ConstantObject.vLoadWithProgressBar -> leaveListInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> leaveListInterface.onHideSwipeLeaveList()
                }
                leaveListInterface.onErrorMessage(context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
            }
            else ->{
                Handler().postDelayed({
                    leaveListInterface.onLoadLeaveList(mutableLeaveList, typeOfLoading)
                }, 2000)
            }
        }
    }

    fun onClickFab(){
        val intent = Intent(context, LeaveTransactionActivity::class.java)
        intent.putExtra(ConstantObject.extra_intent, ConstantObject.extra_fromIntentRequest)
        intent.putExtra(LeaveTransactionActivity.extralLeaveId, "")
        intent.putExtra(LeaveTransactionActivity.extraLeaveRequestor, "")
        context.startActivity(intent)
    }

}