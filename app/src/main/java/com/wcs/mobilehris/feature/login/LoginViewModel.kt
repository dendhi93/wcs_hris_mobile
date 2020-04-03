package com.wcs.mobilehris.feature.login

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.Preference
import org.json.JSONObject

class LoginViewModel(private var _context : Context, private var _loginInterface : LoginInterface, private var apiRepo: ApiRepo) : ViewModel() {

    val stUserId = ObservableField("")
    val stPassword = ObservableField("")
    val stVersion = ObservableField("")
    val isVisibleProgress = ObservableField(false)
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
            preference.getFirebaseToken() == "null" -> _loginInterface.onErrorMessage("firebase token is null, please exit and login again", ConstantObject.vToastError)
            else -> processLogin()
        }
    }

    fun intentForgotPass(){ _loginInterface.onErrorMessage("Under Maintenance", ConstantObject.vToastInfo) }

    private fun processLogin(){
        isVisibleProgress.set(true)
        apiRepo.getLogin(initjObjLogin(),
            _context,
            object : ApiRepo.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        val objectResponse = it.getString(ConstantObject.vResponseData)
                        Log.d("###", "json user $objectResponse")
                        val jObjData = JSONObject(objectResponse)
                        Log.d("###", "data $objectResponse")
                        preference.saveUn(stUserId.get().toString().trim(),
                            jObjData.getString("FULL_NAME"),
                            jObjData.getString("PHONE_NUMBER"),
                            jObjData.getString("EMAIL"),
                            jObjData.getString("TOKEN"))
                        _loginInterface.onSuccessLogin()
                    }
                    isVisibleProgress.set(false)
                }

                override fun onDataError(error: String?) {
                    Log.d("###", " err $error")
                    _loginInterface.onErrorMessage(error.toString().trim(), ConstantObject.vToastInfo)
                    isVisibleProgress.set(false)
                }
            })
    }

    private fun initjObjValidateToken() : JSONObject{
        val reqTokenParam  = JSONObject()
        reqTokenParam.put("NIK", preference.getUn().trim())
        reqTokenParam.put("TOKEN", preference.getApiToken().trim())

        return reqTokenParam
    }

    private fun initjObjLogin() : JSONObject{
        val reqLoginParam  = JSONObject()
        reqLoginParam.put("USERID", stUserId.get().toString().trim())
        reqLoginParam.put("PASSWORD", stPassword.get().toString().trim())
        reqLoginParam.put("DEVICEID", preference.getFirebaseToken().trim())

        return reqLoginParam
    }

    fun validateLogin(){
        if(preference.getUn().isNotEmpty()){
            Log.d("###",""+preference.getApiToken().trim())
            isVisibleProgress.set(true)
            apiRepo.validateToken(initjObjValidateToken(), _context, object : ApiRepo.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        val responseValidate = it.getString(ConstantObject.vResponseMessage)
                        Log.d("###", "Message $responseValidate")
                        Log.d("###", "data "+it.getString(ConstantObject.vResponseData))
                        if(responseValidate != "Not Verified"){
                            _loginInterface.onSuccessLogin()
                        }
                    }
                }

                override fun onDataError(error: String?) {
                    isVisibleProgress.set(false)
                    _loginInterface.onErrorMessage("err Login " +error.toString(), ConstantObject.vToastError)
                }
            })
        }
    }
}