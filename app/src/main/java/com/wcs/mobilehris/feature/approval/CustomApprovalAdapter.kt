package com.wcs.mobilehris.feature.approval

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.utils.ConstantObject
import com.wcs.mobilehris.utils.MessageUtils

class CustomApprovalAdapter (private val _context : Context, private val approvalList : MutableList<ApprovalModel>):
    RecyclerView.Adapter<CustomApprovalAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomApprovalAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list_menu,parent,false))
    }

    override fun getItemCount(): Int = approvalList.size

    override fun onBindViewHolder(holder: CustomApprovalAdapter.ViewHolder, position: Int) {
        var model : ApprovalModel = approvalList[position]
        val stMenu = model.itemMenu.trim()+" "+ model.qtyApproval.toString()
        holder.tvMenu.text = stMenu.trim()
        holder.tvMenuContent.text = model.itemMenuContent.trim()
        holder.imgCustom.setImageResource(model.imgItemMenu)
        holder.cvCustom.setOnClickListener {
            MessageUtils.toastMessage(_context, "coba", ConstantObject.vToastInfo)
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvCustom : CardView = view.findViewById(R.id.cv_custom)
        var tvMenu : TextView = view.findViewById(R.id.tv_custom)
        var imgCustom : ImageView = view.findViewById(R.id.imgV_custom)
        var tvMenuContent : TextView = view.findViewById(R.id.tv_custom_content)
    }



}