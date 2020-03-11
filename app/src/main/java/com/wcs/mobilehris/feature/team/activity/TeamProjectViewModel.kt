package com.wcs.mobilehris.feature.team.activity

import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject

class TeamProjectViewModel (private val context: Context, private val teamProjectInterface: TeamProjectInterface) : ViewModel(){
    val isProgressVisibleTeam = ObservableField(false)
    val isVisibleRecylerView = ObservableField(false)

    fun initDataTeamProject(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> teamProjectInterface.onAlertTeamProject(context.getString(
                R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, TeamActivity.ALERT_TEAM_NO_CONNECTION)
            else -> getTeamProjectData()
        }
    }

    private fun getTeamProjectData(){
        isProgressVisibleTeam.set(false)
        val listTeamProject = mutableListOf<TeamProjectModel>()
        var teamPrjModel = TeamProjectModel("1","Andika","Conflict")
        listTeamProject.add(teamPrjModel)
        teamPrjModel = TeamProjectModel("2","Michael Saputra","Conflict")
        listTeamProject.add(teamPrjModel)
        teamPrjModel = TeamProjectModel("3","Yulseha Putra","Available")
        listTeamProject.add(teamPrjModel)
        teamPrjModel = TeamProjectModel("4","Andri Sebastian","Available")
        listTeamProject.add(teamPrjModel)
        teamPrjModel = TeamProjectModel("5","Khanif Hanafi","Available")
        listTeamProject.add(teamPrjModel)

        when(listTeamProject.size){
            0 -> {
                isProgressVisibleTeam.set(false)
                isVisibleRecylerView.set(false)
                teamProjectInterface.onErrorMessage(context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
            }
            else -> {
                Handler().postDelayed({
                    teamProjectInterface.onLoadTeamProject(listTeamProject)
                }, 2000)
            }
        }
    }
}