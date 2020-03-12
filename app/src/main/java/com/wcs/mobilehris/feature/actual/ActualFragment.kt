package com.wcs.mobilehris.feature.actual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.databinding.FragmentActualBinding
import com.wcs.mobilehris.feature.plan.ContentTaskModel
import com.wcs.mobilehris.feature.plan.CustomTaskAdapter
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

class ActualFragment : Fragment(), ActualInterface {
    private lateinit var actualFragmentBinding : FragmentActualBinding
    private var arrActualList = ArrayList<ContentTaskModel>()
    private lateinit var actualAdapter: CustomTaskAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        actualFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_actual, container, false)
        actualFragmentBinding.viewModel = ActualViewModel(requireContext(), this, ApiRepo())
        return actualFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actualFragmentBinding.rcActual.layoutManager = LinearLayoutManager(requireContext())
        actualFragmentBinding.rcActual.setHasFixedSize(true)
        actualAdapter = CustomTaskAdapter(requireContext(), arrActualList)
        actualFragmentBinding.rcActual.adapter = actualAdapter
        actualFragmentBinding.viewModel?.initActual(ConstantObject.vLoadWithProgressBar)
        actualFragmentBinding.swActual.setOnRefreshListener {
            actualFragmentBinding.viewModel?.initActual(ConstantObject.vLoadWithoutProgressBar)
        }
    }

    override fun onDisplayList(actualList: List<ContentTaskModel>, typeLoading: Int) {
        arrActualList.clear()
        arrActualList.addAll(actualList)

        actualAdapter.notifyDataSetChanged()
        hideUI(ConstantObject.vGlobalUI)
        showUI(ConstantObject.vRecylerViewUI)

        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> hideUI(ConstantObject.vProgresBarUI)
            else -> onHideSwipeRefresh()
        }
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(requireContext(), message,ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(requireContext(), message,ConstantObject.vToastInfo)
        }
    }

    override fun onAlertActual(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_ACTUAL_NO_CONNECTION -> {MessageUtils.alertDialogDismiss(alertMessage, alertTitle, requireContext())}
        }
    }

    override fun onHideSwipeRefresh() {
        actualFragmentBinding.swActual.isRefreshing = false
    }

    override fun hideUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> actualFragmentBinding.pbActual.visibility = View.GONE
            ConstantObject.vRecylerViewUI -> actualFragmentBinding.rcActual.visibility = View.GONE
            else -> actualFragmentBinding.tvActualEmpty.visibility = View.GONE
        }
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> actualFragmentBinding.pbActual.visibility = View.VISIBLE
            ConstantObject.vRecylerViewUI -> actualFragmentBinding.rcActual.visibility = View.VISIBLE
            else -> actualFragmentBinding.tvActualEmpty.visibility = View.VISIBLE
        }
    }

    companion object{
        const val ALERT_ACTUAL_NO_CONNECTION = 1
    }
}
