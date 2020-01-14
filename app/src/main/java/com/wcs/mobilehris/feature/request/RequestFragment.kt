package com.wcs.mobilehris.feature.request

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentRequestBinding

class RequestFragment : Fragment(), RequestInterface {
    private lateinit var requestBinding : FragmentRequestBinding
    private var arrList = ArrayList<RequestModel>()
    private lateinit var requestAdapter : CustomRequestAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        requestBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_request, container, false)
        requestBinding.viewModel = RequestViewModel(this)
        return requestBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestBinding.rcRequest.layoutManager = LinearLayoutManager(requireContext())
        requestBinding.rcRequest.setHasFixedSize(true)
        requestAdapter = CustomRequestAdapter(requireContext(), arrList)
        requestBinding.rcRequest.adapter = requestAdapter
        requestBinding.viewModel?.initMenu()
    }

    override fun loadMenu(requestMenuList: List<RequestModel>) {
        arrList.clear()
        arrList.addAll(requestMenuList)

//        for(i in requestMenuList.indices){
//            arrList.add(RequestModel(requestMenuList[i].itemMenu, requestMenuList[i].imgItemMenu))
//        }
        requestAdapter.notifyDataSetChanged()
    }
}
