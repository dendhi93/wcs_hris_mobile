package com.wcs.mobilehris.feature.request

import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.util.ConstantObject

class RequestViewModel(private val _requestInterface: RequestInterface) :ViewModel(){

    fun initMenu(){
        val listMenu = mutableListOf<RequestModel>()
        var _requestModel = RequestModel(ConstantObject.travelMenu, R.mipmap.ic_train)
        listMenu.add(_requestModel)
        _requestModel = RequestModel(ConstantObject.leaveMenu, R.mipmap.ic_edit_user)
        listMenu.add(_requestModel)
        _requestModel = RequestModel(ConstantObject.benefitMenu, R.mipmap.ic_benefit)
        listMenu.add(_requestModel)
        _requestModel = RequestModel(ConstantObject.travelClaimMenu, R.mipmap.ic_edit_pen)
        listMenu.add(_requestModel)

        _requestInterface.loadMenu(listMenu)
    }
}