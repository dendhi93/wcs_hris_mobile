package com.wcs.mobilehris.feature.dtltask

interface DtlTaskInterface {
    fun loadTeam(listTeam : List<FriendModel>)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertDtlTask(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun enableUI()
    fun disableUI()
    fun onSuccessEditTask()
    fun onSuccessDeleteTask()
}