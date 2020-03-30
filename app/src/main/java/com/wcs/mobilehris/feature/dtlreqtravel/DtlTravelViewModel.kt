package com.wcs.mobilehris.feature.dtlreqtravel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.BuildConfig
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.requesttravel.ReqTravelModel
import com.wcs.mobilehris.util.ConstantObject
import org.json.JSONArray
import org.json.JSONObject

class DtlTravelViewModel (private val context : Context,
                          private val dtlTravelInterface: DtlTravelInterface,
                          private val apiRepo: ApiRepo) : ViewModel(){
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
        if (intentFrom == ConstantObject.extra_fromIntentConfirmTravel ||
                    intentFrom == ConstantObject.extra_fromIntentApproval){
            isConfirmTravelMenu.set(true)
        }else { isConfirmTravelMenu.set(false) }
        when(stIntentFromMenu){
            ConstantObject.extra_fromIntentConfirmTravel -> stButtonSubmitTravel.set(context.getString(R.string.confirm_save))
            else -> stButtonSubmitTravel.set(context.getString(R.string.save))
        }
        apiRepo.getHeaderTravel(BuildConfig.HRIS_URL+"gettravelrequestbyid/"+intentTravelId, context, object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseTravelHeader = it.getString(ConstantObject.vResponseData)
                    val jObjHeaderTravel = JSONObject(responseTravelHeader)
                    stDtlTravelChargeCode.set(jObjHeaderTravel.getString("CHARGE_CD_NAME"))
                    stDtlTravelDepartDate.set(jObjHeaderTravel.getString("DEPART_DATE").split("T")[0])
                    stDtlTravelReturnDate.set(jObjHeaderTravel.getString("RETURN_DATE").split("T")[0])
                    dtlTravelInterface.selectedTravelWayRadio(jObjHeaderTravel.getString("TRAVEL_TYPE_CD"))
                    stDtlTravelReason.set(jObjHeaderTravel.getString("REASON_DESCRIPTION"))
                    stDtlTravelDescription.set(jObjHeaderTravel.getString("DESCRIPTION"))
                    AsyncDtlTravel().execute()
                }
            }

            override fun onDataError(error: String?) {
                isProgressDtlReqTravel.set(false)
                isHideDtlTravelUI.set(false)
                dtlTravelInterface.onMessage("failed header " +error.toString(), ConstantObject.vToastError)
            }
        })
    }

    private fun getDataTeam(){
        val dtlListFriend = mutableListOf<FriendModel>()
        var friendModel : FriendModel
        apiRepo.getHeaderTravel(BuildConfig.HRIS_URL+"gettravelrequestmemberdetailbyheaderid/"+stIntentTravelId,
            context, object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val objectResponseFriend = it.getString(ConstantObject.vResponseData)
                    val jObjArrayFriend = JSONArray(objectResponseFriend)
                    for(i in 0 until jObjArrayFriend.length()){
                        val jObjFriendList = jObjArrayFriend.getJSONObject(i)
                        val booleanStatusTeam = when(jObjFriendList.getString("AVAILABLE")){
                            "Y" -> true
                            else -> false
                        }
                        friendModel = FriendModel(
                            jObjFriendList.getString("NIK"),
                            jObjFriendList.getString("FULL_NAME"),
                            booleanStatusTeam
                        )
                        dtlListFriend.add(friendModel)
                    }
                    when{dtlListFriend.isNotEmpty() -> dtlTravelInterface.onLoadTeam(dtlListFriend) }
                }
            }

            override fun onDataError(error: String?) {
                isProgressDtlReqTravel.set(false)
                isHideDtlTravelUI.set(false)
                dtlTravelInterface.onMessage("failed get team " +error.toString(), ConstantObject.vToastError)
            }
        })
    }

    private fun getDestinationTravel(){
        val dtlListCityTravel = mutableListOf<ReqTravelModel>()
        var reqTravelModel : ReqTravelModel
        apiRepo.getHeaderTravel(BuildConfig.HRIS_URL+"gettravelrequestdetailbyheaderid/"+stIntentTravelId,
            context, object : ApiRepo.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        val objectResponseDestination = it.getString(ConstantObject.vResponseData)
                        val jObjArrayDestination = JSONArray(objectResponseDestination)
                        for(i in 0 until jObjArrayDestination.length()){
                            val jObjDestination = jObjArrayDestination.getJSONObject(i)
                            reqTravelModel = ReqTravelModel(
                                jObjDestination.getString("DESTINATION_FROM"),
                                jObjDestination.getString("DESTINATION_TO"),
                                jObjDestination.getString("START_DATE").split("T")[0],
                                jObjDestination.getString("END_DATE").split("T")[0],
                                jObjDestination.getString("TRANSPORT_NAME")+"-"+jObjDestination.getString("TRANSPORT_NAME"),
                                jObjDestination.getString("ACCOMODATION_NAME")
                            )
                            dtlListCityTravel.add(reqTravelModel)
                            when{dtlListCityTravel.isNotEmpty() -> dtlTravelInterface.onLoadCitiesTravel(dtlListCityTravel)}
                            isProgressDtlReqTravel.set(false)
                            isHideDtlTravelUI.set(false)
                        }
                    }
                }

                override fun onDataError(error: String?) {
                    isProgressDtlReqTravel.set(false)
                    isHideDtlTravelUI.set(false)
                    dtlTravelInterface.onMessage("failed get city " +error.toString(), ConstantObject.vToastError)
                }
            })
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncDtlTravel: AsyncTask<Void, Void, String>(){

        override fun doInBackground(vararg params: Void?): String {
            return "OK"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            getDataTeam()
            getDestinationTravel()
        }
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
}