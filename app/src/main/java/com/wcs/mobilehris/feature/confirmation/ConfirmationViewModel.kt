package com.wcs.mobilehris.feature.confirmation

import android.content.Context
import android.content.Intent
import android.os.Handler
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.util.ConstantObject

class ConfirmationViewModel(private val context : Context, private val confirmationInterface: ConfirmationInterface){

    fun confirmationInitMenu(typeLoading : Int){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> confirmationInterface.onAlertConf(context.getString(
                R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, ConfirmationFragment.ALERT_CONF_NO_CONNECTION)
            else -> getDataConfirmation(typeLoading)
        }
    }

    private fun getDataConfirmation(typeLoading : Int){
        confirmationInterface.hideUI(ConstantObject.vRecylerViewUI)
        confirmationInterface.showUI(ConstantObject.vGlobalUI)
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> { confirmationInterface.showUI(ConstantObject.vProgresBarUI) }
        }
        val listConf = mutableListOf<ConfirmationModel>()
        val confModel = ConfirmationModel(ConstantObject.extra_fromIntentConfirmTravel,
            "1",
            R.mipmap.ic_train,
            "F-0014-017 MILLS MOBILITY APPLICATION")
        listConf.add(confModel)
        when(listConf.size){
            0 -> {
                    when(typeLoading){
                        ConstantObject.vLoadWithProgressBar -> confirmationInterface.hideUI(ConstantObject.vProgresBarUI)
                        else -> confirmationInterface.hideConfirmationSwipe()
                    }
                confirmationInterface.onErrorMessage(context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
            }
            else -> {
                Handler().postDelayed({
                    confirmationInterface.onLoadConfirmationMenu(listConf, typeLoading)
                },2000)
            }
        }
    }
}