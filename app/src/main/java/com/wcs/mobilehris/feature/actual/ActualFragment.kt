package com.wcs.mobilehris.feature.actual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.wcs.mobilehris.R
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
        actualFragmentBinding.viewModel = ActualViewModel(requireContext(), this)
        return actualFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actualFragmentBinding.rcActual.layoutManager = LinearLayoutManager(requireContext())
        actualFragmentBinding.rcActual.setHasFixedSize(true)
        actualAdapter = CustomTaskAdapter(requireContext(), arrActualList)
        actualFragmentBinding.rcActual.adapter = actualAdapter
        actualFragmentBinding.viewModel?.initActual(LOAD_WITH_PROGRESSBAR)
        actualFragmentBinding.swActual.setOnRefreshListener {
            actualFragmentBinding.viewModel?.initActual(LOAD_WITHOUT_PROGRESSBAR)
        }
    }

    override fun onDisplayList(actualList: List<ContentTaskModel>, typeLoading: Int) {
        arrActualList.clear()
        for(i in actualList.indices){
            arrActualList.add(
                ContentTaskModel(actualList[i].taskType ,
                    actualList[i].userCreate,
                    actualList[i].createDate,
                    actualList[i].locationTask,
                    actualList[i].companyName,
                    actualList[i].beginTaskTime,
                    actualList[i].endTaskTime,
                    actualList[i].flagTask,
                    actualList[i].taskDate))
        }
        actualAdapter.notifyDataSetChanged()
        hideUI(ConstantObject.vGlobalUI)
        showUI(ConstantObject.vRecylerViewUI)
        actualFragmentBinding.swActual.isRefreshing = false

        when(typeLoading){
            LOAD_WITH_PROGRESSBAR -> hideUI(ConstantObject.vProgresBarUI)
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
        const val LOAD_WITH_PROGRESSBAR = 2
        const val LOAD_WITHOUT_PROGRESSBAR = 3
    }
}
