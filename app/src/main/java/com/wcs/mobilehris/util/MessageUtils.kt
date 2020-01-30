package com.wcs.mobilehris.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.wcs.mobilehris.utilinterface.DialogInterface
import es.dmoral.toasty.Toasty

object MessageUtils {

    fun toastMessage(context : Context, message: String, type : Int) {
        when (type) {
            ConstantObject.vToastSuccess -> Toasty.success(context, message, Toast.LENGTH_LONG).show()
            ConstantObject.vToastError -> Toasty.error(context, message, Toast.LENGTH_LONG).show()
            ConstantObject.vToastInfo -> Toasty.info(context, message, Toast.LENGTH_LONG).show()
        }
    }

    fun snackBarMessage(snackMessage: String, activity: Activity, action: Int) {
        val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
        when(action) {
            ConstantObject.vSnackBarWithButton -> {
                val snackBar = Snackbar.make(rootView, snackMessage, Snackbar.LENGTH_INDEFINITE)
                snackBar.setActionTextColor(Color.WHITE)
                snackBar.setAction("OK") {
                    snackBar.dismiss()
                }
                snackBar.show()
            }
            else -> {
                val snackBar = Snackbar.make(rootView, snackMessage, Snackbar.LENGTH_LONG)
                snackBar.setActionTextColor(Color.WHITE)
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
        AlertDialog.Builder(context).setTitle(alertTitle)
            .setMessage(alertMessage)
            .setPositiveButton(android.R.string.ok){
                    dialog, _ ->
                    dialog.dismiss()
                    dialogInterface.onPositiveClick(Any())
            }
            .setNegativeButton(android.R.string.cancel){
                    dialog, _ ->
                    dialog.dismiss()
                dialogInterface.onNegativeClick(Any())

            }
            .setCancelable(false)
            .create()
            .show()
    }

}