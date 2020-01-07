package com.wcs.mobilehris.feature.dtltask

interface DtlTaskInterface {
    fun loadTeam(listTeam : List<FriendModel>)
    fun onErrorMessage(message : String, messageType : Int)
    fun onAlertCompleted(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onSetHintFreeEt(hintTitle : String)
    fun onSetCheckedRadio(isOnsite : Boolean)
}