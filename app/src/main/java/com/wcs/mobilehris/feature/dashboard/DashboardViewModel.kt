package com.wcs.mobilehris.feature.dashboard

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.utils.ConstantObject

class DashboardViewModel(val _context : Context, val _dashboardInterface : DashboardInterface) : ViewModel(){
    val stLeaveQty = ObservableField<String>("")
    val stLateQty = ObservableField<String>("")

    fun initDataDashboard(){
        when{
            !ConnectionObject.isNetworkAvailable(_context) ->{
                _dashboardInterface.onAlertDash(_context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection,
                    DashboardFragment.ALERT_DASH_NO_CONNECTION)
            }
            else -> DashAsyncTask().execute()
        }
    }
    fun onInitDashboardMenu(){
        when{
            !ConnectionObject.isNetworkAvailable(_context) ->{
                _dashboardInterface.onAlertDash(_context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection,
                    DashboardFragment.ALERT_DASH_NO_CONNECTION)
            }
            else ->getDashboardList(DashboardFragment.LOAD_WITHOUT_PROGRESBAR)
        }
    }

    private fun getDashboardList(typeLoading : Int){
        when(typeLoading){
            DashboardFragment.LOAD_WITH_PROGRESBAR -> {
                _dashboardInterface.showUI(ConstantObject.vProgresBarUI)
                _dashboardInterface.hideUI(ConstantObject.vGlobalUI)
            }
        }

        _dashboardInterface.hideUI(ConstantObject.vRecylerViewUI)
        var listDashboard = mutableListOf<DashboardModel>()
        var dashBoardModel = DashboardModel("Activity","Metting at 13.00" +
                "\nInterview new Candidate at  15.00" +
                "\nand 2 others")
        listDashboard.add(dashBoardModel)
        dashBoardModel = DashboardModel("Notification","Blood Donation will be held  at 10.00")
        listDashboard.add(dashBoardModel)
        dashBoardModel = DashboardModel("Request","Request by Michael at December 19, about weekend overtime" +
                "\nRequest by Alvin at December 19, about Travel")
        listDashboard.add(dashBoardModel)

        Handler().postDelayed({
            _dashboardInterface.onLoadList(listDashboard, typeLoading)
        }, 2000)
    }

    //by deddy for get lateness qty and leave qty data
    private fun getLateness(){
        Handler().postDelayed({
            stLeaveQty.set("Leave 10")
            stLateQty.set("Lateness 2")
        }, 1000)
    }

    inner class DashAsyncTask() : AsyncTask<Void, Void, String>(){
        override fun doInBackground(vararg params: Void?): String {
            return "OK"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            getLateness()
            getDashboardList(DashboardFragment.LOAD_WITH_PROGRESBAR)
        }
    }

}