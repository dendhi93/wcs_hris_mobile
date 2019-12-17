package com.wcs.mobilehris.feature.completed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wcs.mobilehris.R
import com.wcs.mobilehris.utils.ConstantObject

class CompletedFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_completed, container, false)
    }

    companion object {
        fun newInstance(text: String): CompletedFragment {
            val fragment = CompletedFragment()
            val bundle = Bundle()
            bundle.putString(ConstantObject.vStTitleCompletedFragment, text)
            fragment.arguments = bundle
            return fragment
        }
    }
}