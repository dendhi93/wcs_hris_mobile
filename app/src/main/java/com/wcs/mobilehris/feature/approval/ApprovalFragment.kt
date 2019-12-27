package com.wcs.mobilehris.feature.approval

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentApprovalBinding
import com.wcs.mobilehris.util.MessageUtils

class ApprovalFragment : Fragment(), ApprovalInterface {
    private lateinit var approvalBinding : FragmentApprovalBinding
    private var arrApprovalList = ArrayList<ApprovalModel>()
    private lateinit var approvalAdapter: CustomApprovalAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        approvalBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_approval, container, false)
        approvalBinding.viewModel = ApprovalViewModel(requireContext(), this)

        return approvalBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        approvalBinding.rcApproval.layoutManager = LinearLayoutManager(requireContext())
        approvalBinding.rcApproval.setHasFixedSize(true)
        approvalAdapter = CustomApprovalAdapter(requireContext(), arrApprovalList)
        approvalBinding.rcApproval.adapter = approvalAdapter

        approvalBinding.viewModel?.approvalInitMenu()
    }
    override fun loadApprovalMenu(approvalMenuList: List<ApprovalModel>) {
        arrApprovalList.clear()
        for(i in approvalMenuList.indices){
            arrApprovalList.add(ApprovalModel(approvalMenuList[i].itemMenu,
                approvalMenuList[i].imgItemMenu,
                approvalMenuList[i].qtyApproval,
                approvalMenuList[i].itemMenuContent))
        }
        approvalAdapter.notifyDataSetChanged()
        approvalBinding.viewModel?.isVisibleApprovalUI?.set(false)
    }

    override fun onAlertApproval(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_APPROVAL_NO_CONNECTION -> MessageUtils.alertDialogDismiss(alertMessage, alertMessage, requireContext())
        }
    }

    companion object{
        const val ALERT_APPROVAL_NO_CONNECTION = 1
    }
}