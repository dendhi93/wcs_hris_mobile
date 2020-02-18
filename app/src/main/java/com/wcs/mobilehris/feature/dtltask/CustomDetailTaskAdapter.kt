package com.wcs.mobilehris.feature.dtltask

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.feature.createtask.SelectedFriendInterface
import com.wcs.mobilehris.util.ConstantObject

class CustomDetailTaskAdapter(private val context : Context, private val dtlTaskList : MutableList<FriendModel>, private val listFrom : String):
    RecyclerView.Adapter<CustomDetailTaskAdapter.ViewHolder>(){
    private lateinit var selectedFriendInterface: SelectedFriendInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_global_list,parent,false))
    }

    fun initSelectedTeamCallback(itemCallBack : SelectedFriendInterface){
        this.selectedFriendInterface = itemCallBack
    }

    override fun getItemCount(): Int = dtlTaskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imgVIconIsConflick.visibility = View.VISIBLE
        holder.imgVIconUser.setBackgroundResource(R.mipmap.ic_user_black)
        holder.imgv_2.setBackgroundResource(R.mipmap.ic_trash_24)
        when(listFrom){
            ConstantObject.vCreateEdit -> holder.imgv_2.visibility = View.VISIBLE
            else -> holder.imgv_2.visibility = View.GONE
        }
        val model : FriendModel = dtlTaskList[position]
        val isUserConflict = model.isConflict
        when{
            isUserConflict -> holder.imgVIconIsConflick.setImageResource(R.mipmap.ic_block_32)
            else -> holder.imgVIconIsConflick.setImageResource(R.mipmap.ic_checklist_48)
        }
        holder.tvCustom.text = model.teamName.trim()
        holder.tvCustomContent.text = model.descriptionTeam.trim()
        holder.imgv_2.setOnClickListener {
            when(listFrom){
                ConstantObject.vCreateEdit -> selectedFriendInterface.selectedItemFriend(model)
            }
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var imgVIconUser : ImageView = view.findViewById(R.id.imgV_custom)
        var imgVIconIsConflick : ImageView = view.findViewById(R.id.imgV_custom_1)
        var tvCustom : TextView = view.findViewById(R.id.tv_custom)
        var tvCustomContent : TextView = view.findViewById(R.id.tv_custom_content)
        var imgv_2 : ImageView = view.findViewById(R.id.imgV_custom_2)
    }
}