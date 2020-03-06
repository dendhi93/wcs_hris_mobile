package com.wcs.mobilehris.feature.requesttravellist

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
import com.wcs.mobilehris.feature.dtlreqtravel.DtlRequestTravelActivity
import com.wcs.mobilehris.util.ConstantObject

class CustomTravelListAdapter  (private val context : Context,
                                private val reqTravelList : MutableList<TravelListModel>,
                                private val customIntentTravelFrom : String):
    RecyclerView.Adapter<CustomTravelListAdapter.ViewHolder>(){
    private var mainContent : String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_global_list,parent,false))
    }

    override fun getItemCount(): Int = reqTravelList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : TravelListModel = reqTravelList[position]
        val finalTravelTime = model.departDate.trim() + " - " + model.returnDate.trim()
        val isAccepted = model.statusTravel
        holder.imgCustom.visibility = View.GONE
        holder.imgVIconIsConflick.visibility = View.VISIBLE
        holder.tvTravelDescription.visibility = View.VISIBLE
        holder.tvTravelBusinessType.visibility = View.VISIBLE
        mainContent = when(customIntentTravelFrom){
            ConstantObject.extra_fromIntentApproval -> model.requestor + "\n" +model.reasonDesc
            else -> model.reasonDesc
        }
        holder.tvTravelReason.text = mainContent.trim()
        holder.tvTravelTime.text = finalTravelTime
        holder.tvTravelDescription.text = model.travelDescription.trim()
        holder.tvTravelBusinessType.text = model.travelBusinessType.trim()
        holder.cvCustomTravel.setOnClickListener {
            when(customIntentTravelFrom){
                ConstantObject.extra_fromIntentRequest ->{
                    val intent = Intent(context, DtlRequestTravelActivity::class.java)
                    intent.putExtra(ConstantObject.extra_intent, ConstantObject.extra_fromIntentDtlTravel)
                    intent.putExtra(DtlRequestTravelActivity.extraTravelId, model.travelId)
                    intent.putExtra(DtlRequestTravelActivity.extraTravelRequestor, "")
                    context.startActivity(intent)
                }
                else -> {
                    val intent = Intent(context, DtlRequestTravelActivity::class.java)
                    intent.putExtra(ConstantObject.extra_intent, ConstantObject.extra_fromIntentApproval)
                    intent.putExtra(DtlRequestTravelActivity.extraTravelId, model.travelId)
                    intent.putExtra(DtlRequestTravelActivity.extraTravelRequestor, model.requestor)
                    context.startActivity(intent)
                }
            }
        }
        when(isAccepted){
            "True" -> holder.imgVIconIsConflick.setImageResource(R.mipmap.ic_checklist_48)
            "Waiting" -> holder.imgVIconIsConflick.setImageResource(R.mipmap.ic_waiting)
            "False" -> holder.imgVIconIsConflick.setImageResource(R.mipmap.ic_block_32)
            else -> holder.imgVIconIsConflick.visibility = View.GONE
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvCustomTravel : CardView = view.findViewById(R.id.cv_custom)
        var tvTravelReason : TextView = view.findViewById(R.id.tv_custom)
        var imgCustom : ImageView = view.findViewById(R.id.imgV_custom)
        var tvTravelTime : TextView = view.findViewById(R.id.tv_custom_content)
        var tvTravelDescription : TextView = view.findViewById(R.id.tv_custom_content_2)
        var tvTravelBusinessType : TextView = view.findViewById(R.id.tv_custom_content_3)
        var imgVIconIsConflick : ImageView = view.findViewById(R.id.imgV_custom_1)
    }

}