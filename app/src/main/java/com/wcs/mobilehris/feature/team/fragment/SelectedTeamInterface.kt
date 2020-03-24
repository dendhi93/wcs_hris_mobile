package com.wcs.mobilehris.feature.team.fragment

import com.wcs.mobilehris.feature.team.activity.TeamProjectModel

interface SelectedTeamInterface {
    fun selectedItemTeam(teamModel : TeamModel)

    interface SelectedTeamProjectInterface{
        fun selectedItemNameProject(teamProjectModel: TeamProjectModel)
    }
}