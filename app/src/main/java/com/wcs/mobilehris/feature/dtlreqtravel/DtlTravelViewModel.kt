package com.wcs.mobilehris.feature.dtlreqtravel

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.BuildConfig
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.requesttravel.ReqTravelModel
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
import com.wcs.mobilehris.util.Preference
import org.json.JSONArray
import org.json.JSONObject

class DtlTravelViewModel (private val context : Context,
                          private val dtlTravelInterface: DtlTravelInterface,
                          private val apiRepo: ApiRepo) : ViewModel(){
    val isProgressDtlReqTravel = ObservableField(false)
    val isHideDtlTravelUI = ObservableField(false)
    val isConfirmTravelMenu = ObservableField(false)
    val isHiddenReject = ObservableField(false)
    val isCitiesView = ObservableField(true)
    val isEditTravel = ObservableField(false)
    val stDtlTravelChargeCode = ObservableField("")
    val stDtlTravelDepartDate = ObservableField("")
    val stDtlTravelReturnDate = ObservableField("")
    val stDtlTravelDescription = ObservableField("")
    val stDtlTravelReason = ObservableField("")
    val stDtlTravelNotesReject = ObservableField("")
    val stButtonSubmitTravel = ObservableField("")
    val stButtonRejectTravel = ObservableField("")
    val stDocNumber = ObservableField("")
    val stIntentTravelHeaderId = ObservableField("")
    private val dtlListFriend = mutableListOf<FriendModel>()
    private val dtlListCityTravel = mutableListOf<ReqTravelModel>()
    private var stIntentFromMenu : String? = ""
    private var stIntentTravelId : String? = ""
    private var stDtlReasonCode : String? = ""
    private var stDtlCodeofChargeCode : String? = ""
    private var stDtlTBType : String? = ""
    private var stDtlDocDate : String? = ""
    private var preference = Preference(context)

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
                    intentFrom == ConstantObject.extra_fromIntentApproval ) {
            isConfirmTravelMenu.set(true)
            isHiddenReject.set(false)
        }else if (intentFrom == ConstantObject.vEditTask){
            isConfirmTravelMenu.set(true)
            isHiddenReject.set(false)
        }else { isConfirmTravelMenu.set(false) }
        when(stIntentFromMenu){
            ConstantObject.extra_fromIntentConfirmTravel -> {
                stButtonSubmitTravel.set(context.getString(R.string.confirm_save))
                stButtonRejectTravel.set(context.getString(R.string.reject_save))
                isEditTravel.set(false)
            }
            ConstantObject.vEditTask -> {
                stButtonSubmitTravel.set(context.getString(R.string.save))
                stButtonRejectTravel.set(context.getString(R.string.delete_save))
                isEditTravel.set(true)
            }
            else -> {
                stButtonSubmitTravel.set(context.getString(R.string.appr_button))
                stButtonRejectTravel.set(context.getString(R.string.reject_save))
                isEditTravel.set(false)
            }
        }
        apiRepo.getHeaderTravel(BuildConfig.HRIS_URL+"gettravelrequestbyid/"+intentTravelId, context, object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseTravelHeader = it.getString(ConstantObject.vResponseData)
                    val jObjHeaderTravel = JSONObject(responseTravelHeader)
                    stDtlTravelChargeCode.set(jObjHeaderTravel.getString("CHARGE_CD_NAME"))
                    stDtlCodeofChargeCode = jObjHeaderTravel.getString("CHARGE_CD")
                    stDtlTravelDepartDate.set(jObjHeaderTravel.getString("DEPART_DATE").split("T")[0])
                    stDtlTravelReturnDate.set(jObjHeaderTravel.getString("RETURN_DATE").split("T")[0])
                    stDtlTBType = jObjHeaderTravel.getString("TRAVEL_TYPE_CD")
                    stDtlDocDate = jObjHeaderTravel.getString("DOCUMENT_DATE").split("T")[0]
                    dtlTravelInterface.selectedTravelWayRadio(stDtlTBType.toString().trim())
                    stDtlTravelReason.set(jObjHeaderTravel.getString("REASON_DESCRIPTION"))
                    stDtlReasonCode = jObjHeaderTravel.getString("REASON")
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
        var friendModel : FriendModel
        apiRepo.getHeaderTravel(BuildConfig.HRIS_URL+"gettravelrequestmemberdetailbyheaderid/"+stIntentTravelHeaderId.get()?.trim(),
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
        var reqTravelModel : ReqTravelModel
        apiRepo.getHeaderTravel(BuildConfig.HRIS_URL+"gettravelrequestdetailbyheaderid/"+stIntentTravelHeaderId.get()?.trim(),
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
                                jObjDestination.getString("TRANSPORT_TYPE_CODE").trim()+"-"+jObjDestination.getString("TRANSPORT_NAME"),
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
            stIntentFromMenu == ConstantObject.vEditTask -> dtlTravelInterface.onAlertDtlReqTravel(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, DtlRequestTravelActivity.ALERT_DTL_APPROVE_TRAVEL_EDIT)
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
            stIntentFromMenu == ConstantObject.vEditTask -> dtlTravelInterface.onAlertDtlReqTravel(context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation, DtlRequestTravelActivity.ALERT_DTL_APPROVE_TRAVEL_DELETE)
        }
    }

    //add api here
    fun onProcessConfirm(chooseConfirm : Int){
        isProgressDtlReqTravel.set(true)
        when(chooseConfirm){
            DtlRequestTravelActivity.ALERT_DTL_APPROVE_TRAVEL_ACCEPT -> submitApproveTravel()
            DtlRequestTravelActivity.ALERT_DTL_APPROVE_TRAVEL_REJECT -> submitRejectTravel()
            DtlRequestTravelActivity.ALERT_DTL_APPROVE_TRAVEL_DELETE -> deleteTravelReq()
            DtlRequestTravelActivity.ALERT_DTL_APPROVE_TRAVEL_EDIT -> editReqTravel()
            else ->{
                Handler().postDelayed({
                    when(chooseConfirm){
                        DtlRequestTravelActivity.ALERT_DTL_REQ_TRAVEL_CONFIRMATION_ACCEPT ->  dtlTravelInterface.onSuccessDtlTravel("Transaction Successful accepted")
                        else -> dtlTravelInterface.onSuccessDtlTravel("Transaction confirmation Successful rejected")
                    }
                }, 2000)
            }
        }
    }

    private fun submitApproveTravel(){
        apiRepo.postApproveTravel(stDocNumber.get().toString().trim(),
            "",DtlRequestTravelActivity.approvalAccept,
            context, object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseAcceptTravel = it.getString(ConstantObject.vResponseData)
                    val jObjAccept = JSONObject(responseAcceptTravel)
                    val stSuccess = jObjAccept.getString("STATUS")
                    if(stSuccess == "SUCCESS"){dtlTravelInterface.onSuccessDtlTravel("Transaction successfully approved")}
                }
            }

            override fun onDataError(error: String?) {
                dtlTravelInterface.onMessage("err approve " +error.toString(), ConstantObject.vToastError)
                isProgressDtlReqTravel.set(true)
            }
        })
    }

    private fun submitRejectTravel(){
        apiRepo.postApproveTravel(stDocNumber.get().toString().trim(),
            stDtlTravelNotesReject.get().toString().trim(),DtlRequestTravelActivity.approvalReject,
            context, object : ApiRepo.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        val responseAcceptTravel = it.getString(ConstantObject.vResponseData)
                        val jObjAccept = JSONObject(responseAcceptTravel)
                        val stSuccess = jObjAccept.getString("STATUS")
                        if(stSuccess == "SUCCESS"){dtlTravelInterface.onSuccessDtlTravel("Transaction Successful rejected")}
                    }
                }

                override fun onDataError(error: String?) {
                    dtlTravelInterface.onMessage("err approve travel" +error.toString(), ConstantObject.vToastError)
                    isProgressDtlReqTravel.set(false)
                }
            })
    }

    private fun deleteTravelReq(){
        apiRepo.deleteTravelReq(stIntentTravelId.toString().trim(), context, object :ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseDelTravel = it.getString(ConstantObject.vResponseStatus)
                    if(responseDelTravel == ConstantObject.vValueResponseSuccess){dtlTravelInterface.onSuccessDtlTravel("Transaction successfully deleted")}
                }
            }

            override fun onDataError(error: String?) {
                dtlTravelInterface.onMessage("err delete travel " +error.toString(), ConstantObject.vToastError)
                isProgressDtlReqTravel.set(false)
            }
        })
    }

    private fun editReqTravel(){
        apiRepo.postReqTravelReq(initjObjEditTravel(dtlListFriend, dtlListCityTravel), context, object :ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseEditTravel = it.getString(ConstantObject.vResponseMessage)
                    if(responseEditTravel.trim() == "Travel Request Successfully Added"){
                        dtlTravelInterface.onSuccessDtlTravel("Transaction successfully edited")
                    }else{
                        dtlTravelInterface.onMessage(responseEditTravel.trim(), ConstantObject.vToastInfo)
                        isProgressDtlReqTravel.set(false)
                    }
                }
            }

            override fun onDataError(error: String?) {
                dtlTravelInterface.onMessage("err edit travel " +error.toString(), ConstantObject.vToastError)
                Log.d("###",""+error.toString())
                isProgressDtlReqTravel.set(false)
            }
        })
    }

    private fun initjObjEditTravel(listTeam: List<FriendModel>, listCity: List<ReqTravelModel>) : JSONObject{
        val reqTravelParam  = JSONObject()
        val jObjTravelHeader = JSONObject()
        val jObjTravelDtl = JSONObject()
        val jArrayTravelHeaders = JSONArray()
        val jArrayTravelDtls = JSONArray()

        for(k in listTeam.indices){
            Log.d("###", "loop $k")
            Log.d("###", "loop friend "+listTeam[k].friendId)

            jObjTravelHeader.put("ID",stIntentTravelId?.toInt())
            jObjTravelHeader.put("ID_TR_HEADER",stIntentTravelHeaderId.get()?.toInt())
            jObjTravelHeader.put("REASON",stDtlReasonCode.toString().trim())
            jObjTravelHeader.put("DESCRIPTION",stDtlTravelDescription.get().toString())
            jObjTravelHeader.put("CHARGE_CD",stDtlCodeofChargeCode.toString().trim())
            jObjTravelHeader.put("TRAVEL_TYPE_CD",stDtlTBType.toString().trim())
            jObjTravelHeader.put("NIK",listTeam[k].friendId)
            jObjTravelHeader.put("REQUESTOR_NIK",preference.getUn())
            jObjTravelHeader.put("DURATION",DateTimeUtils.getDifferentDate(stDtlTravelDepartDate.get().toString().trim(), stDtlTravelReturnDate.get().toString().trim())+1)
            jObjTravelHeader.put("DEPART_DATE",stDtlTravelDepartDate.get().toString())
            jObjTravelHeader.put("RETURN_DATE",stDtlTravelReturnDate.get().toString())
            jObjTravelHeader.put("DOCUMENT_NUMBER","")
            jObjTravelHeader.put("DOCUMENT_DATE",stDtlDocDate.toString().trim())
            jObjTravelHeader.put("TRIP_ADVANCE",0)
            jObjTravelHeader.put("STATUS_CD","W")
            jObjTravelHeader.put("REMARK_REJECTED","")
            jObjTravelHeader.put("CREATED_BY",preference.getUn())
            jObjTravelHeader.put("CREATED_DT","")
            jObjTravelHeader.put("MODIFIED_BY","")
            jObjTravelHeader.put("MODIFIED_DT","")
            jObjTravelHeader.put("APPROVED_BY","")
            jObjTravelHeader.put("APPROVED_DT","")
            jObjTravelHeader.put("REJECTED_BY","")
            jObjTravelHeader.put("REJECTED_DT","")
            jObjTravelHeader.put("ISDELETED","")
            jObjTravelHeader.put("ISMEMBER_CONFIRM","")
            jObjTravelHeader.put("ISAPPROVED","")
            jObjTravelHeader.put("ISMEMBER_REJECTED","")
            jObjTravelHeader.put("ISREJECTED","")
            jObjTravelHeader.put("ISCOMPLETED","")

            jArrayTravelHeaders.put(jObjTravelHeader)
        }

        for(l in listCity.indices){
            jObjTravelDtl.put("ID",stIntentTravelId?.toInt())
            jObjTravelDtl.put("ID_TR_HEADER",stIntentTravelHeaderId.get()?.toInt())
            jObjTravelDtl.put("TRANSPORT_TYPE_CODE",listCity[l].transType.trim().split("-")[0])
            jObjTravelDtl.put("TRANSPORT_NAME","")
            jObjTravelDtl.put("TRANSPORT_NUMBER","")
            jObjTravelDtl.put("TRANSPORT_FROM", "")
            jObjTravelDtl.put("TRANSPORT_TO","")
            jObjTravelDtl.put("TRANSPORT_DATE","")
            jObjTravelDtl.put("TRANSPORT_TIME","")
            jObjTravelDtl.put("DESTINATION_FROM",listCity[l].depart.trim())
            jObjTravelDtl.put("DESTINATION_TO",listCity[l].arrival.trim())
            jObjTravelDtl.put("START_DATE",stDtlTravelDepartDate.get().toString())
            jObjTravelDtl.put("END_DATE",stDtlTravelReturnDate.get().toString())
            jObjTravelDtl.put("DURATION", DateTimeUtils.getDifferentDate(stDtlTravelDepartDate.get().toString().trim(), stDtlTravelReturnDate.get().toString().trim())+1)
            jObjTravelDtl.put("ACCOMODATION_NAME",listCity[l].hotelName)
            jObjTravelDtl.put("ACCOMODATION_LOCATION",stDtlTravelReturnDate.get().toString())
            jObjTravelDtl.put("CHECK_IN",listCity[l].dateCheckIn)
            jObjTravelDtl.put("CHECK_OUT",listCity[l].dateCheckOut)
            jObjTravelDtl.put("REMARK","")
            jObjTravelDtl.put("CREATED_BY",preference.getUn())
            jObjTravelDtl.put("CREATED_DT",DateTimeUtils.getCurrentDate())
            jObjTravelDtl.put("MODIFIED_BY","")
            jObjTravelDtl.put("MODIFIED_DT","")
            jObjTravelDtl.put("ISDELETED","False")

            jArrayTravelDtls.put(jObjTravelDtl)
        }

        reqTravelParam.put("TravelRequestHeader",jArrayTravelHeaders)
        reqTravelParam.put("TravelRequestDetail",jArrayTravelDtls)


        return reqTravelParam
    }
}