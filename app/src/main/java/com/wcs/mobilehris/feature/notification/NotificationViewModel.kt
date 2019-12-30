package com.wcs.mobilehris.feature.notification

import android.content.Context
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject

class NotificationViewModel(private var context : Context, private var notificationInterface: NotificationInterface) : ViewModel(){

    val isVisibleUI = ObservableField<Boolean>(false)

    fun validateNotification(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> {
                notificationInterface.onAlertNotif(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, NotificationFragment.ALERT_NOTIF_NO_CONNECTION)
            }
            else -> getNotifData()
        }
    }

    private fun getNotifData(){
        isVisibleUI.set(true)
        val listNotif = mutableListOf<NotificationModel>()
        var notifModel = NotificationModel("Fransiska Martatuli",
            "Pendaftaran Email dan handphone Grab",
            "26/12/2019","Dear All\n\nMohon diinfokan alamat email dan handphone yang digunakan di aplikasi Grab melalui email\n\nRegards\nSiska")
        listNotif.add(notifModel)
        notifModel = NotificationModel("Nidya Sari",
            "Pakta Integritas",
            "27/12/2019","Dear All\n" +
                    "\n" +
                    "Mohon mengikutin acara Pakta Integritas pada hari ini pukul 13.00 - 15.00\n" +
                    "\n" +
                    "Regards\n" +
                    "Siska")
        listNotif.add(notifModel)

        Handler().postDelayed({
            notificationInterface.onLoadNotif(listNotif)
        }, 2000)
    }

}