package com.wcs.mobilehris.feature.plan

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.feature.confirmtask.ConfirmTaskActivity
import com.wcs.mobilehris.feature.dtltask.DetailTaskActivity
import com.wcs.mobilehris.util.ConstantObject

class CustomTaskAdapter (private val _context : Context, private val planList : MutableList<ContentTaskModel>):
    RecyclerView.Adapter<CustomTaskAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list_task,parent,false))
    }

    override fun getItemCount(): Int = planList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : ContentTaskModel = planList[position]
        val stTaskTime = model.beginTaskTime + " - " +model.endTaskTime
        val stCreated = model.userCreate.trim() + " at " + model.createDate.trim()
        val stActionHour = model.actHourTaken
        val isCompleted = model.isCompleted
        val stActHour: String?
        val stSplitChargeCode = model.contentChargeCode.trim().split("|")
        val finalChargeCode = stSplitChargeCode[0]+"\n"+stSplitChargeCode[1]
        holder.tvCreatedUser.text = stCreated
        holder.tvCustomCompanyName.text = model.companyName.trim()
        holder.tvCustomTime.text = stTaskTime.trim()
        holder.tvCustomDate.text = model.taskDate.trim()
        holder.tvCustomTitle.text = finalChargeCode.trim()
        when{
            stActionHour > 0 -> {
                stActHour = "$stActionHour Hour "
                holder.lnCustomTimeTaken.visibility = View.VISIBLE
                holder.tvCustomTimeTaken.text = stActHour.trim()
            }
            else -> holder.lnCustomTimeTaken.visibility = View.GONE
        }
        when{
            !isCompleted -> {
                holder.vwCustomList.visibility = View.GONE
                holder.rlCustomListBottom.setBackgroundColor(Color.GRAY)
                holder.tvCustomIsCompleted.text = model.completedDescription.trim()
            }
            else -> {
                holder.vwCustomList.visibility = View.VISIBLE
                holder.rlCustomListBottom.setBackgroundColor(Color.WHITE)
            }
        }

        holder.imgBCustomPlan.setOnClickListener {
            showPopUp(it, model)
        }
        val flagTask = model.flagTask.trim()
        holder.btnPlan.text = flagTask
        when(flagTask){
            ConstantObject.vPlanTask -> {
                holder.btnPlan.isEnabled = false
                holder.btnPlan.setBackgroundResource(R.drawable.bg_red_button)
            }
            ConstantObject.vConfirmTask -> {
                holder.btnPlan.isEnabled = true
                holder.btnPlan.setBackgroundResource(R.drawable.bg_light_green_button)
            }
            ConstantObject.vWaitingTask ->{
                holder.btnPlan.isEnabled = true
                holder.btnPlan.setBackgroundResource(R.drawable.bg_green_button)
            }
            else -> {
                holder.btnPlan.isEnabled = false
                holder.btnPlan.setBackgroundResource(R.drawable.bg_green_button)
            }
        }
        holder.btnPlan.setOnClickListener{
            val intent = Intent(_context, ConfirmTaskActivity::class.java)
            intent.putExtra(ConfirmTaskActivity.intentExtraTaskId, model.taskId)
            intent.putExtra(ConfirmTaskActivity.intentExtraChargeCode, model.contentChargeCode)
            //VALIDATE click from fragment confirm / completed
            when(flagTask){
                ConstantObject.vWaitingTask ->{ intent.putExtra(ConstantObject.extra_intent, ConstantObject.vEditTask) }
                ConstantObject.vConfirmTask ->{ intent.putExtra(ConstantObject.extra_intent, ConstantObject.vConfirmTask) }
            }
            _context.startActivity(intent)
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var tvCustomTitle : TextView = view.findViewById(R.id.tv_custom_plan)
        var tvCreatedUser : TextView = view.findViewById(R.id.tv_customPlan_created_user)
        var tvCustomCompanyName : TextView = view.findViewById(R.id.tv_customPlan_companyName)
        var tvCustomDate : TextView = view.findViewById(R.id.tv_customPlan_date)
        var tvCustomTime : TextView = view.findViewById(R.id.tv_customPlan_time)
        var tvCustomIsCompleted : TextView = view.findViewById(R.id.tv_custom_isCompleted)
        var tvCustomTimeTaken : TextView = view.findViewById(R.id.tv_customPlan_time_taken)
        var lnCustomTimeTaken : LinearLayout = view.findViewById(R.id.ln_custom_actHourTaken)
        var rlCustomListBottom : RelativeLayout = view.findViewById(R.id.rl_customList_bottom)
        var vwCustomList : View = view.findViewById(R.id.view_customList)
        var btnPlan : Button = view.findViewById(R.id.btn_customPlan)
        var imgBCustomPlan : ImageButton = view.findViewById(R.id.imgB_menu)
    }

    private fun showPopUp(view : View, model : ContentTaskModel){
        val popup = PopupMenu(_context, view)
        popup.inflate(R.menu.menu_custom_list_task)
        popup.setOnMenuItemClickListener{ item: MenuItem? ->
            when(item?.itemId){
                R.id.mnu_custom_list_task_dtl ->{
                    val intent = Intent(_context, DetailTaskActivity::class.java)
                    intent.putExtra(DetailTaskActivity.extraTaskId, model.taskId)
                    intent.putExtra(DetailTaskActivity.extraCode, model.contentChargeCode)
                    _context.startActivity(intent)
                }
            }
            true
        }
        popup.show()
    }
}