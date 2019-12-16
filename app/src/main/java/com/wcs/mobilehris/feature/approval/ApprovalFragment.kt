package com.wcs.mobilehris.feature.approval

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentApprovalBinding
import com.wcs.mobilehris.feature.request.CustomRequestAdapter
import com.wcs.mobilehris.utils.ConstantObject
import com.wcs.mobilehris.utilsinterface.ActionInterface

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
        hideUI(ConstantObject.vProgresBarUI)
        showUI(ConstantObject.vGlobalUI)
    }

    override fun hideUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> approvalBinding.pbApproval.visibility = View.GONE
            ConstantObject.vGlobalUI -> approvalBinding.rcApproval.visibility = View.GONE
        }
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> approvalBinding.pbApproval.visibility = View.VISIBLE
            ConstantObject.vGlobalUI -> approvalBinding.rcApproval.visibility = View.VISIBLE
        }
    }
}