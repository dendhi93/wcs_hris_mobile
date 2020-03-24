package com.wcs.mobilehris.feature.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wcs.mobilehris.feature.actual.ActualFragment
import com.wcs.mobilehris.feature.completed.CompletedFragment
import com.wcs.mobilehris.feature.plan.PlanFragment

class ViewPagerActivityAdapter(fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> PlanFragment()
            1 -> ActualFragment()
            else -> CompletedFragment()
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "PLAN"
            1 -> "ONGOING"
            else -> "COMPLETED"
        }
    }
}
