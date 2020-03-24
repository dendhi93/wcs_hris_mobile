package com.wcs.mobilehris.feature.team.activity

import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject
import org.json.JSONArray
import org.json.JSONObject

class TeamProjectViewModel (private val context: Context, private val teamProjectInterface: TeamProjectInterface, private val apiRepo: ApiRepo) : ViewModel(){
    val isProgressVisibleTeam = ObservableField(false)
    val isVisibleRecylerView = ObservableField(false)

    fun initDataTeamProject(selectedTeamName : String, teamDateFrom : String, teamDateInto : String){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> teamProjectInterface.onAlertTeamProject(context.getString(
                R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, TeamActivity.ALERT_TEAM_NO_CONNECTION)
            else -> getTeamProjectData(selectedTeamName, teamDateFrom, teamDateInto)
        }
    }

    private fun getTeamProjectData(selectedTeamName : String, teamDateFrom : String, teamDateInto : String){
        isProgressVisibleTeam.set(true)
        val listTeamProject = mutableListOf<TeamProjectModel>()
        var teamPrjModel : TeamProjectModel
        apiRepo.searchDataTeam(selectedTeamName, teamDateFrom, teamDateInto, context, object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseSearchTeam = it.getString(ConstantObject.vResponseData)
                    val jArrayTeamProject = JSONArray(responseSearchTeam)
                    if(jArrayTeamProject.length() > 0){
                        for(i in 0 until jArrayTeamProject.length()){
                            val jObjTeamProject = jArrayTeamProject.getJSONObject(i)
                            val finalStatusTeam = when(jObjTeamProject.getString("AVAILABLE")){
                                "Y" -> "Available"
                                else -> "Conflict"
                            }
                            teamPrjModel = TeamProjectModel(
                                jObjTeamProject.getString("NIK"),
                                jObjTeamProject.getString("FULL_NAME"),
                                finalStatusTeam
                            )
                            listTeamProject.add(teamPrjModel)
                        }
                    }

                    teamProjectInterface.clearList()
                    when(listTeamProject.size){
                        0 -> {
                            isProgressVisibleTeam.set(false)
                            isVisibleRecylerView.set(false)
                        }
                        else -> teamProjectInterface.onLoadTeamProject(listTeamProject)
                    }
                }
            }

            override fun onDataError(error: String?) {
                teamProjectInterface.clearList()
                isProgressVisibleTeam.set(false)
            }

        })
    }
}