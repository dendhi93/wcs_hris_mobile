package com.wcs.mobilehris.feature.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentNotificationBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import java.util.ArrayList

class NotificationFragment : Fragment(), NotificationInterface {

    private lateinit var bindingNotification : FragmentNotificationBinding
    private lateinit var notificationAdapter : CustomNotificationAdapter
    private var arrNotifList = ArrayList<NotificationModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindingNotification = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        bindingNotification.viewModel = NotificationViewModel(requireContext(), this)
        return bindingNotification.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingNotification.rcNotification.layoutManager = LinearLayoutManager(requireContext())
        bindingNotification.rcNotification.setHasFixedSize(true)
        notificationAdapter = CustomNotificationAdapter(requireContext(), arrNotifList)
        bindingNotification.rcNotification.adapter = notificationAdapter
    }

    override fun onResume() {
        super.onResume()
        bindingNotification.viewModel?.validateNotification()
    }

    override fun onLoadNotif(vListDash: List<NotificationModel>) {
        arrNotifList.clear()
        for(i in vListDash.indices ){
            arrNotifList.add(NotificationModel(vListDash[i].createdUser.trim(),
                vListDash[i].notifContent.trim(),
                vListDash[i].createdDate.trim()))
        }
        notificationAdapter.notifyDataSetChanged()
        bindingNotification.viewModel?.isVisibleUI?.set(false)
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(requireContext(), message,
                ConstantObject.vToastError)
        }
    }

    override fun onAlertNotif(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_NOTIF_NO_CONNECTION -> {
                MessageUtils.alertDialogDismiss(alertMessage, alertTitle, requireContext())}
        }
    }

    companion object{
        const val ALERT_NOTIF_NO_CONNECTION = 1
    }
}
