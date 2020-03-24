package com.wcs.mobilehris.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.wcs.mobilehris.R
import com.wcs.mobilehris.utilinterface.DialogInterface
import es.dmoral.toasty.Toasty

object MessageUtils {

    fun toastMessage(context : Context, message: String, type : Int) {
        lateinit var toast: Toast
        when (type) {
            ConstantObject.vToastSuccess -> toast = Toasty.success(context, message, Toast.LENGTH_LONG)
            ConstantObject.vToastError -> toast = Toasty.error(context, message, Toast.LENGTH_LONG)
            ConstantObject.vToastInfo -> toast = Toasty.info(context, message, Toast.LENGTH_LONG)
        }
        toast.setGravity(Gravity.CENTER, 0,0)
        toast.show()
    }

    fun snackBarMessage(snackMessage: String, activity: Activity, action: Int) {
        val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
        when(action) {
            ConstantObject.vSnackBarWithButton -> {
                val snackBar = Snackbar.make(rootView, snackMessage, Snackbar.LENGTH_INDEFINITE)
                snackBar.setBackgroundTint(Color.YELLOW)
                snackBar.setActionTextColor(Color.BLACK)
                snackBar.setTextColor(Color.BLACK)
                snackBar.setAction("OK") {
                    snackBar.dismiss()
                }
                snackBar.show()
            }
            else -> {
                val snackBar = Snackbar.make(rootView, snackMessage, Snackbar.LENGTH_LONG)
                snackBar.setBackgroundTint(Color.YELLOW)
                snackBar.setActionTextColor(Color.BLACK)
                snackBar.setTextColor(Color.BLACK)
                snackBar.show()
            }
        }
    }

    fun alertDialogDismiss(alertMessage : String, alertTitle : String,context: Context){
        AlertDialog.Builder(context).setTitle(alertTitle)
            .setMessage(alertMessage)
            .setPositiveButton(android.R.string.ok){
                    dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    fun alertDialogOkCancel(alertMessage : String, alertTitle : String,context: Context, dialogInterface: DialogInterface){
        MaterialAlertDialogBuilder(context, R.style.customAlertDialogStyle)
            .setTitle(alertTitle)
            .setMessage(alertMessage)
            .setPositiveButton(android.R.string.ok) {
                dialog, which ->
                dialog.dismiss()
                dialogInterface.onPositiveClick(Any())
            }
            .setNegativeButton(android.R.string.cancel) {
                dialog, which ->
                dialog.dismiss()
                dialogInterface.onNegativeClick(Any())
            }
            .setCancelable(false)
            .create()
            .show()
    }

}
