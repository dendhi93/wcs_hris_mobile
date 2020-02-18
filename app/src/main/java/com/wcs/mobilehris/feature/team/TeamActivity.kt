package com.wcs.mobilehris.feature.team

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentTeamBinding
import com.wcs.mobilehris.feature.createtask.CreateTaskActivity
import com.wcs.mobilehris.feature.requesttravel.RequestTravelActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TeamActivity : AppCompatActivity(), TeamInterface, SelectedTeamInterface {
    private lateinit var fragmentTeamBinding: FragmentTeamBinding
    private var arrTeamList = ArrayList<TeamModel>()
    private lateinit var teamAdapter: CustomTeamAdapter
    private var backIntent : Intent? = null
    private lateinit var intentFrom : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentTeamBinding = DataBindingUtil.setContentView(this, R.layout.fragment_team)
        fragmentTeamBinding.viewModel = TeamViewModel(this, this)
    }

    override fun onStart() {
        super.onStart()
        fragmentTeamBinding.rcTeam.layoutManager = LinearLayoutManager(this)
        fragmentTeamBinding.rcTeam.setHasFixedSize(true)
        teamAdapter = CustomTeamAdapter(this, arrTeamList)
        teamAdapter.initSelectedCallBack(this)
        fragmentTeamBinding.rcTeam.adapter = teamAdapter
        fragmentTeamBinding.viewModel?.initDataTeam(ConstantObject.vLoadWithProgressBar)
        fragmentTeamBinding.swTeam.setOnRefreshListener {
            fragmentTeamBinding.viewModel?.initDataTeam(ConstantObject.vLoadWithoutProgressBar)
        }
        fragmentTeamBinding.viewModel?.isHiddenSearch?.set(true)
        intentFrom = intent.getStringExtra(ConstantObject.extra_intent).toString()
    }

    override fun onLoadTeam(teamList: List<TeamModel>, typeLoading: Int) {
        arrTeamList.clear()
        arrTeamList.addAll(teamList)
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> hideUI(ConstantObject.vProgresBarUI)
            else -> {
                onHideSwipeRefresh()
                teamAdapter.notifyDataSetChanged()
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

    override fun onAlertTeam(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_TEAM_NO_CONNECTION -> {
                MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this@TeamActivity)}
        }
    }

    override fun onHideSwipeRefresh() {
        fragmentTeamBinding.swTeam.isRefreshing = false
    }

    override fun hideUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> fragmentTeamBinding.pbTeam.visibility = View.GONE
            ConstantObject.vRecylerViewUI -> fragmentTeamBinding.rcTeam.visibility = View.GONE
            ConstantObject.vGlobalUI -> fragmentTeamBinding.tvTeamEmpty.visibility = View.GONE
        }
    }

    override fun showUI(typeUI: Int) {
        when(typeUI){
            ConstantObject.vProgresBarUI -> fragmentTeamBinding.pbTeam.visibility = View.VISIBLE
            ConstantObject.vRecylerViewUI -> fragmentTeamBinding.rcTeam.visibility = View.VISIBLE
            ConstantObject.vGlobalUI -> fragmentTeamBinding.tvTeamEmpty.visibility = View.VISIBLE
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
                filterTeamName(newText.toString().trim())
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun filterTeamName(textFilter : String){
        val arrListFilteredTeam: ArrayList<TeamModel> = ArrayList()
        val arrListTeamList : ArrayList<TeamModel> = arrTeamList
        hideUI(ConstantObject.vGlobalUI)
        showUI(ConstantObject.vRecylerViewUI)
        when{
            arrListTeamList.isNotEmpty() -> {
                for(itemTeam in arrListTeamList) {
                    if (itemTeam.name.toLowerCase(Locale.getDefault()).contains(textFilter.toLowerCase(
                            Locale.getDefault()))
                        || itemTeam.email.toLowerCase(Locale.getDefault()).contains(textFilter.toLowerCase(
                            Locale.getDefault()))
                        || itemTeam.phone.toLowerCase(Locale.getDefault()).contains(textFilter.toLowerCase(
                            Locale.getDefault()))){
                        arrListFilteredTeam.add(itemTeam)
                    }
                }
                teamAdapter.filterListTeam(arrListFilteredTeam)
            }
        }
    }

    companion object{
        const val ALERT_TEAM_NO_CONNECTION = 1
    }

    override fun selectedItemTeam(teamModel: TeamModel) {
        //back to activity create task / request travel
        when(intentFrom){
            ConstantObject.extra_fromIntentCreateTask -> {
                backIntent = Intent(this, CreateTaskActivity::class.java)
                backIntent?.putExtra(CreateTaskActivity.RESULT_EXTRA_TEAM_NAME, teamModel.name)
                backIntent?.putExtra(CreateTaskActivity.RESULT_EXTRA_TEAM_USER_ID, teamModel.userId)
                setResult(CreateTaskActivity.RESULT_SUCCESS_CODE, backIntent)
                finish()
            }
            ConstantObject.extra_fromIntentCreateTravel -> {
                backIntent = Intent(this, RequestTravelActivity::class.java)
                backIntent?.putExtra(RequestTravelActivity.RESULT_EXTRA_TRAVEL_TEAM_NAME, teamModel.name)
                backIntent?.putExtra(RequestTravelActivity.RESULT_EXTRA__TRAVEL_TEAM_USER_ID, teamModel.userId)
                setResult(RequestTravelActivity.RESULT_SUCCESS_CODE_TEAM, backIntent)
                finish()
            }
        }
        hideCreateTaskKeyboard()
    }

    private fun hideCreateTaskKeyboard(){
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideCreateTaskKeyboard()
        finish()
    }
}
