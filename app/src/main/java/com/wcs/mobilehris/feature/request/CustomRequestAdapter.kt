package com.wcs.mobilehris.feature.request

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
import com.wcs.mobilehris.feature.requesttravellist.RequestTravelListActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

class CustomRequestAdapter(private val _context : Context, private val requestList : MutableList<RequestModel>):
    RecyclerView.Adapter<CustomRequestAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list_menu,parent,false))
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvCustom : CardView = view.findViewById(R.id.cv_custom)
        var tvMenu : TextView = view.findViewById(R.id.tv_custom)
        var imgCustom : ImageView = view.findViewById(R.id.imgV_custom)
        var tvMenuContent : TextView = view.findViewById(R.id.tv_custom_content)
    }

    override fun getItemCount(): Int  = requestList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : RequestModel = requestList[position]
        val reqMenu = model.itemMenu.trim()
        holder.tvMenu.text = reqMenu.trim()
        holder.tvMenuContent.visibility = View.GONE
        holder.imgCustom.setImageResource(model.imgItemMenu)
        holder.cvCustom.setOnClickListener {
            when(reqMenu){
                ConstantObject.travelMenu -> _context.startActivity(Intent(_context, RequestTravelListActivity::class.java))
                else -> MessageUtils.toastMessage(_context,"Test", ConstantObject.vToastInfo)
            }
        }
    }

}