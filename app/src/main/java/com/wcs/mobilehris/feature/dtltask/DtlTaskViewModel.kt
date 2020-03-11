package com.wcs.mobilehris.feature.dtltask

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject

class DtlTaskViewModel(private val context : Context, private val dtlTaskInterface : DtlTaskInterface) :ViewModel(){
    val stChargeCode = ObservableField("")
    val stCompanyName = ObservableField("")
    val stDtlTaskDateFrom = ObservableField("")
    val stDtlTaskDateInto = ObservableField("")
    val stDtlTaskTimeFrom = ObservableField("")
    val stDtlTaskTimeInto = ObservableField("")
    val stDtlContactPerson = ObservableField("")
    val stDtlDescription = ObservableField("")
    val stDtlSolmanNo = ObservableField("")
    val stDtlProjectManager = ObservableField("")
    val isProgressDtl = ObservableField(false)
    val isHiddenRv = ObservableField(false)
    val isHiddenSolmanTv = ObservableField(false)
    val isHiddenProjectManager = ObservableField(false)
    private var stIntentTaskId : String = ""
    private var stIntentTypeTask : String = ""

    fun initDataDtl(taskId : String, chargeCode : String){
        stIntentTaskId = taskId.trim()
        val dtlTaskSplitChargeCode = chargeCode.trim().split("|")
        when(dtlTaskSplitChargeCode[0].trim().substring(0, 1)){
            "F" -> stIntentTypeTask = ConstantObject.vProjectTask
            "E" -> stIntentTypeTask = ConstantObject.vSupportTask
            else -> ConstantObject.vPreSalesTask
        }
        stChargeCode.set(dtlTaskSplitChargeCode[0].trim() +" " +dtlTaskSplitChargeCode[1].trim())
        when {
            !ConnectionObject.isNetworkAvailable(context) -> dtlTaskInterface.onAlertDtlTask(context.getString(R.string.alert_no_connection), ConstantObject.vAlertDialogNoConnection,
                    DetailTaskActivity.ALERT_DTL_TASK_NO_CONNECTION)
            else -> AsyncDtlTask().execute()
        }
    }

    private fun initDtlTask(){
        isProgressDtl.set(true)
        isHiddenRv.set(true)
        Handler().postDelayed({

            stCompanyName.set("PT Sukanda")
            stDtlTaskDateFrom.set("15/01/2020")
            stDtlTaskDateInto.set("16/01/2020")
            stDtlTaskTimeFrom.set("08:00")
            stDtlTaskTimeInto.set("17:00")
            stDtlContactPerson.set("Denny Rambakila")
            stDtlDescription.set("Test")
            when(stIntentTypeTask.trim()) {
                ConstantObject.vSupportTask -> {
                    isHiddenProjectManager.set(true)
                    stDtlSolmanNo.set("845894900")
                }
                ConstantObject.vProjectTask -> {
                    isHiddenSolmanTv.set(true)
                    stDtlProjectManager.set("Pak Rojak")
                }
                else->{
                    isHiddenSolmanTv.set(true)
                    isHiddenProjectManager.set(true)
                }
            }
        }, 2000)
    }

    private fun initListTeam(){
        val listFriend = mutableListOf<FriendModel>()
        var friendModel = FriendModel("62664930","Windy", "Free", false)
        listFriend.add(friendModel)
        friendModel = FriendModel("62405890","Michael Saputra", "Conflict With Heinz ABC", true)
        listFriend.add(friendModel)

        Handler().postDelayed({
            when{
                listFriend.isNotEmpty() -> dtlTaskInterface.loadTeam(listFriend)
            }
        },1000)
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncDtlTask: AsyncTask<Void, Void, String>(){

        override fun doInBackground(vararg params: Void?): String {
            return "OK"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            initDtlTask()
            initListTeam()
        }
    }
}