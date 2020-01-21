package com.wcs.mobilehris.feature.dtltask

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R

class CustomDetailTaskAdapter(private val context : Context, private val dtlTaskList : MutableList<FriendModel>):
    RecyclerView.Adapter<CustomDetailTaskAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list_menu,parent,false))
    }

    override fun getItemCount(): Int = dtlTaskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imgVIconIsConflick.visibility = View.VISIBLE
        holder.imgVIconUser.setBackgroundResource(R.mipmap.ic_user_black)
        val model : FriendModel = dtlTaskList[position]
        val isUserConflict = model.isConflict
        when{
            isUserConflict -> holder.imgVIconIsConflick.setImageResource(R.mipmap.ic_block_32)
            else -> holder.imgVIconIsConflick.setImageResource(R.mipmap.ic_checklist_48)
        }
        holder.tvCustom.text = model.teamName.trim()
        holder.tvCustomContent.text = model.descriptionTeam.trim()
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var imgVIconUser : ImageView = view.findViewById(R.id.imgV_custom)
        var imgVIconIsConflick : ImageView = view.findViewById(R.id.imgV_custom_isConflict)
        var tvCustom : TextView = view.findViewById(R.id.tv_custom)
        var tvCustomContent : TextView = view.findViewById(R.id.tv_custom_content)
    }
}