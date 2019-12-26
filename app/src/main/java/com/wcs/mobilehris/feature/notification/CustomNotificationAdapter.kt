package com.wcs.mobilehris.feature.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

class CustomNotificationAdapter(private val context : Context, private val notifList : MutableList<NotificationModel>):
    RecyclerView.Adapter<CustomNotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list_notification,parent,false))
    }

    override fun getItemCount(): Int = notifList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : NotificationModel = notifList[position]

        holder.tvCustomNotifTitle.text = model.createdUser.trim()
        holder.tvCustomNotifContent.text = model.notifContent.trim()
        holder.tvCustomNotifDate.text = model.createdDate.trim()
        holder.lnCustomNotif.setOnClickListener {
            MessageUtils.toastMessage(context, "Coba", ConstantObject.vToastInfo)
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var  tvCustomNotifTitle : TextView = view.findViewById(R.id. tv_custom_notification_title)
        var  tvCustomNotifContent : TextView = view.findViewById(R.id. tv_custom_notification_content)
        var  tvCustomNotifDate : TextView = view.findViewById(R.id. tv_custom_notification_date)
        var lnCustomNotif : LinearLayout = view.findViewById(R.id.ln_customNotif)
    }

}