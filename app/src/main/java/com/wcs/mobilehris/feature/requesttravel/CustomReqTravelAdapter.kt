package com.wcs.mobilehris.feature.requesttravel

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

class CustomReqTravelAdapter (private val context : Context, private val list : MutableList<ReqTravelModel>):
    RecyclerView.Adapter<CustomReqTravelAdapter.ViewHolder>(){
    private lateinit var selectedTravelInterface: SelectedTravelInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_req_travel_list,parent,false))
    }

    fun initSelectedTravel(itemTravelCallBack : SelectedTravelInterface){
        this.selectedTravelInterface = itemTravelCallBack
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : ReqTravelModel = list[position]
        val travelCity = model.depart.trim() + " - " +model.arrival.trim()
        val travelHotelTime = model.dateCheckIn + " - " +model.dateCheckOut.trim()
        val stCustomTransType = model.transType.split("-")[1]
        holder.tvCityName.text = travelCity
        holder.tvTravelCheckInCheckOut.text = travelHotelTime
        holder.tvTravelTransType.text = stCustomTransType.trim()
        holder.tvTravelHotelName.text = model.hotelName.trim()
        holder.imgVTravelTrash.setOnClickListener {
            selectedTravelInterface.selectedItemTravel(model)
        }
        holder.cvCustomTravel.setOnClickListener {
            MessageUtils.toastMessage(context, "Test 2", ConstantObject.vToastInfo)
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvCustomTravel : CardView = view.findViewById(R.id.cv_custom_reqTravel)
        var tvCityName : TextView = view.findViewById(R.id.tv_customReqTravel_city_name)
        var tvTravelCheckInCheckOut : TextView = view.findViewById(R.id.tv_customReqTravel_checkInCheckOut)
        var tvTravelTransType : TextView = view.findViewById(R.id.tv_customReqTravel_transType)
        var tvTravelHotelName : TextView = view.findViewById(R.id.tv_customReqTravel_hotelName)
        var imgVTravelTrash : ImageView = view.findViewById(R.id.imgV_customReqTravel)
    }

}