package com.wcs.mobilehris.feature.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wcs.mobilehris.feature.actual.ActualFragment
import com.wcs.mobilehris.feature.completed.CompletedFragment
import com.wcs.mobilehris.feature.plan.PlanFragment

class ViewPagerActivityAdapter(fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val itemPages =
        listOf(PlanFragment(),
        ActualFragment(),
        CompletedFragment())

    override fun getItem(position: Int): Fragment {
        return itemPages[position]
    }

    override fun getCount(): Int { return itemPages.size }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Plan"
            1 -> "Actual"
            else -> "Completed"
        }
    }
}