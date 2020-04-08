package com.wcs.mobilehris.feature.approvallistofactivities

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.feature.approvallistofactivities.detail.DetailApprovalActivity
import com.wcs.mobilehris.util.ConstantObject

class AppActionListAdapter(private val context : Context, private val listOfActions : MutableList<AppActionListModel>) :
        RecyclerView.Adapter<AppActionListAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppActionListAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list_approval_activity, parent, false))
    }

    override fun getItemCount(): Int {
        return listOfActions.size
    }

    override fun onBindViewHolder(holder: AppActionListAdapter.ViewHolder, position: Int) {
        val model : AppActionListModel = listOfActions[position]

        holder.nik.text = model.activityNik.trim()
        holder.chargeCode.text = model.activityChargeCode.trim()
        holder.description.text = model.activityDescription.trim()
        holder.cvCustomActivity.setOnClickListener {
            val intent = Intent(context, DetailApprovalActivity::class.java)
            intent.putExtra(AppActionListActivity.extraDocumentNumber, model.activityDocNo)
            intent.putExtra(AppActionListActivity.extraActivityId, model.activityId)
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvCustomActivity : CardView = view.findViewById(R.id.cv_customListApprovalActivity)
        var nik : TextView = view.findViewById(R.id.tv_nik)
        var chargeCode : TextView = view.findViewById(R.id.tv_chargeCode)
        var description : TextView = view.findViewById(R.id.tv_description)
    }
}