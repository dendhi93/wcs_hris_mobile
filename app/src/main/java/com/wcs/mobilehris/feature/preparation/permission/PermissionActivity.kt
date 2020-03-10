package com.wcs.mobilehris.feature.preparation.permission

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityPermissionBinding
import com.wcs.mobilehris.feature.login.LoginActivity
import com.wcs.mobilehris.feature.preparation.splash.SplashActivity

class PermissionActivity : AppCompatActivity() {

    private lateinit var bindingPermission : ActivityPermissionBinding
    private var allPermissionsListener: MultiplePermissionsListener? = null
    private var errorListener: PermissionRequestErrorListener? = null
    private lateinit var rootView: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingPermission = DataBindingUtil.setContentView(this,
            R.layout.activity_permission
        )
        rootView = findViewById(android.R.id.content)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
        createPermissionListeners()
        checkPermission()
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        } else if (
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }
    }

    fun onAllPermissionsButtonClicked(view : View) {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(allPermissionsListener)
            .withErrorListener(errorListener)
            .check()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun showPermissionRationale(token: PermissionToken) {
        AlertDialog.Builder(this).setTitle(R.string.permission_rationale_title)
            .setMessage(R.string.permission_rationale_message)
            .setNegativeButton(android.R.string.cancel) { dialog, _->
                dialog.dismiss()
                token.cancelPermissionRequest()
            }
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                token.continuePermissionRequest()
            }
            .setOnDismissListener { token.cancelPermissionRequest() }
            .show()
    }

    private fun createPermissionListeners() {
        val feedbackViewMultiplePermissionListener = NewMultiplePermission()
        allPermissionsListener = CompositeMultiplePermissionsListener(feedbackViewMultiplePermissionListener,
            SnackbarOnAnyDeniedMultiplePermissionsListener.Builder.with(rootView,
                R.string.all_permissions_denied_feedback
            )
                .withOpenSettingsButton(R.string.permission_rationale_settings_button_text)
                .build())
        errorListener = PermissionRequestErrorListener { error -> Log.e("Dexter", "err " + error.toString()) }
    }

    private inner class NewMultiplePermission internal constructor() : MultiplePermissionsListener {

        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            for (response in report.grantedPermissionResponses) {
                if (report.areAllPermissionsGranted()) {
                    checkPermission()
                }
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
            showPermissionRationale(token)
        }
    }

}
