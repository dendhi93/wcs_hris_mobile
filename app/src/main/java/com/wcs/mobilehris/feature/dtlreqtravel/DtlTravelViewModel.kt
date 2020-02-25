package com.wcs.mobilehris.feature.dtlreqtravel

import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.requesttravel.ReqTravelModel
import com.wcs.mobilehris.util.ConstantObject

class DtlTravelViewModel (private val context : Context, private val dtlTravelInterface: DtlTravelInterface) : ViewModel(){
    val isProgressDtlReqTravel = ObservableField<Boolean>(false)
    val isHideDtlTravelUI = ObservableField<Boolean>(false)
    val isConfirmTravelMenu = ObservableField<Boolean>(false)
    val isCitiesView = ObservableField<Boolean>(true)
    val stDtlTravelChargeCode = ObservableField<String>("")
    val stDtlTravelDepartDate = ObservableField<String>("")
    val stDtlTravelReturnDate = ObservableField<String>("")
    val stDtlTravelDescription = ObservableField<String>("")
    val stDtlTravelReason = ObservableField<String>("")
    val stDtlTravelNotes = ObservableField<String>("")
    private val stDtlTravelIsTB = ObservableField<Boolean>(false)

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
            when(intentFrom){
                ConstantObject.extra_fromIntentConfirmTravel -> isConfirmTravelMenu.set(true)
                else -> isConfirmTravelMenu.set(false)
            }
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
    fun onSubmitConfirm(){dtlTravelInterface.onAlertDtlReqTravel(context.getString(R.string.transaction_alert_confirmation),
        ConstantObject.vAlertDialogConfirmation, DtlRequestTravelActivity.ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT)}
    fun onSubmitReject(){dtlTravelInterface.onAlertDtlReqTravel(context.getString(R.string.transaction_alert_confirmation),
        ConstantObject.vAlertDialogConfirmation, DtlRequestTravelActivity.ALERT_DTL_REQ_TRAVEL_CONFIRMATION_REJECT)}

    fun onProcessConfirm(chooseConfirm : Int){
        isProgressDtlReqTravel.set(true)
        Handler().postDelayed({
            when(chooseConfirm){
                DtlRequestTravelActivity.ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT ->  dtlTravelInterface.onSuccessDtlTravel("Transaction Successful accepted")
                else -> dtlTravelInterface.onSuccessDtlTravel("Transaction Successful rejected")
            }
        }, 2000)
    }
}