package com.wcs.mobilehris.feature.team.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.FragmentTeamBinding
import com.wcs.mobilehris.feature.profile.ProfileActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import java.util.*
import kotlin.collections.ArrayList

class TeamFragment : Fragment(),
    TeamInterface,
    SelectedTeamInterface {
    private lateinit var fragmentTeamBinding: FragmentTeamBinding
    private var arrTeamList = ArrayList<TeamModel>()
    private lateinit var teamAdapter: CustomTeamAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTeamBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_team, container, false)
        fragmentTeamBinding.viewModel =
            TeamViewModel(
                requireContext(),
                this
            )
        return  fragmentTeamBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentTeamBinding.rcTeam.layoutManager = LinearLayoutManager(requireContext())
        fragmentTeamBinding.rcTeam.setHasFixedSize(true)
        teamAdapter = CustomTeamAdapter(
            requireContext(),
            arrTeamList
        )
        teamAdapter.initSelectedCallBack(this)
        fragmentTeamBinding.rcTeam.adapter = teamAdapter
        fragmentTeamBinding.viewModel?.initDataTeam(ConstantObject.vLoadWithProgressBar)
        fragmentTeamBinding.swTeam.setOnRefreshListener {
            fragmentTeamBinding.viewModel?.initDataTeam(ConstantObject.vLoadWithoutProgressBar)
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
        val arrListFilteredTeam: ArrayList<TeamModel> = ArrayList()
        val arrListTeamList : ArrayList<TeamModel> = arrTeamList
        hideUI(ConstantObject.vGlobalUI)
        showUI(ConstantObject.vRecylerViewUI)
        when{
            arrListTeamList.isNotEmpty() -> {
                for(itemTeam in arrListTeamList) {
                    if (itemTeam.name.toLowerCase(Locale.getDefault()).contains(textFilter.toLowerCase(Locale.getDefault()))
                        || itemTeam.email.toLowerCase(Locale.getDefault()).contains(textFilter.toLowerCase(Locale.getDefault()))
                        || itemTeam.phone.toLowerCase(Locale.getDefault()).contains(textFilter.toLowerCase(Locale.getDefault()))){
                        arrListFilteredTeam.add(itemTeam)
                    }
                }
                teamAdapter.filterListTeam(arrListFilteredTeam)
            }
        }

    }

    override fun onLoadTeam(teamList: List<TeamModel>, typeLoading: Int) {
        arrTeamList.clear()
        arrTeamList.addAll(teamList)
        when(typeLoading){
            ConstantObject.vLoadWithProgressBar -> hideUI(ConstantObject.vProgresBarUI)
            else -> {
                teamAdapter.notifyDataSetChanged()
                hideUI(ConstantObject.vGlobalUI)
                showUI(ConstantObject.vRecylerViewUI)
                onHideSwipeRefresh()
            }
        }
    }

    override fun onErrorMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(requireContext(), message, ConstantObject.vToastError)
            else -> MessageUtils.toastMessage(requireContext(), message, ConstantObject.vToastInfo)
        }
    }

    override fun onAlertTeam(alertMessage: String, alertTitle: String, intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_TEAM_NO_CONNECTION -> {MessageUtils.alertDialogDismiss(alertMessage, alertTitle, requireContext())}
        }
    }

    override fun onHideSwipeRefresh() { fragmentTeamBinding.swTeam.isRefreshing = false }

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

    override fun selectedItemTeam(teamModel: TeamModel) {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        intent.putExtra(ConstantObject.extra_intent, ConstantObject.extra_fromIntentTeam)
        intent.putExtra(extraTeamName, teamModel.name)
        intent.putExtra(extraTeamPhone, teamModel.phone)
        intent.putExtra(extraTeamEmail, teamModel.email)
        startActivity(intent)
    }

    companion object{
        const val ALERT_TEAM_NO_CONNECTION = 1
        const val extraTeamName = "team_name"
        const val extraTeamPhone = "team_phone"
        const val extraTeamEmail = "team_email"
    }
}


