package com.wcs.mobilehris.feature.createtask

import com.wcs.mobilehris.database.entity.ChargeCodeEntity
import com.wcs.mobilehris.database.entity.TransportTypeEntity
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.utilinterface.DialogInterface

interface CreateTaskInterface : DialogInterface {

    fun onLoadTeam(listTeam : List<FriendModel>)
    fun onMessage(message : String, messageType : Int)
    fun onAlertCreateTask(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onResizeLayout(resizeType : Int)
    fun onLoadChargeCode(listChargeCode : List<ChargeCodeEntity>)
    fun getTeamData()
    fun onSuccessCreateTask()
}