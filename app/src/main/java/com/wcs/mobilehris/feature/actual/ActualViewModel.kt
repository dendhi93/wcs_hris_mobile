package com.wcs.mobilehris.feature.actual

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

class ActualViewModel(var _context : Context, var _actualnterface : ActualInterface ) : ViewModel(){

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
            ConstantObject.loadWithProgressBar -> _actualnterface.showUI(ConstantObject.vProgresBarUI)
        }

        _actualnterface.hideUI(ConstantObject.vRecylerViewUI)
        _actualnterface.showUI(ConstantObject.vGlobalUI)
        val listActual = mutableListOf<ContentTaskModel>()
        var _actualModel = ContentTaskModel("F-0014-018|MILLS MOBILITY APPLICATION",
            "Michael",
            "18/12/2019 11.24",
            "PT Sukanda",
            "08:00",
            "11:00",
            "Confirm",
            "20/12/2019",
            "50",
            0,
            true,
            "")
        listActual.add(_actualModel)
        _actualModel = ContentTaskModel("A-1003-096|BUSINESS DEVELOPMENT FOR MOBILITY ACTIVITY",
            "Windy",
            "19/12/2019 11.24",
            "PT Heinz ABC",
            "13:00",
            "17:00",
            "Confirm",
            "20/12/2019",
            "51",
            0,
            true,
            "")
        listActual.add(_actualModel)
        _actualModel = ContentTaskModel("A-1003-096|BUSINESS DEVELOPMENT FOR MOBILITY ACTIVITY",
            "Deddy",
            "20/12/2019 11.24",
            "PT Yakult",
            "08:00",
            "17:30",
            "Confirm",
            "20/12/2019",
            "52",
            0,
            true,
            "")
        listActual.add(_actualModel)

        when{
            listActual.size > 0 -> {
                Handler().postDelayed({
                    _actualnterface.onDisplayList(listActual, typeLoading)
                }, 2000)
            }
            else -> {
                _actualnterface.showUI(ConstantObject.vGlobalUI)
                _actualnterface.hideUI(ConstantObject.vRecylerViewUI)
                _actualnterface.onErrorMessage(_context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
                when(typeLoading){
                    ConstantObject.loadWithProgressBar -> _actualnterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> _actualnterface.onHideSwipeRefresh()
                }
            }
        }
    }

    fun fabActualClick(){
        _context.startActivity(Intent(_context, CreateTaskActivity::class.java))
    }
}