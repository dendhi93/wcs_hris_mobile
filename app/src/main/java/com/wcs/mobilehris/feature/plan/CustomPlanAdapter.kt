package com.wcs.mobilehris.feature.plan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

class CustomPlanAdapter (private val _context : Context, private val planList : MutableList<ContentPlanModel>):
    RecyclerView.Adapter<CustomPlanAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list_plan,parent,false))
    }

    override fun getItemCount(): Int = planList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var model : ContentPlanModel = planList[position]
        val stTaskTime = model.beginTaskTime + " - " +model.endTaskTime
        val stCreated = model.userCreate.trim() + " at " + model.createDate.trim()
        holder.tvCreatedUser.text = stCreated
        holder.tvCustomCompanyName.text = model.companyName.trim()
        holder.tvCustomLocation.text = model.locationTask.trim()
        holder.tvCustomTime.text = stTaskTime.trim()
        holder.tvCustomDate.text = model.taskDate.trim()
        holder.tvCustomTitle.text = model.taskType.trim()
        holder.imgBCustomPlan.setOnClickListener {
            MessageUtils.toastMessage(_context, "Under Maintenance", ConstantObject.vToastInfo)
        }
        holder.cvCustomPlan.setOnClickListener {
            MessageUtils.toastMessage(_context, "Under Maintenance 2", ConstantObject.vToastInfo)
        }
        val flagTask = model.flagTask.trim()
        holder.btnPlan.text = flagTask
        when(flagTask){
            ConstantObject.vPlanTask -> holder.btnPlan.isEnabled = false
            else -> holder.btnPlan.isEnabled = true
        }

    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvCustomPlan : CardView = view.findViewById(R.id.cv_customPlan)
        var imgBCustomPlan : ImageButton = view.findViewById(R.id.imgB_menu)
        var tvCustomTitle : TextView = view.findViewById(R.id.tv_custom_plan)
        var tvCreatedUser : TextView = view.findViewById(R.id.tv_customPlan_created_user)
        var tvCustomCompanyName : TextView = view.findViewById(R.id.tv_customPlan_companyName)
        var tvCustomLocation : TextView = view.findViewById(R.id.tv_customPlan_Location)
        var tvCustomDate : TextView = view.findViewById(R.id.tv_customPlan_date)
        var tvCustomTime : TextView = view.findViewById(R.id.tv_customPlan_time)
        var btnPlan : Button = view.findViewById(R.id.btn_customPlan)

    }


}