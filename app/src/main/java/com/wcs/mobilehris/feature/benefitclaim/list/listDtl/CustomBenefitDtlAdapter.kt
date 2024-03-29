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

class CustomBenefitDtlAdapter(private val context : Context,
                              private val listBenefDtl : MutableList<BenefitDtlModel>,
                              private val customIntentBenefDtlFrom : String,
                               private val statusBenefit: String):
    RecyclerView.Adapter<CustomBenefitDtlAdapter.ViewHolder>(){
    private lateinit var selectedBenefitInterface: SelectedBenefInterface

    fun initSelectedBenefCallback(itemCallBack : SelectedBenefInterface){
        this.selectedBenefitInterface = itemCallBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomBenefitDtlAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list_benefit_dtl,parent,false))
    }

    override fun getItemCount(): Int = listBenefDtl.size

    override fun onBindViewHolder(holder: CustomBenefitDtlAdapter.ViewHolder, position: Int) {
        val benfDtlModel = listBenefDtl[position]
        holder.tvItemContent1.text = benfDtlModel.benefitDtlDate.trim()
        holder.tvItemContent2.text = benfDtlModel.benefitType.trim()
        holder.tvItemContent3.text = benfDtlModel.benefitName.trim()
        holder.tvItemContent4.text = benfDtlModel.personalBenefit.trim()
        holder.tvItemContent5.text = benfDtlModel.amountClaim.trim()
        holder.tvItemContent6.text = benfDtlModel.paidClaim.trim()
        holder.tvItemContent7.text = benfDtlModel.diagnoseDisease.trim()
        holder.tvItemContent8.text = benfDtlModel.benefitDescription.trim()
        holder.imgBMenu.setOnClickListener { showBenefitPopUp(it, benfDtlModel) }
    }

    inner class  ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var imgBMenu : ImageButton = view.findViewById(R.id.imgB_menu_benefit)
        var tvItemContent1 : TextView = view.findViewById(R.id.tvItem_content1)
        var tvItemContent2 : TextView = view.findViewById(R.id.tvItem_content2)
        var tvItemContent3 : TextView = view.findViewById(R.id.tvItem_content3)
        var tvItemContent4 : TextView = view.findViewById(R.id.tvItem_content4)
        var tvItemContent5 : TextView = view.findViewById(R.id.tvItem_content5)
        var tvItemContent6 : TextView = view.findViewById(R.id.tvItem_content6)
        var tvItemContent7 : TextView = view.findViewById(R.id.tvItem_content7)
        var tvItemContent8 : TextView = view.findViewById(R.id.tvItem_content8)
    }

    private fun showBenefitPopUp(view : View, model : BenefitDtlModel){
        val popup = PopupMenu(context, view)
        when(statusBenefit){
            ConstantObject.vWaitingTask -> popup.inflate(R.menu.menu_benefit_dtl_list)
            ConstantObject.vNew -> popup.inflate(R.menu.menu_benefit_dtl_list)
            else -> popup.inflate(R.menu.menu_benefit_only_detail)
        }

        popup.setOnMenuItemClickListener{ item: MenuItem? ->
            when(item?.itemId){
                R.id.mnu_custom_benefit_list_dtl, R.id.mnu_custom_benefit_list_only_dtl ->onIntentPopUp("Detail", model)
                R.id.mnu_custom_benefit_list_edit -> onIntentPopUp("Edit", model)
                R.id.mnu_custom_benefit_list_delete ->{ selectedBenefitInterface.selectedItemBenefit(model) }
            }
            true
        }
        popup.show()
    }

    private fun onIntentPopUp(transType : String,model : BenefitDtlModel){
        val intent = Intent(context, BenefitTransActivity::class.java)
        intent.putExtra(BenefitTransActivity.extraBenefitTransDate, model.benefitDtlDate.trim())
        intent.putExtra(BenefitTransActivity.extraBenefitTransType, model.benefitType.trim())
        intent.putExtra(BenefitTransActivity.extraBenefitTransName, model.benefitName.trim())
        intent.putExtra(BenefitTransActivity.extraBenefitTransPerson, model.personalBenefit.trim())
        intent.putExtra(BenefitTransActivity.extraBenefitTransDiagnose, model.diagnoseDisease.trim())
        intent.putExtra(BenefitTransActivity.extraTransAmount, model.amountClaim.trim())
        intent.putExtra(BenefitTransActivity.extraTransPaidAmount, model.paidClaim.trim())
        intent.putExtra(BenefitTransActivity.extraTransDescription, model.benefitDescription.trim())
        intent.putExtra(BenefitTransActivity.extraBenefDocNo, model.benefitDocNo.trim())
        intent.putExtra(BenefitTransActivity.extraBenefitId, model.benefitDtlId.trim())
        intent.putExtra(ConstantObject.extra_intent, customIntentBenefDtlFrom)
        when(transType){
            "Detail" -> intent.putExtra(BenefitTransActivity.extraTransType, BenefitTransActivity.extraValueTransDtlType)
            "Edit" -> intent.putExtra(BenefitTransActivity.extraTransType, BenefitTransActivity.extraValueTransEditType)
        }
        context.startActivity(intent)
    }
}