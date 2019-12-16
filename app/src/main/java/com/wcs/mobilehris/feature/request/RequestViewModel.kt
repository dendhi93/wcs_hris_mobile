package com.wcs.mobilehris.feature.request

import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R

class RequestViewModel(private val _requestInterface: RequestInterface) :ViewModel(){

    fun initMenu(){
        val listMenu = mutableListOf<RequestModel>()
        var _requestModel = RequestModel("Travel", R.mipmap.ic_train)
        listMenu.add(_requestModel)
        _requestModel = RequestModel("Leave", R.mipmap.ic_edit_user)
        listMenu.add(_requestModel)
        _requestModel = RequestModel("Benefit", R.mipmap.ic_benefit)
        listMenu.add(_requestModel)
        _requestModel = RequestModel("Travel Claim", R.mipmap.ic_edit_pen)
        listMenu.add(_requestModel)

        _requestInterface.loadMenu(listMenu)
    }
}