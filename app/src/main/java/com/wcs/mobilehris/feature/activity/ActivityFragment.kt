package com.wcs.mobilehris.feature.activity

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentActivityBinding

class ActivityFragment : Fragment() {
    private lateinit var activityBinding : FragmentActivityBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity, container, false)
        return activityBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val viewPagerAdapter = ViewPagerActivityAdapter(childFragmentManager)
        activityBinding.vpActivity.adapter = viewPagerAdapter
        activityBinding.tlActivity.setupWithViewPager(activityBinding.vpActivity)
        activityBinding.tlActivity.setTabTextColors(Color.parseColor("#BDBDBD"), Color.parseColor("#000000"))
    }

}
