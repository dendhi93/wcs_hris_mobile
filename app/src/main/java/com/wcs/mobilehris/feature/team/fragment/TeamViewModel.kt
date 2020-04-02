package com.wcs.mobilehris.feature.team.fragment

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
import org.json.JSONArray
import org.json.JSONObject

class TeamViewModel (private val context : Context,
                     private val teamInterface: TeamInterface,
                     private val apiRepo: ApiRepo) : ViewModel(){
    val isHiddenSearch = ObservableField(false)

    fun initDataTeam(teamName : String){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> teamInterface.onAlertTeam(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, TeamFragment.ALERT_TEAM_NO_CONNECTION)
            else -> getTeamData(teamName)
        }
    }

    private fun getTeamData(teamName : String){
        teamInterface.hideUI(ConstantObject.vRecylerViewUI)
        teamInterface.showUI(ConstantObject.vGlobalUI)
        teamInterface.showUI(ConstantObject.vProgresBarUI)
        val listTeam = mutableListOf<TeamModel>()
        var teamModel : TeamModel
        apiRepo.searchDataTeam(ConstantObject.extra_fromIntentSearchTeam, teamName, DateTimeUtils.getCurrentDate(), DateTimeUtils.getCurrentDate(), context, object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseSearchTeam = it.getString(ConstantObject.vResponseData)
                    val jArrayTeam = JSONArray(responseSearchTeam)
                    if(jArrayTeam.length() > 0){
                        for(i in 0 until jArrayTeam.length()){
                            val jObjTeam = jArrayTeam.getJSONObject(i)

                            teamModel = TeamModel(
                                jObjTeam.getString("NIK"),
                                jObjTeam.getString("FULL_NAME"),
                                jObjTeam.getString("PHONE_NUMBER"),
                                jObjTeam.getString("EMAIL")

                            )
                            listTeam.add(teamModel)
                        }
                    }

                    teamInterface.onClearTeamList()
                    when(listTeam.size){
                        0 -> {
                        teamInterface.showUI(ConstantObject.vGlobalUI)
                        teamInterface.hideUI(ConstantObject.vRecylerViewUI)
                        teamInterface.hideUI(ConstantObject.vProgresBarUI)
                        }
                        else -> teamInterface.onLoadTeam(listTeam)
                    }
                }
            }

            override fun onDataError(error: String?) {
                teamInterface.onClearTeamList()
                teamInterface.showUI(ConstantObject.vGlobalUI)
                teamInterface.hideUI(ConstantObject.vRecylerViewUI)
                teamInterface.hideUI(ConstantObject.vProgresBarUI)
            }
        })
    }
}