package com.wcs.mobilehris.feature.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentDashboardBinding
import com.wcs.mobilehris.utils.ConstantObject
import com.wcs.mobilehris.utils.MessageUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class DashboardFragment : Fragment(), DashboardInterface {
    private lateinit var dashboardBinding : FragmentDashboardBinding
    private var arrDashList = ArrayList<DashboardModel>()
    private lateinit var dashAdapter : CustomDashboardAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        dashboardBinding.viewModel = DashboardViewModel(requireContext(), this)
        return  dashboardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardBinding.rcDashboard.layoutManager = LinearLayoutManager(requireContext())
        dashboardBinding.rcDashboard.setHasFixedSize(true)
        dashAdapter = CustomDashboardAdapter(requireContext(), arrDashList)
        dashboardBinding.rcDashboard.adapter = dashAdapter

        dashboardBinding.viewModel?.initDataDashboard()
        dashboardBinding.swDashboard.setOnRefreshListener{
            dashboardBinding.viewModel?.onInitDashboardMenu()
            dashboardBinding.swDashboard.isRefreshing = false
        }
    }

    override fun onLoadList(vListDash: List<DashboardModel>, typeLoading : Int) {
        arrDashList.clear()
        for(i in vListDash.indices){
            arrDashList.add(
                DashboardModel(vListDash[i].vtitleContent,
                vListDash[i].vcontentDashboard)
            )
        }
        dashAdapter.notifyDataSetChanged()
        when(typeLoading){
            LOAD_WITH_PROGRESBAR -> {
                hideUI(ConstantObject.vProgresBarUI)
                showUI(ConstantObject.vGlobalUI)
            }
        }
        hideUI(TEXTVIEW_UI)
        showUI(ConstantObject.vRecylerViewUI)
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(requireContext(), message,
                ConstantObject.vToastError)
        }
    }

    override fun onAlertDash(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_DASH_NO_CONNECTION -> {
                MessageUtils.alertDialogDismiss(alertMessage, alertTitle, requireContext())}
        }
    }

    override fun hideUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vRecylerViewUI -> dashboardBinding.rcDashboard.visibility = View.GONE
            ConstantObject.vProgresBarUI -> dashboardBinding.pbDashboard.visibility = View.GONE
            TEXTVIEW_UI -> dashboardBinding.tvDashboardEmpty.visibility = View.GONE
            else -> {
                dashboardBinding.btnDashboardLate.visibility = View.GONE
                dashboardBinding.btnDashboardLeave.visibility = View.GONE
            }
        }
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vRecylerViewUI -> dashboardBinding.rcDashboard.visibility = View.VISIBLE
            ConstantObject.vProgresBarUI -> dashboardBinding.pbDashboard.visibility = View.VISIBLE
            TEXTVIEW_UI -> dashboardBinding.tvDashboardEmpty.visibility = View.VISIBLE
            else -> {
                dashboardBinding.btnDashboardLate.visibility = View.VISIBLE
                dashboardBinding.btnDashboardLeave.visibility = View.VISIBLE
            }
        }
    }

    companion object{
        const val ALERT_DASH_NO_CONNECTION = 1
        const val LOAD_WITH_PROGRESBAR = 2
        const val LOAD_WITHOUT_PROGRESBAR = 3
        const val TEXTVIEW_UI = 4
    }
}
