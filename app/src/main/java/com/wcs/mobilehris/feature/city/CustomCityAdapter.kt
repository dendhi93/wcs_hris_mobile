package com.wcs.mobilehris.feature.city

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wcs.mobilehris.R

class CustomCityAdapter (private val context : Context, private var cityMutableList : MutableList<CityModel>):
    RecyclerView.Adapter<CustomCityAdapter.ViewHolder>(){
    private lateinit var selectedCityInterface: SelectedCityInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_global_list,parent,false))
    }

    override fun getItemCount(): Int = cityMutableList.size

    fun initSelectedCityCallback(itemCallBack : SelectedCityInterface){
        this.selectedCityInterface = itemCallBack
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : CityModel = cityMutableList[position]
        holder.imgCustom.visibility = View.GONE
        holder.imgVIconIsConflick.visibility = View.GONE
        holder.tvCity.text = model.cityDescription.trim()
        holder.tvCountry.text = model.countryDescription.trim()
        holder.cvCustomCity.setOnClickListener {
            selectedCityInterface?.selectedItemCity(model)
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cvCustomCity : CardView = view.findViewById(R.id.cv_custom)
        var tvCity : TextView = view.findViewById(R.id.tv_custom)
        var imgCustom : ImageView = view.findViewById(R.id.imgV_custom)
        var tvCountry : TextView = view.findViewById(R.id.tv_custom_content)
        var imgVIconIsConflick : ImageView = view.findViewById(R.id.imgV_custom_isConflict)
    }

    fun filterListCities(filterListCity : ArrayList<CityModel>){
        cityMutableList = filterListCity
        notifyDataSetChanged()
    }

}