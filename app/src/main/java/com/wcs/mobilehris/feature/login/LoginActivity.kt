package com.wcs.mobilehris.feature.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.databinding.DataBindingUtil
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityLoginBinding
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.utils.ConstantObject
import com.wcs.mobilehris.utils.MessageUtils
import com.wcs.mobilehris.utilsinterface.DialogInterface

class LoginActivity : AppCompatActivity(), LoginInterface, DialogInterface {
    private lateinit var bindingLogin : ActivityLoginBinding
    private var keyDialogActive = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingLogin = DataBindingUtil.setContentView(this, R.layout.activity_login)
        bindingLogin.viewModel = LoginViewModel(this, this)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
        bindingLogin.viewModel?.getVersion()
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vSnackBarNoButton -> MessageUtils.snackBarMessage(message, this, ConstantObject.vSnackBarNoButton)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
        }
    }

    override fun onAlertLogin(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            DIALOG_NO_INTERNET ->{
                keyDialogActive = DIALOG_NO_INTERNET
                MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)
            }
        }
    }

    override fun onSuccessLogin() {
        hideUI(ConstantObject.vProgresBarUI)
        enableUI(ConstantObject.vButtonUI)
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_DASHBOARD)
        startActivity(intent)
        finish()
    }

    override fun onNegativeClick(o: Any) {}

    override fun onPositiveClick(o: Any) {}

    override fun onResume() {
        super.onResume()
        bindingLogin.viewModel?.validateLogin()
    }

    private var isDoubleTab = false
    override fun onBackPressed() {
        if(isDoubleTab){
            super.onBackPressed()
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
            return
        }
        this.isDoubleTab = true
        MessageUtils.toastMessage(this, "Tap again to exit", ConstantObject.vToastInfo)
        Handler().postDelayed({ isDoubleTab = false }, 2000)
    }

    override fun showUI(typeUI: Int) {
        when(typeUI) {
            ConstantObject.vButtonUI -> bindingLogin.btnLoginLogIn.visibility = View.VISIBLE
            ConstantObject.vProgresBarUI -> bindingLogin.pbLogin.visibility = View.VISIBLE
        }
    }

    override fun hideUI(typeUI: Int) {
        when(typeUI) {
            ConstantObject.vButtonUI -> bindingLogin.btnLoginLogIn.visibility = View.GONE
            ConstantObject.vProgresBarUI -> bindingLogin.pbLogin.visibility = View.GONE
        }
    }

    override fun disableUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vButtonUI ->bindingLogin.btnLoginLogIn.isEnabled = false
        }
    }

    override fun enableUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vButtonUI ->bindingLogin.btnLoginLogIn.isEnabled = true
        }
    }

    companion object{
        const val DIALOG_NO_INTERNET = 1
    }

}
