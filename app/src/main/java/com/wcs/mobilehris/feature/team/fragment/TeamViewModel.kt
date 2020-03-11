package com.wcs.mobilehris.feature.team.fragment

import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.team.fragment.TeamFragment
import com.wcs.mobilehris.feature.team.fragment.TeamInterface
import com.wcs.mobilehris.feature.team.fragment.TeamModel
import com.wcs.mobilehris.util.ConstantObject

class TeamViewModel (private val context : Context, private val teamInterface: TeamInterface) : ViewModel(){
    val isHiddenSearch = ObservableField(false)

    fun initDataTeam(typeOfLoading : Int){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> teamInterface.onAlertTeam(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, TeamFragment.ALERT_TEAM_NO_CONNECTION)
            else -> getTeamData(typeOfLoading)
        }
    }

    private fun getTeamData(typeLoading : Int){
        teamInterface.hideUI(ConstantObject.vRecylerViewUI)
        teamInterface.showUI(ConstantObject.vGlobalUI)
        val listTeam = mutableListOf<TeamModel>()
        var teamModel = TeamModel(
            "62909090", "Andika Kurnia",
            "+620878945445",
            "andika.kurnia@id.wilmar-intl.com"
        )
        listTeam.add(teamModel)
        teamModel = TeamModel(
            "62909091", "Claudia",
            "+620878945489",
            "claudia@id.wilmar-intl.com"
        )
        listTeam.add(teamModel)
        teamModel = TeamModel(
            "62909092", "Michael Saputra",
            "+62087894547878",
            "michael.saputra@id.wilmar-intl.com"
        )
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
                    ConstantObject.vLoadWithProgressBar -> teamInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> teamInterface.onHideSwipeRefresh()
                }
                teamInterface.onErrorMessage(context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
            }
        }
    }
}