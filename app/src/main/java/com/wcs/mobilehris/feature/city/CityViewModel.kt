package com.wcs.mobilehris.feature.city

import android.content.Context
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.util.ConstantObject

class CityViewModel (private val context : Context, private val cityInterface: CityInterface) : ViewModel(){

    fun initCity(typeOfLoading : Int){
        when{
            !ConnectionObject.isNetworkAvailable(context) ->
                cityInterface.onAlertCity(context.getString(R.string.alert_no_connection),
                    ConstantObject.vAlertDialogNoConnection, CityActivity.ALERT_CITY_NO_CONNECTION)
            else -> getDataCity(typeOfLoading)
        }
    }

    private fun getDataCity(typeLoading : Int){
//        when(typeLoading){
//            ConstantObject.loadWithProgressBar -> cityInterface.showUI(ConstantObject.vProgresBarUI)
//        }
        cityInterface.hideUI(ConstantObject.vRecylerViewUI)
        cityInterface.showUI(ConstantObject.vGlobalUI)
        val listCity = mutableListOf<CityModel>()
        var cityModel = CityModel("01", "Jakarta", "01", "Indonesia")
        listCity.add(cityModel)
        cityModel = CityModel("02", "Medan", "01", "Indonesia")
        listCity.add(cityModel)
        cityModel = CityModel("03", "Bandung", "01", "Indonesia")
        listCity.add(cityModel)
        cityModel = CityModel("04", "Solo", "01", "Indonesia")
        listCity.add(cityModel)
        cityModel = CityModel("05", "Kalimantan", "01", "Indonesia")
        listCity.add(cityModel)
        cityModel = CityModel("06", "Penang", "02", "Malaysia")
        listCity.add(cityModel)
        cityModel = CityModel("07", "Orchad", "03", "Singapura")
        listCity.add(cityModel)
        cityModel = CityModel("08", "Johanesburg", "01", "Afrika Selatan")
        listCity.add(cityModel)

        when{
            listCity.size > 0 -> {
                Handler().postDelayed({
                    cityInterface.onLoadCity(listCity, typeLoading)
                },2000)
            }
            else -> {
                cityInterface.showUI(ConstantObject.vGlobalUI)
                cityInterface.hideUI(ConstantObject.vRecylerViewUI)
                when(typeLoading){
                    ConstantObject.vLoadWithProgressBar -> cityInterface.hideUI(ConstantObject.vProgresBarUI)
                    else -> cityInterface.onHideSwipeRefreshCity()
                }
                cityInterface.onErrorMessage(context.getString(R.string.no_data_found), ConstantObject.vToastInfo)
            }
        }
    }

}