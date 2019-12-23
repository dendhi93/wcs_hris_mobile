package com.wcs.mobilehris.feature.preparation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivitySplashBinding
import com.wcs.mobilehris.feature.login.LoginActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import com.wcs.mobilehris.utilinterface.DialogInterface

class SplashActivity : AppCompatActivity(), SplashInterface, DialogInterface {
    private lateinit var bindingSplash : ActivitySplashBinding
    private var keyDialogActive = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSplash = DataBindingUtil.setContentView<ActivitySplashBinding>(this,R.layout.activity_splash)
        bindingSplash.viewModel = SplashViewModel(this, this)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
        bindingSplash.viewModel?.processDownload()
        hideUI(ConstantObject.vButtonUI)
    }

    override fun onNegativeClick(o: Any) {}

    override fun onPositiveClick(o: Any) {
//         when(keyDialogActive){
//             DIALOG_NO_INTERNET -> finish()
//         }
    }

    override fun onErrorMessage(message: String, messageType: Int) {}

    override fun onAlertSplash(alertMessage: String, alertTitle: String, intTypeActionAlert : Int) {
        when(intTypeActionAlert) {
            DIALOG_NO_INTERNET -> {
                keyDialogActive = DIALOG_NO_INTERNET
                MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)
            }
        }
    }

    override fun successSplash() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        hideUI(ConstantObject.vProgresBarUI)
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vButtonUI -> bindingSplash.btnSplashRetry.visibility = View.VISIBLE
            ConstantObject.vProgresBarUI -> bindingSplash.pbSplash.visibility = View.VISIBLE
        }
    }

    override fun hideUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vButtonUI -> bindingSplash.btnSplashRetry.visibility = View.GONE
            ConstantObject.vProgresBarUI -> bindingSplash.pbSplash.visibility = View.GONE
        }
    }

    companion object{
        const val DIALOG_NO_INTERNET = 1
    }
}
