package com.wcs.mobilehris.feature.dashboard

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.Preference
import org.json.JSONArray
import org.json.JSONObject

class DashboardViewModel(private val _context : Context,
                         private val _dashboardInterface : DashboardInterface,
                         private val apiRepo: ApiRepo) : ViewModel(){
    val stLeaveQty = ObservableField("")
    val stLateQty = ObservableField("")
    private val preference = Preference(_context)

    fun initDataDashboard(){
        when{
            !ConnectionObject.isNetworkAvailable(_context) ->{
                _dashboardInterface.onAlertDash(_context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection,
                    DashboardFragment.ALERT_DASH_NO_CONNECTION)
            }
            else -> getDashboardList(ConstantObject.vLoadWithProgressBar)
        }
    }
    fun onInitDashboardMenu(){
        when{
            !ConnectionObject.isNetworkAvailable(_context) ->{
                _dashboardInterface.onAlertDash(_context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection,
                    DashboardFragment.ALERT_DASH_NO_CONNECTION)
            }
            else ->getDashboardList(ConstantObject.vLoadWithoutProgressBar)
        }
    }

    private fun getDashboardList(typeLoading : Int){
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> {
                _dashboardInterface.showUI(ConstantObject.vProgresBarUI)
            }
        }

        _dashboardInterface.showUI(DashboardFragment.TEXTVIEW_UI)
        _dashboardInterface.hideUI(ConstantObject.vRecylerViewUI)
        _dashboardInterface.hideUI(ConstantObject.vGlobalUI)
        val listDashboard = mutableListOf<DashboardModel>()
        var dashboardModel : DashboardModel
        apiRepo.getDataDashboard(preference.getUn().trim(), _context, object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseDashboardData = it.getString(ConstantObject.vResponseData)
                    val jObjDashHeader = JSONObject(responseDashboardData)
                    stLeaveQty.set("Leave " +jObjDashHeader.getString("LEAVE_QTY"))
                    stLateQty.set("Lateness " +jObjDashHeader.getString("LATENESS_QTY"))
                    val jArrayMenu = JSONArray(jObjDashHeader.getString("MENU"))
                    for(i in 0 until jArrayMenu.length()){
                        val jObjDashboardList = jArrayMenu.getJSONObject(i)
                        dashboardModel = DashboardModel(jObjDashboardList.getString("MENU_NAME"), jObjDashboardList.getString("DESCRIPTION"))
                        listDashboard.add(dashboardModel)
                    }

                    when{
                        listDashboard.size > 0 -> _dashboardInterface.onLoadList(listDashboard, typeLoading)
                        else -> {
                            _dashboardInterface.showUI(DashboardFragment.TEXTVIEW_UI)
                            _dashboardInterface.hideUI(ConstantObject.vRecylerViewUI)
                            _dashboardInterface.hideUI(ConstantObject.vGlobalUI)

                            when(typeLoading){
                                ConstantObject.vLoadWithProgressBar -> { _dashboardInterface.hideUI(ConstantObject.vProgresBarUI) }
                                else -> _dashboardInterface.hideSwipeRefreshLayout()
                            }
                            _dashboardInterface.onErrorMessage(_context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
                        }
                    }

                }
            }

            override fun onDataError(error: String?) {
                _dashboardInterface.showUI(DashboardFragment.TEXTVIEW_UI)
                _dashboardInterface.hideUI(ConstantObject.vRecylerViewUI)
                _dashboardInterface.hideUI(ConstantObject.vGlobalUI)

                when(typeLoading){
                    ConstantObject.vLoadWithProgressBar -> { _dashboardInterface.hideUI(ConstantObject.vProgresBarUI) }
                    else -> _dashboardInterface.hideSwipeRefreshLayout()
                }
                _dashboardInterface.onErrorMessage(error.toString(), ConstantObject.vToastError)
            }
        })
    }
}