package com.wcs.mobilehris.feature.confirmation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentConfirmationBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils


class ConfirmationFragment : Fragment(), ConfirmationInterface {
    private lateinit var fragmentConfirmationBinding : FragmentConfirmationBinding
    private var arrConfirmationList = ArrayList<ConfirmationModel>()
    private lateinit var confAdapter: CustomConfirmationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentConfirmationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_confirmation, container, false)
        fragmentConfirmationBinding.viewModel = ConfirmationViewModel(requireContext(), this)
        return fragmentConfirmationBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentConfirmationBinding.rcConfirmation.layoutManager = LinearLayoutManager(requireContext())
        fragmentConfirmationBinding.rcConfirmation.setHasFixedSize(true)
        confAdapter = CustomConfirmationAdapter(requireContext(), arrConfirmationList)
        fragmentConfirmationBinding.rcConfirmation.adapter = confAdapter
        fragmentConfirmationBinding.viewModel?.confirmationInitMenu(ConstantObject.vLoadWithProgressBar)
        fragmentConfirmationBinding.swConfirmation.setOnRefreshListener{
            fragmentConfirmationBinding.viewModel?.confirmationInitMenu(ConstantObject.vLoadWithoutProgressBar)
        }
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(requireContext(), message,
                ConstantObject.vToastError)
            else ->  MessageUtils.toastMessage(requireContext(), message, ConstantObject.vToastInfo)
        }
    }

    override fun onAlertConf(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_CONF_NO_CONNECTION -> MessageUtils.alertDialogDismiss(alertMessage, alertTitle, requireContext())}
    }

    override fun hideConfirmationSwipe() {
        fragmentConfirmationBinding.swConfirmation.isRefreshing = false
    }

    override fun onLoadConfirmationMenu(listConf: List<ConfirmationModel>, typeLoading : Int) {
        arrConfirmationList.clear()
        arrConfirmationList.addAll(listConf)
        showUI(ConstantObject.vRecylerViewUI)
        hideUI(ConstantObject.vGlobalUI)
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> hideUI(ConstantObject.vProgresBarUI)
            else -> hideConfirmationSwipe()
        }
    }

    override fun hideUI(typeUI: Int) {
       when(typeUI){
           ConstantObject.vRecylerViewUI -> fragmentConfirmationBinding.rcConfirmation.visibility = View.GONE
           ConstantObject.vProgresBarUI -> fragmentConfirmationBinding.pbConfirmation.visibility = View.GONE
           else -> fragmentConfirmationBinding.tvConfirmationEmpty.visibility = View.GONE
       }
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vRecylerViewUI -> fragmentConfirmationBinding.rcConfirmation.visibility = View.VISIBLE
            ConstantObject.vProgresBarUI -> fragmentConfirmationBinding.pbConfirmation.visibility = View.VISIBLE
            else -> fragmentConfirmationBinding.tvConfirmationEmpty.visibility = View.VISIBLE
        }
    }

    companion object{
        const val ALERT_CONF_NO_CONNECTION = 1
    }
}
