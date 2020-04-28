package com.wcs.mobilehris.feature.benefitclaim.list.listDtl

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.feature.benefitclaim.trans.BenefitTransActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

class CustomBenefitDtlAdapter(private val context : Context,
                              private val listBenefDtl : MutableList<BenefitDtlModel>,
                              private val customIntentBenefDtlFrom : String,
                               private val statusBenefit: String):
    RecyclerView.Adapter<CustomBenefitDtlAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomBenefitDtlAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list_benefit_dtl,parent,false))
    }

    override fun getItemCount(): Int = listBenefDtl.size

    override fun onBindViewHolder(holder: CustomBenefitDtlAdapter.ViewHolder, position: Int) {
        val benfDtlModel = listBenefDtl[position]
        holder.tvitemContent1.text = benfDtlModel.benefitDtlDate.trim()
        holder.tvitemContent2.text = benfDtlModel.benefitType.trim()
        holder.tvitemContent3.text = benfDtlModel.benefitName.trim()
        holder.tvitemContent4.text = benfDtlModel.personalBenefit.trim()
        holder.tvitemContent5.text = benfDtlModel.amountClaim.trim()
        holder.tvitemContent6.text = benfDtlModel.paidClaim.trim()
        holder.tvitemContent7.text = benfDtlModel.diagnoseDisease.trim()
        holder.tvitemContent8.text = benfDtlModel.benefitDescription.trim()
        holder.imgBMenu.setOnClickListener { showBenefitPopUp(it, benfDtlModel) }
    }

    inner class  ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var imgBMenu : ImageButton = view.findViewById(R.id.imgB_menu_benefit)
        var tvitemContent1 : TextView = view.findViewById(R.id.tvItem_content1)
        var tvitemContent2 : TextView = view.findViewById(R.id.tvItem_content2)
        var tvitemContent3 : TextView = view.findViewById(R.id.tvItem_content3)
        var tvitemContent4 : TextView = view.findViewById(R.id.tvItem_content4)
        var tvitemContent5 : TextView = view.findViewById(R.id.tvItem_content5)
        var tvitemContent6 : TextView = view.findViewById(R.id.tvItem_content6)
        var tvitemContent7 : TextView = view.findViewById(R.id.tvItem_content7)
        var tvitemContent8 : TextView = view.findViewById(R.id.tvItem_content8)
    }

    private fun showBenefitPopUp(view : View, model : BenefitDtlModel){
        val popup = PopupMenu(context, view)
        when(statusBenefit){
            ConstantObject.vWaitingTask -> popup.inflate(R.menu.menu_benefit_dtl_list)
            ConstantObject.vNew -> popup.inflate(R.menu.menu_benefit_dtl_list)
            else -> popup.inflate(R.menu.menu_benefit_only_detail)
        }
        val intent = Intent(context, BenefitTransActivity::class.java)
        popup.setOnMenuItemClickListener{ item: MenuItem? ->
            when(item?.itemId){
                R.id.mnu_custom_benefit_list_dtl, R.id.mnu_custom_benefit_list_only_dtl ->{
                    intent.putExtra(BenefitTransActivity.extraBenefitTransDate, model.benefitDtlDate.trim())
                    intent.putExtra(BenefitTransActivity.extraBenefitTransType, model.benefitType.trim())
                    intent.putExtra(BenefitTransActivity.extraBenefitTransName, model.benefitName.trim())
                    intent.putExtra(BenefitTransActivity.extraBenefitTransPerson, model.personalBenefit.trim())
                    intent.putExtra(BenefitTransActivity.extraBenefitTransDiagnose, model.diagnoseDisease.trim())
                    intent.putExtra(BenefitTransActivity.extraTransAmount, model.amountClaim.trim())
                    intent.putExtra(BenefitTransActivity.extraTransPaidAmount, model.paidClaim.trim())
                    intent.putExtra(BenefitTransActivity.extraTransDescription, model.benefitDescription.trim())
                    intent.putExtra(ConstantObject.extra_intent, customIntentBenefDtlFrom)
                    intent.putExtra(BenefitTransActivity.extraTransType, BenefitTransActivity.extraValueTransDtlType)
                    context.startActivity(intent)
                }
                R.id.mnu_custom_benefit_list_edit ->{
                    intent.putExtra(BenefitTransActivity.extraBenefitTransDate, model.benefitDtlDate.trim())
                    intent.putExtra(BenefitTransActivity.extraBenefitTransType, model.benefitType.trim())
                    intent.putExtra(BenefitTransActivity.extraBenefitTransName, model.benefitName.trim())
                    intent.putExtra(BenefitTransActivity.extraBenefitTransPerson, model.personalBenefit.trim())
                    intent.putExtra(BenefitTransActivity.extraBenefitTransDiagnose, model.diagnoseDisease.trim())
                    intent.putExtra(BenefitTransActivity.extraTransAmount, model.amountClaim.trim())
                    intent.putExtra(BenefitTransActivity.extraTransPaidAmount, model.paidClaim.trim())
                    intent.putExtra(BenefitTransActivity.extraTransDescription, model.benefitDescription.trim())
                    intent.putExtra(ConstantObject.extra_intent, customIntentBenefDtlFrom)
                    intent.putExtra(BenefitTransActivity.extraTransType, BenefitTransActivity.extraValueTransEditType)
                    context.startActivity(intent)
                }
                R.id.mnu_custom_benefit_list_delete ->{
                    MessageUtils.toastMessage(context, "delete", ConstantObject.vToastInfo)
                }
            }
            true
        }
        popup.show()
    }
}