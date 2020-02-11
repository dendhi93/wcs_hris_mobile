package com.wcs.mobilehris.feature.city

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityCityBinding
import com.wcs.mobilehris.feature.requesttravel.RequestTravelActivity
import com.wcs.mobilehris.feature.team.TeamActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import java.util.*
import kotlin.collections.ArrayList

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class CityActivity : AppCompatActivity(), CityInterface, SelectedCityInterface {
    private lateinit var activityCityBinding : ActivityCityBinding
    private var arrCity = ArrayList<CityModel>()
    private lateinit var cityAdapter : CustomCityAdapter
    private var backIntent : Intent? = null
    private lateinit var intentFrom : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCityBinding = DataBindingUtil.setContentView(this, R.layout.activity_city)
        activityCityBinding.viewModel = CityViewModel(this, this)
    }

    override fun onStart() {
        super.onStart()
        intentFrom = intent.getStringExtra(RequestTravelActivity.extra_city_intent).toString()
        activityCityBinding.rcCityList.layoutManager  = LinearLayoutManager(this)
        activityCityBinding.rcCityList.setHasFixedSize(true)
        cityAdapter = CustomCityAdapter(this, arrCity)
        cityAdapter.initSelectedCityCallback(this)
        activityCityBinding.rcCityList.adapter = cityAdapter
        activityCityBinding.viewModel?.initCity(ConstantObject.vLoadWithProgressBar)
        activityCityBinding.swCityList.setOnRefreshListener {
            activityCityBinding.viewModel?.initCity(ConstantObject.vLoadWithoutProgressBar)
        }
    }

    override fun onLoadCity(cityList: List<CityModel>, typeLoading: Int) {
        arrCity.clear()
        arrCity.addAll(cityList)
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> hideUI(ConstantObject.vProgresBarUI)
            else -> {
                onHideSwipeRefreshCity()
                cityAdapter.notifyDataSetChanged()
                hideUI(ConstantObject.vGlobalUI)
                showUI(ConstantObject.vRecylerViewUI)
            }
        }
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            else -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
        }
    }

    override fun onAlertCity(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            TeamActivity.ALERT_TEAM_NO_CONNECTION -> {
                MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this@CityActivity)}
        }
    }

    override fun onHideSwipeRefreshCity() { activityCityBinding.swCityList.isRefreshing = false }

    override fun hideUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> activityCityBinding.pbCityList.visibility = View.GONE
            ConstantObject.vRecylerViewUI -> activityCityBinding.rcCityList.visibility = View.GONE
            ConstantObject.vGlobalUI -> activityCityBinding.tvCityListEmpty.visibility = View.GONE
        }
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> activityCityBinding.pbCityList.visibility = View.VISIBLE
            ConstantObject.vRecylerViewUI -> activityCityBinding.rcCityList.visibility = View.VISIBLE
            ConstantObject.vGlobalUI -> activityCityBinding.tvCityListEmpty.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search_friend, menu)
        val item: MenuItem? = menu?.findItem(R.id.mnu_search_team)
        val searchView = MenuItemCompat.getActionView(item) as SearchView
        val searchIconId = searchView.context.resources
            .getIdentifier("android:id/search_button", null, null)
        val searchIcon: ImageView = searchView.findViewById<View>(searchIconId) as ImageView
        searchIcon.setImageResource(R.mipmap.ic_black_search_24)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCity(newText.toString().trim())
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun filterCity(textFilter : String){
        val arrListFilteredCity: ArrayList<CityModel> = ArrayList()
        val arrListCityList : ArrayList<CityModel> = arrCity
        hideUI(ConstantObject.vGlobalUI)
        showUI(ConstantObject.vRecylerViewUI)
        when{
            arrListCityList.isNotEmpty() -> {
                for(itemCity in arrListCityList) {
                    if (itemCity.cityDescription.toLowerCase(Locale.getDefault()).contains(textFilter.toLowerCase(
                            Locale.getDefault()))
                        || itemCity.countryDescription.toLowerCase(Locale.getDefault()).contains(textFilter.toLowerCase(
                            Locale.getDefault()))){
                        arrListFilteredCity.add(itemCity)
                    }
                }
                cityAdapter.filterListCities(arrListFilteredCity)
            }
        }
    }

    override fun selectedItemCity(cityModel: CityModel) {
        when(intentFrom){
            RequestTravelActivity.extra_city_intentDepart -> {
                backIntent = Intent(this, RequestTravelActivity::class.java)
                backIntent?.putExtra(RequestTravelActivity.RESULT_EXTRA_TRAVEL_CITY_CODE, cityModel.cityCode)
                backIntent?.putExtra(RequestTravelActivity.RESULT_EXTRA__TRAVEL_CITY_DESC, cityModel.cityDescription)
                backIntent?.putExtra(RequestTravelActivity.RESULT_EXTRA__TRAVEL_COUNTRY_CODE, cityModel.countryCode)
                setResult(RequestTravelActivity.RESULT_SUCCESS_DESTINATION_FROM, backIntent)
                finish()
            }
            RequestTravelActivity.extra_city_intentReturn -> {
                backIntent = Intent(this, RequestTravelActivity::class.java)
                backIntent?.putExtra(RequestTravelActivity.RESULT_EXTRA_TRAVEL_CITY_CODE, cityModel.cityCode)
                backIntent?.putExtra(RequestTravelActivity.RESULT_EXTRA__TRAVEL_CITY_DESC, cityModel.cityDescription)
                backIntent?.putExtra(RequestTravelActivity.RESULT_EXTRA__TRAVEL_COUNTRY_CODE, cityModel.countryCode)
                setResult(RequestTravelActivity.RESULT_SUCCESS_DESTINATION_INTO, backIntent)
                finish()
            }
        }
        onHidingCityKeyboard()
    }

    private fun onHidingCityKeyboard(){
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onBackPressed() {
        onHidingCityKeyboard()
        super.onBackPressed()
        finish()
    }

    companion object{
        const val ALERT_CITY_NO_CONNECTION = 1
    }
}
