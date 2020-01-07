package com.wcs.mobilehris.feature.notification

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.feature.dtlnotification.DetailNotificationActivity

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
        holder.cvCustomNotif.setOnClickListener {
            val intent = Intent(context, DetailNotificationActivity::class.java)
            intent.putExtra(DetailNotificationActivity.INTENT_DATE, model.createdDate)
            intent.putExtra(DetailNotificationActivity.INTENT_TITLE, model.notifContent)
            intent.putExtra(DetailNotificationActivity.INTENT_CONTENT, model.dtlContent)
            context.startActivity(intent)
//            MessageUtils.toastMessage(context,"" +model.notifContent.trim(), ConstantObject.vToastInfo)
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var  tvCustomNotifTitle : TextView = view.findViewById(R.id. tv_custom_notification_title)
        var  tvCustomNotifContent : TextView = view.findViewById(R.id. tv_custom_notification_content)
        var  tvCustomNotifDate : TextView = view.findViewById(R.id. tv_custom_notification_date)
        var cvCustomNotif : CardView = view.findViewById(R.id.cv_custom_notif)
    }

}