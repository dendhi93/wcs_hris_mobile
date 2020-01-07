package com.wcs.mobilehris.feature.team

import android.content.Context
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject

class TeamViewModel (private val context : Context, private val teamInterface: TeamInterface) : ViewModel(){

    fun initDataTeam(typeOfLoading : Int){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> {
                teamInterface.onAlertTeam(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, TeamFragment.ALERT_TEAM_NO_CONNECTION)
            }
            else -> getTemData(typeOfLoading)
        }
    }

    private fun getTemData(typeLoading : Int){
        when(typeLoading){
            ConstantObject.loadWithProgressBar -> teamInterface.showUI(ConstantObject.vProgresBarUI)
        }
        teamInterface.hideUI(ConstantObject.vRecylerViewUI)
        teamInterface.showUI(ConstantObject.vGlobalUI)
        val listTeam = mutableListOf<TeamModel>()
        var teamModel = TeamModel("Andika Kurnia",
            "+620878945445",
            "andika.kurnia@id.wilmar-intl.com")
        listTeam.add(teamModel)
        teamModel = TeamModel("Claudia",
            "+620878945489",
            "claudia@id.wilmar-intl.com")
        listTeam.add(teamModel)
        teamModel = TeamModel("Michael Saputra",
            "+62087894547878",
            "michael.saputra@id.wilmar-intl.com")
        listTeam.add(teamModel)

        when{
            listTeam.size > 0 -> {
                Handler().postDelayed({
                    teamInterface.onLoadTeam(listTeam, typeLoading)
                }, 2000)
            }
            else -> {
                teamInterface.showUI(ConstantObject.vGlobalUI)
                teamInterface.hideUI(ConstantObject.vRecylerViewUI)
                when(typeLoading){
                    ConstantObject.loadWithProgressBar -> teamInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> teamInterface.onHideSwipeRefresh()
                }
                teamInterface.onErrorMessage(context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
            }
        }


    }

}