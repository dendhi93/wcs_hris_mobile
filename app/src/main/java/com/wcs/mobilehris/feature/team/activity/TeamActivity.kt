package com.wcs.mobilehris.feature.team.activity

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
import com.wcs.mobilehris.databinding.TeamProjectActivityBinding
import com.wcs.mobilehris.feature.createtask.CreateTaskActivity
import com.wcs.mobilehris.feature.requesttravel.RequestTravelActivity
import com.wcs.mobilehris.feature.team.fragment.*
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TeamActivity : AppCompatActivity(),
    TeamProjectInterface,
    SelectedTeamInterface.SelectedTeamProjectInterface {
    private lateinit var teamProjectActivityBinding: TeamProjectActivityBinding
    private var arrTeamList = ArrayList<TeamProjectModel>()
    private lateinit var teamAdapter: CustomTeamProjectAdapter
    private var backIntent : Intent? = null
    private lateinit var intentFrom : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamProjectActivityBinding = DataBindingUtil.setContentView(this, R.layout.team_project_activity)
        teamProjectActivityBinding.viewModel = TeamProjectViewModel(this, this)
    }

    override fun onStart() {
        super.onStart()
        teamProjectActivityBinding.rcTeamProject.layoutManager = LinearLayoutManager(this)
        teamProjectActivityBinding.rcTeamProject.setHasFixedSize(true)
        teamAdapter = CustomTeamProjectAdapter(
            this,
            arrTeamList
        )
        teamAdapter.initSelectedCallBack(this)
        teamProjectActivityBinding.rcTeamProject.adapter = teamAdapter
        teamProjectActivityBinding.viewModel?.initDataTeamProject()
        intentFrom = intent.getStringExtra(ConstantObject.extra_intent).toString()
    }

    override fun onLoadTeamProject(teamList: List<TeamProjectModel>) {
        arrTeamList.clear()
        arrTeamList.addAll(teamList)
        teamAdapter.notifyDataSetChanged()
        teamProjectActivityBinding.viewModel?.isProgressVisibleTeam?.set(false)
        teamProjectActivityBinding.viewModel?.isVisibleRecylerView?.set(false)
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            else -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
        }
    }

    override fun onAlertTeamProject(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_TEAM_NO_CONNECTION -> {
                MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this@TeamActivity)}
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
        val arrListFilteredTeam: ArrayList<TeamProjectModel> = ArrayList()
        val arrListTeamList : ArrayList<TeamProjectModel> = arrTeamList
        teamProjectActivityBinding.viewModel?.isVisibleRecylerView?.set(true)
        when{
            arrListTeamList.isNotEmpty() -> {
                for(itemTeam in arrListTeamList) {
                    if (itemTeam.nameTeam.toLowerCase(Locale.getDefault()).contains(textFilter.toLowerCase(
                            Locale.getDefault()))){
                        arrListFilteredTeam.add(itemTeam)
                    }
                }
                teamAdapter.filterListTeamProject(arrListFilteredTeam)
            }
        }
    }

    companion object{
        const val ALERT_TEAM_NO_CONNECTION = 1
    }

    override fun selectedItemNameProject(teamProjectModel: TeamProjectModel) {
        //back to activity create task / request travel
        when(intentFrom){
            ConstantObject.extra_fromIntentCreateTask -> {
                backIntent = Intent(this, CreateTaskActivity::class.java)
                backIntent?.putExtra(CreateTaskActivity.RESULT_EXTRA_TEAM_NAME, teamProjectModel.nameTeam.trim())
                backIntent?.putExtra(CreateTaskActivity.RESULT_EXTRA_TEAM_USER_ID, teamProjectModel.teamProjectId.trim())
                backIntent?.putExtra(CreateTaskActivity.RESULT_EXTRA_TEAM_STATUS, teamProjectModel.statusTeam.trim())
                setResult(CreateTaskActivity.RESULT_SUCCESS_CODE, backIntent)
                finish()
            }
            ConstantObject.extra_fromIntentCreateTravel -> {
                backIntent = Intent(this, RequestTravelActivity::class.java)
                backIntent?.putExtra(RequestTravelActivity.RESULT_EXTRA_TRAVEL_TEAM_NAME, teamProjectModel.nameTeam)
                backIntent?.putExtra(RequestTravelActivity.RESULT_EXTRA_TRAVEL_TEAM_USER_ID, teamProjectModel.teamProjectId)
                backIntent?.putExtra(RequestTravelActivity.RESULT_EXTRA_TRAVEL_STATUS_TEAM, teamProjectModel.statusTeam)
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
