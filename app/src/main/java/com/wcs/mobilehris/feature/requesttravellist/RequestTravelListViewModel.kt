package com.wcs.mobilehris.feature.requesttravellist

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject

class RequestTravelListViewModel (private val context : Context, private val requestTravalListInterface: ReqTravelListInterface) : ViewModel(){

    fun initDataTravel(typeOfLoading : Int){
        when{
            !ConnectionObject.isNetworkAvailable(context) ->
                requestTravalListInterface.onAlertReqTravelList(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, RequestTravelListActivity.ALERT_REQ_TRAVEL_HIST_NO_CONNECTION)

            else -> getTravelData(typeOfLoading)
        }
    }

    private fun getTravelData(typeLoading : Int){
        when(typeLoading){ConstantObject.loadWithProgressBar -> requestTravalListInterface.showUI(ConstantObject.vProgresBarUI) }
        requestTravalListInterface.hideUI(ConstantObject.vRecylerViewUI)
        requestTravalListInterface.showUI(ConstantObject.vGlobalUI)

        val listTravelList = mutableListOf<TravelListModel>()
        var travelModel = TravelListModel("01","Jakarta",
            "Malaysia","27/01/2020","31/01/2020","Waiting")
        listTravelList.add(travelModel)
        travelModel = TravelListModel("02","Jakarta",
            "Solo","03/02/2020","14/02/2020","True")
        listTravelList.add(travelModel)
        travelModel = TravelListModel("02","Solo",
            "Medan","17/02/2020","28/02/2020","False")
        listTravelList.add(travelModel)

        when{
            listTravelList.size > 0 -> {
                Handler().postDelayed({
                    requestTravalListInterface.onLoadTravelList(listTravelList, typeLoading)
                }, 2000)
            }
            else -> {
                requestTravalListInterface.showUI(ConstantObject.vGlobalUI)
                requestTravalListInterface.hideUI(ConstantObject.vRecylerViewUI)
                when(typeLoading){
                    ConstantObject.loadWithProgressBar -> requestTravalListInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> requestTravalListInterface.onHideSwipeTravelList()
                }
                requestTravalListInterface.onErrorMessage(context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
            }
        }
    }

    fun fabClickRequest(){ requestTravalListInterface.intentToRequest() }
}