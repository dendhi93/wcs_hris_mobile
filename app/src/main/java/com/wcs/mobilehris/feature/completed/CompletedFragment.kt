package com.wcs.mobilehris.feature.completed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentCompletedBinding
import com.wcs.mobilehris.feature.plan.ContentTaskModel

class CompletedFragment : Fragment(), CompletedInterface {
    private lateinit var fragmentCompletedBinding: FragmentCompletedBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentCompletedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed, container, false)
        return fragmentCompletedBinding.root
    }

    override fun onCompletedList(completedList: List<ContentTaskModel>, typeLoading: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAlertCompleted(
        alertMessage: String,
        alertTitle: String,
        intTypeActionAlert: Int
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideUI(typeUI: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showUI(typeUI: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}