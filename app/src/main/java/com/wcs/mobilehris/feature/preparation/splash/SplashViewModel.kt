package com.wcs.mobilehris.feature.preparation.splash

import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.utils.ConstantObject

class SplashViewModel(private var _context : Context, private var _splashInterface: SplashInterface) : ViewModel() {

    val stErrDownload = ObservableField<String>("")
//    private val TAG = "SplashViewModel"

    fun retryDownload(){
        when{
            !ConnectionObject.isNetworkAvailable(_context) -> _splashInterface.onAlertSplash(_context.getString(R.string.alert_no_connection), ConstantObject.vAlertDialogNoConnection,SplashActivity.DIALOG_NO_INTERNET)
            else -> processDownload()
        }
    }

    fun processDownload(){
        Handler().postDelayed({
            _splashInterface.showUI(ConstantObject.vProgresBarUI)
            _splashInterface.successSplash()
        }, 2000)
    }

}