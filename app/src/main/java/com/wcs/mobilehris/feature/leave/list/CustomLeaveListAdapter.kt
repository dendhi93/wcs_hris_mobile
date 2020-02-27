package com.wcs.mobilehris.feature.leave.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

class CustomLeaveListAdapter(private val context : Context,
                             private val list : MutableList<LeaveListModel>,
                             private val customIntentLeaveFrom : String):
    RecyclerView.Adapter<CustomLeaveListAdapter.ViewHolder>() {
    private var mainLeaveContent : String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_global_list,parent,false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : LeaveListModel = list[position]
        val leaveDate = model.leaveDateFrom + " - " + model.leaveDateInto
        mainLeaveContent = when(customIntentLeaveFrom){
            ConstantObject.extra_fromIntentApproval -> model.leaveRequestor.trim() + "\n" + model.leaveDescription
            else -> model.leaveDescription
        }
        holder.tvLeaveDescription.text = mainLeaveContent.trim()
        holder.tvLeaveDate.text = leaveDate.trim()
        holder.imgCustom.visibility = View.GONE
        holder.imgVIconStatus.visibility = View.VISIBLE
        when(model.leaveStatus){
            "True" -> holder.imgVIconStatus.setImageResource(R.mipmap.ic_checklist_48)
            "Waiting" -> holder.imgVIconStatus.setImageResource(R.mipmap.ic_waiting)
            "False" -> holder.imgVIconStatus.setImageResource(R.mipmap.ic_block_32)
            else -> holder.imgVIconStatus.visibility = View.GONE
        }
        holder.cvCustomLeave.setOnClickListener {
            when(customIntentLeaveFrom){
                ConstantObject.extra_fromIntentRequest -> MessageUtils.toastMessage(context, "Coba", ConstantObject.vToastInfo)
                else -> MessageUtils.toastMessage(context, "Coba", ConstantObject.vToastInfo)
            }
        }
    }

    inner class  ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvCustomLeave : CardView = view.findViewById(R.id.cv_custom)
        var tvLeaveDescription : TextView = view.findViewById(R.id.tv_custom)
        var tvLeaveDate : TextView = view.findViewById(R.id.tv_custom_content)
        var imgCustom : ImageView = view.findViewById(R.id.imgV_custom)
        var imgVIconStatus : ImageView = view.findViewById(R.id.imgV_custom_1)
    }
}