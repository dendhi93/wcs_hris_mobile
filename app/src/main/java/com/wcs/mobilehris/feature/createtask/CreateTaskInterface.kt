package com.wcs.mobilehris.feature.createtask

import com.wcs.mobilehris.feature.dtltask.FriendModel

interface CreateTaskInterface {

    fun onLoadTeam(listTeam : List<FriendModel>)
    fun onMessage(message : String, messageType : Int)
    fun onAlertCreateTask(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onResizeLayout(resizeType : Int)
}