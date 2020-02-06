package com.wcs.mobilehris.feature.completed

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.createtask.CreateTaskActivity
import com.wcs.mobilehris.feature.dtltask.DetailTaskActivity
import com.wcs.mobilehris.feature.plan.ContentTaskModel
import com.wcs.mobilehris.util.ConstantObject

class CompletedViewModel(private val _context : Context, private val _completedInterface : CompletedInterface) : ViewModel() {

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
            ConstantObject.loadWithProgressBar -> _completedInterface.showUI(ConstantObject.vProgresBarUI)
        }
        _completedInterface.hideUI(ConstantObject.vRecylerViewUI)
        _completedInterface.showUI(ConstantObject.vGlobalUI)
        val listCompleted = mutableListOf<ContentTaskModel>()
        var _completedModel = ContentTaskModel("F-0014-018|MILLS MOBILITY APPLICATION",
            "Michael",
            "18/12/2019 11.24",
            "PT Sukanda",
            "08:00",
            "11:00",
            "Completed",
            "20/12/2019",
            "60",
            8,
            true,
            "")
        listCompleted.add(_completedModel)
        _completedModel = ContentTaskModel("A-1003-096|BUSINESS DEVELOPMENT FOR MOBILITY ACTIVITY",
            "Windy",
            "20/01/2020 11:24",
            "PT KSL",
            "08:30",
            "12:00",
            "Completed",
            "20/12/2019",
            "60",
            4,
            false,
            "Task kurang dari 8 jam")
        listCompleted.add(_completedModel)

        when{
            listCompleted.size > 0 -> {
                Handler().postDelayed({
                    _completedInterface.onDisplayCompletedList(listCompleted, typeLoading)
                }, 2000)
            }
            else -> {
                _completedInterface.showUI(ConstantObject.vGlobalUI)
                _completedInterface.hideUI(ConstantObject.vRecylerViewUI)
                _completedInterface.onErrorMessage(_context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
                when(typeLoading){
                    ConstantObject.loadWithProgressBar -> _completedInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> _completedInterface.onHideSwipeRefresh()
                }
            }
        }
    }

    fun fabCompletedClick(){
        _context.startActivity(Intent(_context, CreateTaskActivity::class.java))
    }


}