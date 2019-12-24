package com.wcs.mobilehris.feature.approval

import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject


class ApprovalViewModel(private val _context : Context, private val _approvalInterface : ApprovalInterface) : ViewModel(){
    val isVisibleApprovalUI = ObservableField<Boolean>(false)

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
        var listApprovalMenu = mutableListOf<ApprovalModel>()
        var _approvalModel = ApprovalModel("Travel", R.mipmap.ic_train, 2, "Last request by Andika about an hour ago")
        listApprovalMenu.add(_approvalModel)
        _approvalModel = ApprovalModel("Leave", R.mipmap.ic_edit_user, 0, "You got none")
        listApprovalMenu.add(_approvalModel)
        _approvalModel = ApprovalModel("Benefit", R.mipmap.ic_benefit, 1, "Last request by Andika less than an hour ago")
        listApprovalMenu.add(_approvalModel)
        _approvalModel = ApprovalModel("Travel Claim", R.mipmap.ic_edit_pen, 10, "Last request by Jean less than two hour ago")
        listApprovalMenu.add(_approvalModel)

        Handler().postDelayed({
            _approvalInterface.loadApprovalMenu(listApprovalMenu)
        }, 2000)
    }
}