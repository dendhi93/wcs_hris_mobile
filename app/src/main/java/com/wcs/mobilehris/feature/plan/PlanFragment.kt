package com.wcs.mobilehris.feature.plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wcs.mobilehris.R
import com.wcs.mobilehris.utils.ConstantObject

class PlanFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }
}
