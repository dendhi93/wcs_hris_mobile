package com.wcs.mobilehris.feature.approvallistofactivities

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.feature.requesttravellist.RequestTravelListActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.Preference
import org.json.JSONArray
import org.json.JSONObject

class AppActionListViewModel(private val context: Context, private val appActionListInterface : AppActionListInterface, private val apiRepo: ApiRepo) : ViewModel() {
    private var preference: Preference = Preference(context)

    fun initDataActivities(typeOfLoading : Int){
        when{
            !ConnectionObject.isNetworkAvailable(context) ->
                appActionListInterface.onAlertAppActivitiesList(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection,
                    RequestTravelListActivity.ALERT_REQ_TRAVEL_HIST_NO_CONNECTION)
            else -> getAppActivities(typeOfLoading)
        }
    }

    private fun getAppActivities(typeOfLoading : Int) {
        when(typeOfLoading){ConstantObject.vLoadWithProgressBar -> appActionListInterface.showUI(ConstantObject.vProgresBarUI) }
        appActionListInterface.hideUI(ConstantObject.vRecylerViewUI)
        appActionListInterface.showUI(ConstantObject.vGlobalUI)

        val appActivitiesList = mutableListOf<AppActionListModel>()
        var appActionListModel : AppActionListModel

        apiRepo.getActivityApprovalAll(preference.getUn(), context, object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseActivityApprovalList = it.getString(ConstantObject.vResponseData)
                    val arrayActivityList = JSONArray(responseActivityApprovalList)

                    for(i in 0 until arrayActivityList.length()) {
                        val jObjActivityApproval = arrayActivityList.getJSONObject(i)

                        appActionListModel = AppActionListModel(
                            jObjActivityApproval.getString("ID"),
                            jObjActivityApproval.getString("DOCUMENT_NO"),
                            jObjActivityApproval.getString("NIK"),
                            jObjActivityApproval.getString("CHARGE_CD"),
                            jObjActivityApproval.getString("DESCRIPTION")
                        )
                        appActivitiesList.add(appActionListModel)
                    }

                    showList(appActivitiesList, typeOfLoading)
                }
            }

            override fun onDataError(error: String?) {
                appActionListInterface.showUI(ConstantObject.vGlobalUI)
                appActionListInterface.hideUI(ConstantObject.vRecylerViewUI)
                when(typeOfLoading){
                    ConstantObject.vLoadWithProgressBar -> appActionListInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> appActionListInterface.onHideSwipeActivitiesList()
                }
                appActionListInterface.onErrorMessage("Error Activity Approval List" + error.toString().trim(), ConstantObject.vToastError)
            }

        })
    }

    private fun showList(list : List<AppActionListModel>, typeOfLoading: Int) {
        when(list.size){
            0 -> {
                appActionListInterface.showUI(ConstantObject.vGlobalUI)
                appActionListInterface.hideUI(ConstantObject.vRecylerViewUI)
                when(typeOfLoading){
                    ConstantObject.vLoadWithProgressBar -> appActionListInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> appActionListInterface.onHideSwipeActivitiesList()
                }
                appActionListInterface.onErrorMessage(context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
            }
            else -> appActionListInterface.onLoadApprovalListOfActivities(list, typeOfLoading)
        }
    }

    fun onBackLeaveListMenu(){
        val intent = Intent(context, MenuActivity::class.java)
        intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_APPROVAL)

        context.startActivity(intent)
    }

}
