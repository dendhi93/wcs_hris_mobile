package com.wcs.mobilehris.feature.login

import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.Preference

class LoginViewModel(private var _context : Context, private var _loginInterface : LoginInterface) : ViewModel() {

    val stUserId = ObservableField<String>("")
    val stPassword = ObservableField<String>("")
    val stVersion = ObservableField<String>("")
    val isVisibleProgress = ObservableField<Boolean>(false)
    private var preference: Preference = Preference(_context)

    fun getVersion(){
        val manager = _context.packageManager
        val info = manager.getPackageInfo(_context.packageName, PackageManager.GET_ACTIVITIES)
        stVersion.set("Version ${info.versionName}")
    }

    fun getLogin(){
        when {
            stUserId.get().isNullOrEmpty() -> _loginInterface.onErrorMessage("Please fill userid", ConstantObject.vSnackBarNoButton)
            stPassword.get().isNullOrEmpty() -> _loginInterface.onErrorMessage("Please fill password", ConstantObject.vSnackBarNoButton)
            !ConnectionObject.isNetworkAvailable(_context) -> {_loginInterface.onAlertLogin(_context.getString(R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, LoginActivity.DIALOG_NO_INTERNET)}
            else -> processLogin()
        }
    }

    fun intentForgotPass(){
        _loginInterface.onErrorMessage("Under Maintenance", ConstantObject.vToastInfo)
    }

    private fun processLogin(){
        isVisibleProgress.set(true)
        _loginInterface.disableUI(ConstantObject.vButtonUI)
        Handler().postDelayed({
            preference.saveUn(stUserId.get().toString().trim(), "Untitled", "0878900679", "untitled-id@id.wilmar-intl.com")
            _loginInterface.onSuccessLogin()
        }, 2000)
    }

    fun validateLogin(){
        if(preference.getUn().isNotEmpty()){
            _loginInterface.onSuccessLogin()
        }
    }

}