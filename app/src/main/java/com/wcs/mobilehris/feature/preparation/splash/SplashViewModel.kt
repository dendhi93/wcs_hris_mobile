package com.wcs.mobilehris.feature.preparation.splash

import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject

class SplashViewModel(private var _context : Context, private var _splashInterface: SplashInterface) : ViewModel() {

    val stErrDownload = ObservableField<String>("")
    val isPrgBarVisible = ObservableField<Boolean>(false)
    val isBtnVisible = ObservableField<Boolean>(false)

    fun retryDownload(){
        when{
            !ConnectionObject.isNetworkAvailable(_context) -> {
                _splashInterface.onAlertSplash(_context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection,SplashActivity.DIALOG_NO_INTERNET)
            }
            else -> processDownload()
        }
    }

    fun processDownload(){
        isPrgBarVisible.set(true)
        Handler().postDelayed({
            _splashInterface.successSplash()
        }, 2000)
    }

}