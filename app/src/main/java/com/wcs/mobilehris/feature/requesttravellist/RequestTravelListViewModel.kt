package com.wcs.mobilehris.feature.requesttravellist

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.Preference
import org.json.JSONArray
import org.json.JSONObject

class RequestTravelListViewModel (private val context : Context,
                                  private val requestTravalListInterface: ReqTravelListInterface,
                                    private val apiRepo: ApiRepo) : ViewModel(){
    val isVisibleFab = ObservableField(false)
    private var preference: Preference = Preference(context)
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
        var travelListModel : TravelListModel
        apiRepo.getTravelListData(preference.getUn(), intentFrom.trim(), context, object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseTravelList = it.getString(ConstantObject.vResponseData)
                    val jArrayTravelList = JSONArray(responseTravelList)
                    when(intentFrom){
                        ConstantObject.extra_fromIntentApproval -> {
                            isVisibleFab.set(false)
                            for(j in 0 until jArrayTravelList.length()){
                                val jObjApprovalTravel = jArrayTravelList.getJSONObject(j)
                                travelListModel = TravelListModel(
                                    jObjApprovalTravel.getString("ID_TR_HEADER"),
                                    jObjApprovalTravel.getString("REASON_NAME"),
                                    jObjApprovalTravel.getString("DEPART_DATE").split("T")[0].trim(),
                                    jObjApprovalTravel.getString("RETURN_DATE").split("T")[0].trim(),
                                    jObjApprovalTravel.getString("DESCRIPTION"),
                                    jObjApprovalTravel.getString("STATUS_CD"),
                                    "",
                                    jObjApprovalTravel.getString("REQUESTOR_NAME")
                                )
                                listTravelList.add(travelListModel)
                            }
                            validateListTravel(listTravelList, typeLoading)
                        }
                        else -> {
                            isVisibleFab.set(true)
                            for(i in 0 until jArrayTravelList.length()) {
                                val jObjReqTravel = jArrayTravelList.getJSONObject(i)
                                travelListModel = TravelListModel(
                                    jObjReqTravel.getString("ID_TR_HEADER"),
                                    jObjReqTravel.getString("REASON_DESCRIPTION"),
                                    jObjReqTravel.getString("DEPART_DATE").split("T")[0].trim(),
                                    jObjReqTravel.getString("RETURN_DATE").split("T")[0].trim(),
                                    jObjReqTravel.getString("DESCRIPTION"),
                                    jObjReqTravel.getString("TRAVEL_TYPE_NAME"),
                                    jObjReqTravel.getString("STATUS_CD"),
                                    ""
                                )
                                listTravelList.add(travelListModel)
                            }
                            validateListTravel(listTravelList, typeLoading)
                        }
                    }
                }
            }

            override fun onDataError(error: String?) {
                requestTravalListInterface.showUI(ConstantObject.vGlobalUI)
                requestTravalListInterface.hideUI(ConstantObject.vRecylerViewUI)
                when(typeLoading){
                    ConstantObject.vLoadWithProgressBar -> requestTravalListInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> requestTravalListInterface.onHideSwipeTravelList()
                }
                requestTravalListInterface.onErrorMessage("err req Travel "+error.toString().trim(), ConstantObject.vToastError)
            }
        })
    }

    private fun validateListTravel(list : List<TravelListModel>, loadingType : Int){
        when{
            list.isNotEmpty() -> requestTravalListInterface.onLoadTravelList(list, loadingType)
            else -> {
                requestTravalListInterface.showUI(ConstantObject.vGlobalUI)
                requestTravalListInterface.hideUI(ConstantObject.vRecylerViewUI)
                when(loadingType){
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