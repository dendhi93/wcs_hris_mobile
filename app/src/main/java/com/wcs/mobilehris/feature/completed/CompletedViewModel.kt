package com.wcs.mobilehris.feature.completed

import android.content.Context
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
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
            CompletedFragment.LOAD_WITH_PROGRESSBAR -> _completedInterface.showUI(ConstantObject.vProgresBarUI)
        }
        _completedInterface.hideUI(ConstantObject.vRecylerViewUI)
        _completedInterface.showUI(ConstantObject.vGlobalUI)
        var listCompleted = mutableListOf<ContentTaskModel>()
        var _completedModel = ContentTaskModel("Prospect",
            "Michael",
            "18/12/2019 11.24",
            "Cibitung",
            "PT Sukanda",
            "08:00",
            "11:00",
            "Completed",
            "20/12/2019")
        listCompleted.add(_completedModel)

        Handler().postDelayed({
            _completedInterface.onDisplayCompletedList(listCompleted, typeLoading)
        }, 2000)
    }



}