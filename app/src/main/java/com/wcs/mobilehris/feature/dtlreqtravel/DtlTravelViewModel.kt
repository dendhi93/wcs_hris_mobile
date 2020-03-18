package com.wcs.mobilehris.feature.dtlreqtravel

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.feature.requesttravel.ReqTravelModel
import com.wcs.mobilehris.util.ConstantObject

class DtlTravelViewModel (private val context : Context, private val dtlTravelInterface: DtlTravelInterface) : ViewModel(){
    val isProgressDtlReqTravel = ObservableField(false)
    val isHideDtlTravelUI = ObservableField(false)
    val isConfirmTravelMenu = ObservableField(false)
    val isCitiesView = ObservableField(true)
    val stDtlTravelChargeCode = ObservableField("")
    val stDtlTravelDepartDate = ObservableField("")
    val stDtlTravelReturnDate = ObservableField("")
    val stDtlTravelDescription = ObservableField("")
    val stDtlTravelReason = ObservableField("")
    val stDtlTravelNotes = ObservableField("")
    val stButtonSubmitTravel = ObservableField("")
    private val stDtlTravelIsTB = ObservableField(false)

    private var stIntentFromMenu : String? = null
    private var stIntentTravelId : String? = null

    fun validateDataTravel(intentFrom : String, intentTravelId : String){
        when {
            !ConnectionObject.isNetworkAvailable(context) -> dtlTravelInterface.onAlertDtlReqTravel(context.getString(R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, DtlRequestTravelActivity.ALERT_DTL_REQ_TRAVEL_NO_CONNECTION)
            else -> getDataTravel(intentFrom, intentTravelId)
        }
    }

    private fun getDataTravel(intentFrom : String, intentTravelId : String){
        stIntentFromMenu = intentFrom.trim()
        stIntentTravelId = intentTravelId.trim()
        isProgressDtlReqTravel.set(true)
        isHideDtlTravelUI.set(true)
        isConfirmTravelMenu.set(false)
        when(stIntentFromMenu){
            ConstantObject.extra_fromIntentConfirmTravel -> stButtonSubmitTravel.set(context.getString(R.string.confirm_save))
            else -> stButtonSubmitTravel.set(context.getString(R.string.save))
        }

        Handler().postDelayed({
            stDtlTravelChargeCode.set("A-1003-096 BUSINESS DEVELOPMENT FOR MOBILITY ACTIVITY")
            stDtlTravelDepartDate.set("11-02-2020")
            stDtlTravelReturnDate.set("18-02-2020")
            stDtlTravelIsTB.set(false)
            dtlTravelInterface.selectedTravelWayRadio(stDtlTravelIsTB.get())
            stDtlTravelReason.set("Routine Duty")
            stDtlTravelDescription.set("Test Mobile")

            val dtlListFriend = mutableListOf<FriendModel>()
            var friendModel = FriendModel("62664930","Windy", "Free", false)
            dtlListFriend.add(friendModel)
            friendModel = FriendModel("62405890","Michael Saputra", "Conflict With Heinz ABC", true)
            dtlListFriend.add(friendModel)
            when{dtlListFriend.isNotEmpty() -> dtlTravelInterface.onLoadTeam(dtlListFriend) }

            val dtlListCityTravel = mutableListOf<ReqTravelModel>()
            var reqTravelModel = ReqTravelModel("Jakarta",
                "Bandung",
                "11-02-2020",
                "12-02-2020",
                "TX-TAXI",
                "Airy Room")
            dtlListCityTravel.add(reqTravelModel)
            reqTravelModel = ReqTravelModel("Bandung",
                "Solo",
                "12-02-2020",
                "14-02-2020",
                "TR-TRAIN",
                "Mercure Hotel")
            dtlListCityTravel.add(reqTravelModel)
            when{dtlListCityTravel.isNotEmpty() -> dtlTravelInterface.onLoadCitiesTravel(dtlListCityTravel)}

            isProgressDtlReqTravel.set(false)
            isHideDtlTravelUI.set(false)
            if (intentFrom == ConstantObject.extra_fromIntentConfirmTravel ||
                    intentFrom == ConstantObject.extra_fromIntentApproval){
                isConfirmTravelMenu.set(true)
            }else { isConfirmTravelMenu.set(false) }
        }, 2000)
    }

    fun viewCities(){
        isCitiesView.set(true)
        dtlTravelInterface.onChangeButtonBackground(true)
    }
    fun viewFriends(){
        isCitiesView.set(false)
        dtlTravelInterface.onChangeButtonBackground(false)
    }
    fun onSubmitConfirm(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> dtlTravelInterface.onAlertDtlReqTravel(context.getString(R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, DtlRequestTravelActivity.ALERT_DTL_REQ_TRAVEL_NO_CONNECTION)
            stIntentFromMenu == ConstantObject.extra_fromIntentConfirmTravel -> dtlTravelInterface.onAlertDtlReqTravel(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, DtlRequestTravelActivity.ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT)
            stIntentFromMenu == ConstantObject.extra_fromIntentApproval -> dtlTravelInterface.onAlertDtlReqTravel(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, DtlRequestTravelActivity.ALERT_DTL_APPROVE_TRAVEL_ACCEPT)
        }

    }
    fun onSubmitReject(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> dtlTravelInterface.onAlertDtlReqTravel(context.getString(R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, DtlRequestTravelActivity.ALERT_DTL_REQ_TRAVEL_NO_CONNECTION)
            stIntentFromMenu == ConstantObject.extra_fromIntentConfirmTravel -> dtlTravelInterface.onAlertDtlReqTravel(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, DtlRequestTravelActivity.ALERT_DTL_REQ_TRAVEL_CONFIRMATION_REJECT)
            stIntentFromMenu == ConstantObject.extra_fromIntentApproval -> dtlTravelInterface.onAlertDtlReqTravel(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, DtlRequestTravelActivity.ALERT_DTL_APPROVE_TRAVEL_REJECT)
        }
    }

    fun onProcessConfirm(chooseConfirm : Int){
        isProgressDtlReqTravel.set(true)
        Handler().postDelayed({
            when(chooseConfirm){
                DtlRequestTravelActivity.ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT ->  dtlTravelInterface.onSuccessDtlTravel("Transaction Successful accepted")
                DtlRequestTravelActivity.ALERT_DTL_REQ_TRAVEL_CONFIRMATION_REJECT -> dtlTravelInterface.onSuccessDtlTravel("Transaction Successful rejected")
                DtlRequestTravelActivity.ALERT_DTL_APPROVE_TRAVEL_ACCEPT -> dtlTravelInterface.onSuccessDtlTravel("Transaction Successful approved")
                else -> dtlTravelInterface.onSuccessDtlTravel("Transaction Successful rejected")
            }
        }, 2000)
    }

    fun onBackDtlTravel(intentTravelFrom : String){
        val intent = Intent(context, MenuActivity::class.java)
        when(intentTravelFrom){
            ConstantObject.extra_fromIntentDtlTravel -> intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_REQUEST)
            ConstantObject.extra_fromIntentConfirmTravel -> intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_CONFIRMATION)
            else -> intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_APPROVAL)
        }
        context.startActivity(intent)
    }
}