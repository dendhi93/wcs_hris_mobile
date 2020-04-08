package com.wcs.mobilehris.feature.approvallistofactivities.detail

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject
import org.json.JSONArray
import org.json.JSONObject

class DetailApprovalViewModel(
    private val _context: Context,
    private val _interface: DetailApprovalInterface,
    private val apiRepo: ApiRepo
) : ViewModel() {

    val isProgressActiApprovalTrans = ObservableField(false)
    val isHiddenContent = ObservableField(false)
    val stChargeCode = ObservableField("")
    val stNik = ObservableField("")
    val stName = ObservableField("")
    val stAvailable = ObservableField("")
    val stDateFrom = ObservableField("")
    val stDateInto = ObservableField("")
    val stCreatedBy = ObservableField("")
    val stApprovalRejectNotes = ObservableField("")

    private var strDocNo : String = ""

    fun onInitApprovalData(activityId: String) {
        when {
            !ConnectionObject.isNetworkAvailable(_context) -> _interface.onAlertApproval(
                _context.getString(
                    R.string.alert_no_connection
                ),
                ConstantObject.vAlertDialogNoConnection,
                DetailApprovalActivity.ALERT_APPROVAL_TRANS_NO_CONNECTION
            )
            else -> getDetailActivityData(activityId)
        }
    }

    fun getDetailActivityData(activityId: String) {
        isProgressActiApprovalTrans.set(true)
        isHiddenContent.set(true)

        apiRepo.getDetailActivity(activityId, _context, object : ApiRepo.ApiCallback<JSONObject> {
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseDetail = it.getString(ConstantObject.vResponseData)
                    val arrayDetails = JSONArray(responseDetail)
                    val objDetail = arrayDetails.getJSONObject(0)

                    stChargeCode.set(objDetail.getString("CHARGE_CD"))
                    stNik.set(objDetail.getString("NIK"))
                    stName.set(objDetail.getString("EMPLOYEE_NAME"))
                    stAvailable.set(objDetail.getString("AVAILABLE"))
                    stDateFrom.set(objDetail.getString("DATE_FROM"))
                    stDateInto.set(objDetail.getString("DATE_TO"))
                    stCreatedBy.set(objDetail.getString("CREATED_BY"))

                    isProgressActiApprovalTrans.set(false)
                    isHiddenContent.set(false)
                }
            }

            override fun onDataError(error: String?) {
                isProgressActiApprovalTrans.set(false)
                _interface.onMessage(
                    "failed activity approval detail " + error.toString(), ConstantObject.vToastError
                )
            }

        })
    }

    fun clickActivityApproval() {
        when {
            !ConnectionObject.isNetworkAvailable(_context) -> _interface.onAlertApproval(
                _context.getString(R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection,
                DetailApprovalActivity.ALERT_APPROVAL_TRANS_NO_CONNECTION)

            else -> (_interface.onAlertApproval(_context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation,
                DetailApprovalActivity.ALERT_APPROVAL_TRANS_APPROVE))
        }
    }

    fun clickActivityRejection() {
        when {
            !ConnectionObject.isNetworkAvailable(_context) -> _interface.onAlertApproval(
                _context.getString(R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection,
                DetailApprovalActivity.ALERT_APPROVAL_TRANS_NO_CONNECTION)

            else -> (_interface.onAlertApproval(_context.getString(R.string.transaction_alert_confirmation),
                ConstantObject.vAlertDialogConfirmation,
                DetailApprovalActivity.ALERT_APPROVAL_TRANS_REJECT))
        }
    }

    fun onSubmitApproval(clickAlertType : Int, intentDocumentNo : String) {
        isProgressActiApprovalTrans.set(true)

        when(clickAlertType) {
            DetailApprovalActivity.ALERT_APPROVAL_TRANS_APPROVE -> approveActivity(intentDocumentNo)
            DetailApprovalActivity.ALERT_APPROVAL_TRANS_REJECT -> rejectActivity(intentDocumentNo, stApprovalRejectNotes.get().toString())
            else -> {
                Handler().postDelayed({
                    when(clickAlertType) {
                        DetailApprovalActivity.ALERT_APPROVAL_TRANS_APPROVE -> _interface.onSuccessApprovalTrans("Transaction Success : Approved")
                        DetailApprovalActivity.ALERT_APPROVAL_TRANS_REJECT -> _interface.onSuccessApprovalTrans(("Transaction Success : Rejected"))
                    }
                }, 2000)
            }
        }
    }

    fun approveActivity(docNo : String) {
        apiRepo.approveActivityDetail(docNo, _context, object : ApiRepo.ApiCallback<JSONObject> {
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val objectResponse = it.getString(ConstantObject.vResponseData)
                    Log.d("###", "approveActivity -> response = $data")
                    _interface.onSuccessApprovalTrans(_context.getString(R.string.alert_transaction_success))
                }
            }

            override fun onDataError(error: String?) {
                isProgressActiApprovalTrans.set(false)
                _interface.onMessage("failed approve activity " +error.toString(), ConstantObject.vToastError)
            }

        })
    }

    fun rejectActivity(docNo : String, reason : String) {
        apiRepo.rejectActivityDetail(docNo, reason, _context, object : ApiRepo.ApiCallback<JSONObject> {
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val objectResponse = it.getString(ConstantObject.vResponseData)
                    Log.d("###", "rejectActivity -> response = $data")
                    _interface.onSuccessApprovalTrans(_context.getString(R.string.alert_transaction_success))
                }
            }

            override fun onDataError(error: String?) {
                isProgressActiApprovalTrans.set(false)
                _interface.onMessage("failed reject activity " +error.toString(), ConstantObject.vToastError)
            }

        })
    }

}