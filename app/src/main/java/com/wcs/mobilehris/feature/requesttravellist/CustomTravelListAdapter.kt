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

class CustomTravelListAdapter  (private val context : Context, private val reqTravelList : MutableList<TravelListModel>):
    RecyclerView.Adapter<CustomTravelListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_global_list,parent,false))
    }

    override fun getItemCount(): Int = reqTravelList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : TravelListModel = reqTravelList[position]
        val finalTravel = model.depart.trim() + " - " +model.arrival.trim()
        val finalTime = model.dateFrom.trim() + " - " + model.dateInto.trim()
        val isAccepted = model.statusTravel
        holder.imgCustom.visibility = View.GONE
        holder.imgVIconIsConflick.visibility = View.VISIBLE
        holder.imgVWayTravel.visibility = View.GONE
        holder.tvTravelDestination.text = finalTravel
        holder.tvTravelTime.text = finalTime
        holder.cvCustomTravel.setOnClickListener {
           val intent = Intent(context, DtlRequestTravelActivity::class.java)
            intent.putExtra(ConstantObject.extra_intent, ConstantObject.extra_fromIntentDtlTravel)
            intent.putExtra(DtlRequestTravelActivity.extraTravelId, model.travelId)
            context.startActivity(intent)
        }
        when(isAccepted){
            "True" -> holder.imgVIconIsConflick.setImageResource(R.mipmap.ic_checklist_48)
            "Waiting" -> holder.imgVIconIsConflick.setImageResource(R.mipmap.ic_waiting)
            else -> holder.imgVIconIsConflick.setImageResource(R.mipmap.ic_block_32)
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvCustomTravel : CardView = view.findViewById(R.id.cv_custom)
        var tvTravelDestination : TextView = view.findViewById(R.id.tv_custom)
        var imgCustom : ImageView = view.findViewById(R.id.imgV_custom)
        var tvTravelTime : TextView = view.findViewById(R.id.tv_custom_content)
        var imgVIconIsConflick : ImageView = view.findViewById(R.id.imgV_custom_1)
        var imgVWayTravel : ImageView = view.findViewById(R.id.imgV_custom_2)
    }

}