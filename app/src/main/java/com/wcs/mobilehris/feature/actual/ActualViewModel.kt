package com.wcs.mobilehris.feature.actual

import android.content.Context
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
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
            ActualFragment.LOAD_WITH_PROGRESSBAR -> _actualnterface.showUI(ConstantObject.vProgresBarUI)
        }

        _actualnterface.hideUI(ConstantObject.vRecylerViewUI)
        _actualnterface.showUI(ConstantObject.vGlobalUI)
        var listActual = mutableListOf<ContentTaskModel>()
        var _actualModel = ContentTaskModel("Prospect",
            "Michael",
            "18/12/2019 11.24",
            "Cibitung",
            "PT Sukanda",
            "08:00",
            "11:00",
            "Confirm",
            "20/12/2019")
        listActual.add(_actualModel)
        _actualModel = ContentTaskModel("PreSales",
            "Windy",
            "19/12/2019 11.24",
            "Jakarta Selatan",
            "PT Heinz ABC",
            "13:00",
            "17:00",
            "Confirm",
            "20/12/2019")
        listActual.add(_actualModel)
        _actualModel = ContentTaskModel("Project",
            "Deddy",
            "20/12/2019 11.24",
            "Jakarta Selatan",
            "PT Yakult",
            "08:00",
            "17:30",
            "Confirm",
            "20/12/2019")
        listActual.add(_actualModel)

        Handler().postDelayed({
            _actualnterface.onDisplayList(listActual, typeLoading)
        }, 2000)
    }

    fun fabActualClick(){
        _actualnterface.onErrorMessage("Test", ConstantObject.vToastInfo)
    }
}