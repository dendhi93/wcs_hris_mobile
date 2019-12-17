package com.wcs.mobilehris.feature.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wcs.mobilehris.feature.actual.ActualFragment
import com.wcs.mobilehris.feature.completed.CompletedFragment
import com.wcs.mobilehris.feature.plan.PlanFragment
import com.wcs.mobilehris.utils.ConstantObject

class ViewPagerActivityAdapter(fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    override fun getItem(position: Int): Fragment {
        return when(position) {
            ConstantObject.vPlanFragment -> PlanFragment.newInstance(ConstantObject.vStTitlePlanFragment)
            ConstantObject.vActualFragment -> ActualFragment.newInstance(ConstantObject.vStTitleActualFragment)
            else -> CompletedFragment.newInstance(ConstantObject.vStTitleCompletedFragment)
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Plan"
            1 -> "Actual"
            else -> "Completed"
        }
    }
}