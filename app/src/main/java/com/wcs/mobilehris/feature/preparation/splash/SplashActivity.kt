package com.wcs.mobilehris.feature.preparation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.databinding.ActivitySplashBinding
import com.wcs.mobilehris.feature.login.LoginActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.DateTimeUtils
import com.wcs.mobilehris.util.MessageUtils
import com.wcs.mobilehris.utilinterface.DialogInterface

class SplashActivity : AppCompatActivity(), SplashInterface, DialogInterface {
    private lateinit var bindingSplash : ActivitySplashBinding
    private var keyDialogActive = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSplash = DataBindingUtil.setContentView(this,R.layout.activity_splash)
        bindingSplash.viewModel = SplashViewModel(this, this, ApiRepo())
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
        bindingSplash.viewModel?.isBtnVisible?.set(false)
    }

    override fun onResume() {
        super.onResume()
        bindingSplash.viewModel?.validateUpdateMaster()
    }

    override fun onNegativeClick(o: Any) {}

    override fun onPositiveClick(o: Any) {
         when(keyDialogActive){
             DIALOG_NO_INTERNET -> finish()
         }
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
        }
    }

    override fun onAlertSplash(alertMessage: String, alertTitle: String, intTypeActionAlert : Int) {
        when(intTypeActionAlert) {
            DIALOG_NO_INTERNET -> {
                keyDialogActive = DIALOG_NO_INTERNET
                MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)
            }
        }
    }

    override fun successSplash() {
        bindingSplash.viewModel?.isPrgBarVisible?.set(false)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    companion object{
        const val DIALOG_NO_INTERNET = 1
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }
}
