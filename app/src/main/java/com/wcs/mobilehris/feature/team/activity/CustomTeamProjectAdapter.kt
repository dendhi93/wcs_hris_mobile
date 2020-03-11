package com.wcs.mobilehris.feature.team.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.feature.team.fragment.SelectedTeamInterface

class CustomTeamProjectAdapter(private val context : Context, private var teamProjectList : MutableList<TeamProjectModel>):
    RecyclerView.Adapter<CustomTeamProjectAdapter.ViewHolder>(){
    private lateinit var selectedInterface : SelectedTeamInterface.SelectedTeamProjectInterface

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_global_list,parent,false))
    }

    override fun getItemCount(): Int = teamProjectList.size

    fun initSelectedCallBack(itemCallBack : SelectedTeamInterface.SelectedTeamProjectInterface){
        this.selectedInterface = itemCallBack
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = teamProjectList[position]
        holder.imgVcustom.setImageResource(R.mipmap.ic_user_black)
        holder.tvNameTeam.text = model.nameTeam.trim()
        holder.tvNameStatus.text = model.statusTeam.trim()
        holder.cvTeamProject.setOnClickListener {
            selectedInterface.selectedItemNameProject(model)
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvTeamProject : CardView = view.findViewById(R.id.cv_custom)
        var imgVcustom : ImageView = view.findViewById(R.id.imgV_custom)
        var tvNameTeam : TextView = view.findViewById(R.id.tv_custom)
        var tvNameStatus : TextView = view.findViewById(R.id.tv_custom_content)
    }

    fun filterListTeamProject(filterListteam : ArrayList<TeamProjectModel>){
        teamProjectList = filterListteam
        notifyDataSetChanged()
    }
}