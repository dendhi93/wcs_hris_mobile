package com.wcs.mobilehris.feature.team

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

class CustomTeamAdapter(private val _context : Context, private val teamList : MutableList<TeamModel>):
    RecyclerView.Adapter<CustomTeamAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list_team,parent,false))
    }

    override fun getItemCount(): Int = teamList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : TeamModel = teamList[position]
        holder.tvTeamName.text = model.name.trim()
        holder.tvTeamPhone.text = model.phone.trim()
        holder.tvTeamMail.text = model.email.trim()
        holder.cvTeam.setOnClickListener {}
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvTeam : CardView = view.findViewById(R.id.cv_custom_team)
        var tvTeamName : TextView = view.findViewById(R.id.tv_custom_team_name)
        var tvTeamPhone : TextView = view.findViewById(R.id.tv_custom_team_phone)
        var tvTeamMail : TextView = view.findViewById(R.id.tv_custom_team_email)
    }
}