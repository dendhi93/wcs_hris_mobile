package com.wcs.mobilehris.feature.approval

import com.wcs.mobilehris.utilsinterface.ActionInterface

interface ApprovalInterface : ActionInterface.showHideUI {
    fun loadApprovalMenu(approvalMenuList: List<ApprovalModel>)
}