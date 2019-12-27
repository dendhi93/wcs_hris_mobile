package com.wcs.mobilehris.feature.activity

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentActivityBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils


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

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_activity_create_task, menu);
//
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.mnu_activity_create_task -> {
//                MessageUtils.toastMessage(requireContext(), "Test", ConstantObject.vToastInfo)
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}
