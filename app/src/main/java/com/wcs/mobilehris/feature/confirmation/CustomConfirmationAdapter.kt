package com.wcs.mobilehris.feature.confirmation

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
import com.wcs.mobilehris.util.MessageUtils

class CustomConfirmationAdapter (private val context : Context, private val confirmList : MutableList<ConfirmationModel>):
    RecyclerView.Adapter<CustomConfirmationAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_global_list,parent,false))
    }

    override fun getItemCount(): Int = confirmList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = confirmList[position]
        holder.tvConfMenu.text = model.itemConfMenu.trim()
        holder.imgConfCustom.setImageResource(model.imgItemConfMenu)
        holder.tvConfMenuContent.text = model.itemConfContentMenu.trim()
        holder.cvConfCustom.setOnClickListener {
            when(model.itemConfMenu.trim()){
                ConstantObject.extra_fromIntentConfirmTravel -> {
                    val intent = Intent(context, DtlRequestTravelActivity::class.java)
                    intent.putExtra(ConstantObject.extra_intent, ConstantObject.extra_fromIntentConfirmTravel)
                    intent.putExtra(DtlRequestTravelActivity.extraTravelId, model.menuId)
                    context.startActivity(intent)
                }
            }
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvConfCustom : CardView = view.findViewById(R.id.cv_custom)
        var tvConfMenu : TextView = view.findViewById(R.id.tv_custom)
        var imgConfCustom : ImageView = view.findViewById(R.id.imgV_custom)
        var tvConfMenuContent : TextView = view.findViewById(R.id.tv_custom_content)
    }

}