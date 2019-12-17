package com.wcs.mobilehris.feature.actual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wcs.mobilehris.R
import com.wcs.mobilehris.utils.ConstantObject

class ActualFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_actual, container, false)
    }

    companion object {
        fun newInstance(text: String): ActualFragment {
            val fragment = ActualFragment()
            val bundle = Bundle()
            bundle.putString(ConstantObject.vStTitleActualFragment, text)
            fragment.arguments = bundle
            return fragment
        }
    }
}
