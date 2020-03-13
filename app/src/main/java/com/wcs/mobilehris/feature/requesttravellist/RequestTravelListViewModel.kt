package com.wcs.mobilehris.feature.requesttravellist

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.util.ConstantObject

class RequestTravelListViewModel (private val context : Context, private val requestTravalListInterface: ReqTravelListInterface) : ViewModel(){
    val isVisibleFab = ObservableField(false)
    private var intentFrom : String = ""

    fun initDataTravel(typeOfLoading : Int, intentTravelFrom : String){
        intentFrom = intentTravelFrom
        when{
            !ConnectionObject.isNetworkAvailable(context) ->
                requestTravalListInterface.onAlertReqTravelList(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, RequestTravelListActivity.ALERT_REQ_TRAVEL_HIST_NO_CONNECTION)
            else -> getTravelData(typeOfLoading)
        }
    }

    private fun getTravelData(typeLoading : Int){
        when(typeLoading){ConstantObject.vLoadWithProgressBar -> requestTravalListInterface.showUI(ConstantObject.vProgresBarUI) }
        requestTravalListInterface.hideUI(ConstantObject.vRecylerViewUI)
        requestTravalListInterface.showUI(ConstantObject.vGlobalUI)

        val listTravelList = mutableListOf<TravelListModel>()
        when(intentFrom){
            ConstantObject.extra_fromIntentApproval -> {
                isVisibleFab.set(false)
                var travelModel = TravelListModel("01","Training/Seminar/WorkShop",
                    "27/01/2020","31/01/2020","Training React Native",
                    "Non Travel Business", "","Andika")
                listTravelList.add(travelModel)
                travelModel = TravelListModel("01","Training/Seminar/WorkShop",
                    "03/02/2020","07/02/2020","Training .net",
                    "Non Travel Business", "", "Michael")
                listTravelList.add(travelModel)
            }
            else -> {
                isVisibleFab.set(true)
                var travelModel = TravelListModel("01","Training/Seminar/WorkShop",
                    "27/01/2020","31/01/2020","Training React Native",
                    "Non Travel Business", "Waiting","")
                listTravelList.add(travelModel)
                travelModel = TravelListModel("01","Routine Duty",
                    "10/02/2020","14/02/2020","Support Sari Roti",
                    "Travel Business", "True","")
                listTravelList.add(travelModel)
                travelModel = TravelListModel("01","Others",
                    "03/02/2020","07/02/2020","Presales",
                    "Non Travel Business", "False","")
                listTravelList.add(travelModel)
            }
        }
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
                    ConstantObject.vLoadWithProgressBar -> requestTravalListInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> requestTravalListInterface.onHideSwipeTravelList()
                }
                requestTravalListInterface.onErrorMessage(context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
            }
        }
    }

    fun fabClickRequest(){ requestTravalListInterface.intentToRequest() }
    fun onBackTravelList(){
        val intent = Intent(context, MenuActivity::class.java)
        when(intentFrom){
            ConstantObject.extra_fromIntentApproval -> intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_APPROVAL)
            else -> intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_REQUEST)
        }
        context.startActivity(intent)
    }
}