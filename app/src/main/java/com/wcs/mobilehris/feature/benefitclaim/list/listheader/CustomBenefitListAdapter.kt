package com.wcs.mobilehris.feature.benefitclaim.list.listheader

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.feature.benefitclaim.list.listDtl.BenefitDtlListActivity
import com.wcs.mobilehris.util.ConstantObject

class CustomBenefitListAdapter(private val context : Context,
                               private val list : MutableList<BenefitModel>,
                               private val customIntentBenefitFrom : String):
    RecyclerView.Adapter<CustomBenefitListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
        viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_global_list,parent,false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : BenefitModel = list[position]
        holder.imgCustom.visibility = View.GONE
        holder.imgVIconStatus.visibility = View.VISIBLE
        holder.tvBenefitDate.visibility = View.VISIBLE
        when(model.statusBenefit){
            ConstantObject.vLeaveApprove -> holder.imgVIconStatus.setImageResource(R.mipmap.ic_checklist_48)
            ConstantObject.vWaitingTask -> holder.imgVIconStatus.setImageResource(R.mipmap.ic_waiting)
            ConstantObject.vLeaveReject -> holder.imgVIconStatus.setImageResource(R.mipmap.ic_block_32)
            else -> holder.imgVIconStatus.visibility = View.GONE
        }
        holder.tvBenefitDate.text = model.createdDate.trim()
        holder.tvBenefitDocNo.text = model.benefitDoc.trim()
        when(customIntentBenefitFrom){
            ConstantObject.vApproved -> {
                holder.tvBenefitRequestor.visibility = View.VISIBLE
                holder.tvBenefitRequestor.text = model.nameRequestor.trim()
            }
            else -> holder.tvBenefitRequestor.visibility = View.GONE
        }
        holder.cvCustomBenefit.setOnClickListener {
            val intent = Intent(context, BenefitDtlListActivity::class.java)
            intent.putExtra(ConstantObject.extra_intent, customIntentBenefitFrom)
            intent.putExtra(BenefitDtlListActivity.extraBenefitDocNo, model.benefitDoc.trim())
            intent.putExtra(BenefitDtlListActivity.extraBenefitTransType, model.statusBenefit)
            when(customIntentBenefitFrom){
                ConstantObject.vApproved -> intent.putExtra(BenefitDtlListActivity.extraBenefitRequestor, model.nameRequestor)
                else -> intent.putExtra(BenefitDtlListActivity.extraBenefitRequestor, "")
            }
            context.startActivity(intent)
        }
    }

    inner class  ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvCustomBenefit : CardView = view.findViewById(R.id.cv_custom)
        var imgCustom : ImageView = view.findViewById(R.id.imgV_custom)
        var imgVIconStatus : ImageView = view.findViewById(R.id.imgV_custom_1)
        var tvBenefitDocNo : TextView = view.findViewById(R.id.tv_custom)
        var tvBenefitDate : TextView = view.findViewById(R.id.tv_custom_content_2)
        var tvBenefitRequestor : TextView = view.findViewById(R.id.tv_custom_content)
    }
}