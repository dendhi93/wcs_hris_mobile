package com.wcs.mobilehris.feature.completed

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker

import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.databinding.FragmentCompletedBinding
import com.wcs.mobilehris.feature.plan.ContentTaskModel
import com.wcs.mobilehris.feature.plan.CustomTaskAdapter
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

class CompletedFragment : Fragment(), CompletedInterface, View.OnClickListener {
    private lateinit var fragmentCompletedBinding: FragmentCompletedBinding
    private var arrCompletedList = ArrayList<ContentTaskModel>()
    private lateinit var completedAdapter: CustomTaskAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentCompletedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed, container, false)
        fragmentCompletedBinding.viewModel = CompletedViewModel(requireContext(), this, ApiRepo())
        return fragmentCompletedBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCompletedBinding.rcCompleted.layoutManager = LinearLayoutManager(requireContext())
        fragmentCompletedBinding.rcCompleted.setHasFixedSize(true)
        completedAdapter = CustomTaskAdapter(requireContext(), arrCompletedList)
        fragmentCompletedBinding.rcCompleted.adapter = completedAdapter
        fragmentCompletedBinding.viewModel?.initCompleted(ConstantObject.vLoadWithProgressBar)
        fragmentCompletedBinding.swCompleted.setOnRefreshListener {
            fragmentCompletedBinding.viewModel?.initCompleted(ConstantObject.vLoadWithoutProgressBar)
        }
        fragmentCompletedBinding.btnDateForCompleted.setOnClickListener(this)
    }

    override fun onDisplayCompletedList(completedList: List<ContentTaskModel>, typeLoading: Int) {
        arrCompletedList.clear()
        arrCompletedList.addAll(completedList)
        completedAdapter.notifyDataSetChanged()

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

    override fun onAlertCompleted(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_COMPLETED_NO_CONNECTION -> { MessageUtils.alertDialogDismiss(alertMessage, alertTitle, requireContext())}
        }
    }

    override fun onHideSwipeRefresh() {
        fragmentCompletedBinding.swCompleted.isRefreshing = false
    }


    override fun hideUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> fragmentCompletedBinding.pbCompleted.visibility = View.GONE
            ConstantObject.vRecylerViewUI -> fragmentCompletedBinding.rcCompleted.visibility = View.GONE
            else -> fragmentCompletedBinding.tvCompletedEmpty.visibility = View.GONE
        }
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> fragmentCompletedBinding.pbCompleted.visibility = View.VISIBLE
            ConstantObject.vRecylerViewUI -> fragmentCompletedBinding.rcCompleted.visibility = View.VISIBLE
            else -> fragmentCompletedBinding.tvCompletedEmpty.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_date_for_completed -> {
                    val builder: MaterialDatePicker.Builder<Pair<Long, Long>> = MaterialDatePicker.Builder.dateRangePicker()
                    val constraintsBuilder = CalendarConstraints.Builder()

                    try {
                        builder.setCalendarConstraints(constraintsBuilder.build())
                        val picker: MaterialDatePicker<*> = builder.build()
                        getDateRange(picker)
                        picker.show(this.activity!!.supportFragmentManager, picker.toString())
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun getDateRange(materialCalendarPicker: MaterialDatePicker<out Any>) {
        materialCalendarPicker.addOnPositiveButtonClickListener { selection: Any? ->
            Log.e("DateRangeText",materialCalendarPicker.headerText)
        }
        materialCalendarPicker.addOnNegativeButtonClickListener { dialog: View? ->

        }
        materialCalendarPicker.addOnCancelListener { dialog: DialogInterface? ->

        }
    }

    companion object{
        const val ALERT_COMPLETED_NO_CONNECTION = 1
    }

}