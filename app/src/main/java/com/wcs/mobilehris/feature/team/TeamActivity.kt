package com.wcs.mobilehris.feature.team

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentTeamBinding
import com.wcs.mobilehris.feature.createtask.CreateTaskActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import java.util.*
import kotlin.collections.ArrayList

class TeamActivity : AppCompatActivity(), TeamInterface, SelectedTeamInterface {
    private lateinit var fragmentTeamBinding: FragmentTeamBinding
    private var arrTeamList = ArrayList<TeamModel>()
    private lateinit var teamAdapter: CustomTeamAdapter
    private var backIntent : Intent? = null

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
        fragmentTeamBinding.viewModel?.initDataTeam(ConstantObject.loadWithProgressBar)
        fragmentTeamBinding.swTeam.setOnRefreshListener {
            fragmentTeamBinding.viewModel?.initDataTeam(ConstantObject.loadWithoutProgressBar)
        }
        searchTeam()
    }

    private fun searchTeam(){
        fragmentTeamBinding.svTeam.queryHint = "Search Team"
        fragmentTeamBinding.svTeam.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTeamName(newText.toString().trim())
                return true
            }

        })
    }

    private fun filterTeamName(textFilter : String){
        val arrListfilteredTeam: ArrayList<TeamModel> = ArrayList()
        val arrListTeamList : ArrayList<TeamModel> = arrTeamList
        when{
            arrListTeamList.isNotEmpty() -> {
                for(itemTeam in arrListTeamList) {
                    if (itemTeam.name.toLowerCase(Locale.getDefault()).contains(textFilter.toLowerCase(
                            Locale.getDefault()))
                        || itemTeam.email.toLowerCase(Locale.getDefault()).contains(textFilter.toLowerCase(
                            Locale.getDefault()))
                        || itemTeam.phone.toLowerCase(Locale.getDefault()).contains(textFilter.toLowerCase(
                            Locale.getDefault()))){
                        arrListfilteredTeam.add(itemTeam)
                    }
                }
                teamAdapter.filterListTeam(arrListfilteredTeam)
            }
        }
    }

    override fun onLoadTeam(teamList: List<TeamModel>, typeLoading: Int) {
        arrTeamList.clear()
        for(i in teamList.indices){
            arrTeamList.add(
                TeamModel(teamList[i].userId,teamList[i].name ,
                    teamList[i].phone,
                    teamList[i].email))
        }
        teamAdapter.notifyDataSetChanged()
        hideUI(ConstantObject.vGlobalUI)
        showUI(ConstantObject.vRecylerViewUI)
        when(typeLoading){
            ConstantObject.loadWithProgressBar -> hideUI(ConstantObject.vProgresBarUI)
            else -> onHideSwipeRefresh()
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

    companion object{
        const val ALERT_TEAM_NO_CONNECTION = 1
    }

    override fun selectedItemTeam(teamModel: TeamModel) {
        //todo back to activity create task
        backIntent = Intent(this, CreateTaskActivity::class.java)
        backIntent?.putExtra(CreateTaskActivity.RESULT_EXTRA_TEAM_NAME, teamModel.name)
        backIntent?.putExtra(CreateTaskActivity.RESULT_EXTRA_TEAM_USER_ID, teamModel.userId)
        setResult(CreateTaskActivity.RESULT_SUCCESS_CODE, backIntent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
