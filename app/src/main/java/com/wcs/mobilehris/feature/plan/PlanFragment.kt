package com.wcs.mobilehris.feature.plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentPlanBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils


class PlanFragment : Fragment(), PlanInterface {
    private lateinit var planFragmentBinding : FragmentPlanBinding
    private var arrPlanList = ArrayList<ContentTaskModel>()
    private lateinit var planAdapter: CustomTaskAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        planFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan, container, false)
        planFragmentBinding.viewModel = PlanViewModel(requireContext(), this)
        return planFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        planFragmentBinding.rcPlan.layoutManager = LinearLayoutManager(requireContext())
        planFragmentBinding.rcPlan.setHasFixedSize(true)
        planAdapter = CustomTaskAdapter(requireContext(), arrPlanList)
        planFragmentBinding.rcPlan.adapter = planAdapter
        planFragmentBinding.viewModel?.initPlan(ConstantObject.loadWithProgressBar)
        planFragmentBinding.swPlan.setOnRefreshListener {
            planFragmentBinding.viewModel?.initPlan(ConstantObject.loadWithoutProgressBar)
        }
    }

    override fun onLoadList(planList: List<ContentTaskModel>, typeLoading : Int) {
        arrPlanList.clear()
        arrPlanList.addAll(planList)
        planAdapter.notifyDataSetChanged()

        hideUI(ConstantObject.vGlobalUI)
        showUI(ConstantObject.vRecylerViewUI)

        when(typeLoading){
            ConstantObject.loadWithProgressBar -> hideUI(ConstantObject.vProgresBarUI)
            else -> onHideSwipeRefresh()
        }
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(requireContext(), message,ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(requireContext(), message,ConstantObject.vToastInfo)
        }
    }

    override fun onAlertPlan(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_PLAN_NO_CONNECTION -> {MessageUtils.alertDialogDismiss(alertMessage, alertTitle, requireContext())}
        }
    }

    override fun onHideSwipeRefresh() { planFragmentBinding.swPlan.isRefreshing = false }

    override fun hideUI(typeUI: Int) {
       when(typeUI){
           ConstantObject.vProgresBarUI -> planFragmentBinding.pbPlan.visibility = View.INVISIBLE
           ConstantObject.vRecylerViewUI -> planFragmentBinding.rcPlan.visibility = View.GONE
           ConstantObject.vGlobalUI -> planFragmentBinding.tvPlanEmpty.visibility = View.GONE
       }
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> planFragmentBinding.pbPlan.visibility = View.VISIBLE
            ConstantObject.vRecylerViewUI -> planFragmentBinding.rcPlan.visibility = View.VISIBLE
            ConstantObject.vGlobalUI -> planFragmentBinding.tvPlanEmpty.visibility = View.VISIBLE
        }
    }

    companion object{
        const val ALERT_PLAN_NO_CONNECTION = 1
    }
}
