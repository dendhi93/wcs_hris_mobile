package com.wcs.mobilehris.feature.dashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.utils.ConstantObject
import com.wcs.mobilehris.utils.MessageUtils

class CustomDashboardAdapter(private val _context : Context, private val dashList : MutableList<DashboardModel>):
    RecyclerView.Adapter<CustomDashboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list_dashboard,parent,false))
    }

    override fun getItemCount(): Int = dashList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var model : DashboardModel = dashList[position]
        var titleContent = model.vtitleContent.trim()
        holder.cvCustomDasboard.setOnClickListener {
            var intent : Intent? = null
            when(titleContent.trim()){
                _context.getString(R.string.team_menu) -> {
                    intent = Intent(_context, MenuActivity::class.java)
                    intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_TEAM)
                }
                _context.getString(R.string.approval_menu) -> {
                    intent = Intent(_context, MenuActivity::class.java)
                    intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_APPROVAL)
                }
                _context.getString(R.string.request_menu) -> {
                    intent = Intent(_context, MenuActivity::class.java)
                    intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_REQUEST)
                }
                _context.getString(R.string.activity_menu) -> {
                    intent = Intent(_context, MenuActivity::class.java)
                    intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_ACTIVITY)
                }
                _context.getString(R.string.absent_menu) -> {
                    intent = Intent(_context, MenuActivity::class.java)
                    intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_ABSENT)
                }
                _context.getString(R.string.confirmation_menu) -> {
                    intent = Intent(_context, MenuActivity::class.java)
                    intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_CONFIRMATION)
                }
                _context.getString(R.string.notification_menu) -> {
                    intent = Intent(_context, MenuActivity::class.java)
                    intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_NOTIFICATION)
                }
                _context.getString(R.string.status_menu) -> {
                    intent = Intent(_context, MenuActivity::class.java)
                    intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_STATUS)
                }
                else -> MessageUtils.toastMessage(_context,"Tidak Ada Menu tersebut pada list", ConstantObject.vToastError)
            }
            if (intent != null){
                _context.startActivity(intent)
            }
        }
        holder.tvDashboardTitle.text = titleContent.trim()
        holder.tvDashboardContent.text = model.vcontentDashboard.trim()
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvCustomDasboard : CardView = view.findViewById(R.id.cv_custom_dashboard)
        var tvDashboardTitle : TextView = view.findViewById(R.id.tv_custom_dash_title)
        var tvDashboardContent : TextView = view.findViewById(R.id.tv_custom_dash_content)
    }


}