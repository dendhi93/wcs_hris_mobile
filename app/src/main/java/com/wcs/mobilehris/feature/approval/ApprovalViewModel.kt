package com.wcs.mobilehris.feature.approval

import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.Preference
import org.json.JSONArray
import org.json.JSONObject


class ApprovalViewModel(private val _context : Context,
                        private val _approvalInterface : ApprovalInterface,
                        private val apiRepo: ApiRepo) : ViewModel(){
    val isVisibleApprovalUI = ObservableField(false)
    private var preference: Preference = Preference(_context)

    fun approvalInitMenu(){
        when{
            !ConnectionObject.isNetworkAvailable(_context) ->{
                _approvalInterface.onAlertApproval(_context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, ApprovalFragment.ALERT_APPROVAL_NO_CONNECTION)
            }
            else -> getApprovalMenu()
        }
    }

    private fun getApprovalMenu(){
        isVisibleApprovalUI.set(true)
        val listApprovalMenu = mutableListOf<ApprovalModel>()
        var approvalModel : ApprovalModel
        apiRepo.getCountAllApproval(preference.getUn(), _context, object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseCountAllApproval = it.getString(ConstantObject.vResponseResult)
                    val jArrayCountApproval = JSONArray(responseCountAllApproval)
                    for (i in 0 until jArrayCountApproval.length()) {
                        val jObjCountApproval = jArrayCountApproval.getJSONObject(i)
                        val responseApprovalMenu = jObjCountApproval.getString("TYPE_ACTYVITY")
                        var iconMenu  = 0
                        when(responseApprovalMenu){
                            ConstantObject.activityMenu -> iconMenu = R.mipmap.ic_checklist_64
                            ConstantObject.benefitMenu -> iconMenu = R.mipmap.ic_benefit
                            ConstantObject.leaveMenu -> iconMenu = R.mipmap.ic_edit_user
                            ConstantObject.travelMenu -> iconMenu = R.mipmap.ic_train
                            ConstantObject.travelClaimMenu -> iconMenu = R.mipmap.ic_edit_pen
                        }
                        approvalModel = ApprovalModel(responseApprovalMenu,
                            iconMenu,
                            jObjCountApproval.getString("TOTAL_COUNT").toInt()
                        )
                        listApprovalMenu.add(approvalModel)
                    }
                    _approvalInterface.loadApprovalMenu(listApprovalMenu)
                }
            }

            override fun onDataError(error: String?) {
                _approvalInterface.onMessage(" err approval " +error.toString().trim(), ConstantObject.vToastError)
                isVisibleApprovalUI.set(false)
            }
        })
    }
}