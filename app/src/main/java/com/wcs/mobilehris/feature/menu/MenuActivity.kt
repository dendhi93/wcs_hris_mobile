package com.wcs.mobilehris.feature.menu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.databinding.ActivityMenuBinding
import com.wcs.mobilehris.feature.absent.AbsentFragment
import com.wcs.mobilehris.feature.profile.ProfileActivity
import com.wcs.mobilehris.feature.activity.ActivityFragment
import com.wcs.mobilehris.feature.approval.ApprovalFragment
import com.wcs.mobilehris.feature.confirmation.ConfirmationFragment
import com.wcs.mobilehris.feature.dashboard.DashboardFragment
import com.wcs.mobilehris.feature.notification.NotificationFragment
import com.wcs.mobilehris.feature.request.RequestFragment
import com.wcs.mobilehris.feature.setting.SettingFragment
import com.wcs.mobilehris.feature.team.fragment.TeamFragment
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import com.wcs.mobilehris.util.Preference
import com.wcs.mobilehris.utilinterface.DialogInterface
import org.json.JSONObject


@Suppress("DEPRECATION")
class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DialogInterface {
    private lateinit var menuBinding : ActivityMenuBinding
    private lateinit var tvHeaderMenu : TextView
    private lateinit var lnHeader : LinearLayout
    private lateinit var preference: Preference
    private var keyDialogActive = 0
    private var apiRepo = ApiRepo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuBinding = DataBindingUtil.setContentView(this, R.layout.activity_menu)
        val header = menuBinding.navView.inflateHeaderView(R.layout.nav_header_menu)
        tvHeaderMenu = header.findViewById(R.id.tv_nav_header_bottom)
        lnHeader = header.findViewById(R.id.ln_nav_header)
        setSupportActionBar(menuBinding.toolbar)
        supportActionBar?.let {
//            it.title = Html.fromHtml("<font color='#000000'>WcsHR System</font>")
            it.title = getString(R.string.app_name_label)
        }

        val toggle = ActionBarDrawerToggle(
            this, menuBinding.drawerLayout, menuBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        menuBinding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        menuBinding.navView.setNavigationItemSelectedListener(this)
        preference = Preference(this)

    }

    override fun onStart() {
        super.onStart()
        tvHeaderMenu.text = preference.getName().trim()
        lnHeader.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(ConstantObject.extra_intent, ConstantObject.extra_fromIntentProfile)
            startActivity(intent)
        }
        loadFragment()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        var vFragment : Fragment? = null
        when(menuItem.itemId){
            R.id.nav_dashboard -> {
                vFragment = DashboardFragment()
                supportActionBar?.let { it.subtitle = "Dashboard" }
            }
            R.id.nav_activity -> {
                vFragment = ActivityFragment()
                supportActionBar?.let { it.subtitle = "Activity" }
            }
            R.id.nav_request -> {
                vFragment = RequestFragment()
                supportActionBar?.let { it.subtitle = "Request" }
            }
            R.id.nav_approval -> {
                vFragment = ApprovalFragment()
                supportActionBar?.let { it.subtitle = "Approval" }
            }
            R.id.nav_confirmation -> {
                vFragment = ConfirmationFragment()
                supportActionBar?.let { it.subtitle = "Confirmation" }
            }
            R.id.nav_absent -> {
                vFragment = AbsentFragment()
                supportActionBar?.let { it.subtitle = "Absent" }
            }
            R.id.nav_notification -> {
                vFragment = NotificationFragment()
                supportActionBar?.let { it.subtitle = "Notification" }
            }
            R.id.nav_team -> {
                vFragment =
                    TeamFragment()
                supportActionBar?.let { it.subtitle = "Team" }
            }
            R.id.nav_setting -> {
                vFragment = SettingFragment()
                supportActionBar?.let { it.subtitle = "Setting" }
            }
            R.id.nav_logout -> {
                keyDialogActive = DIALOG_LOG_OUT
                MessageUtils.alertDialogOkCancel("Are you sure want to exit Apps ?", "Exit Apps", this, this)
            }
        }
        if (vFragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_nav_container, vFragment).commit()
        }
        menuBinding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private var isDoubleTab = false
    override fun onBackPressed() {
        if(menuBinding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            menuBinding.drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            if(isDoubleTab){
                super.onBackPressed()
                val startMain = Intent(Intent.ACTION_MAIN)
                startMain.addCategory(Intent.CATEGORY_HOME)
                startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(startMain)
                return
            }
            this.isDoubleTab = true
            MessageUtils.toastMessage(this, "Tap again to exit", ConstantObject.vToastInfo)
            Handler().postDelayed({ isDoubleTab = false }, 2000)
        }
    }

    //method for load fragment from another activity or another fragment
    private fun loadFragment(){
        val intToFragment = intent.getIntExtra(EXTRA_CALLER_ACTIVITY_FLAG, 0)
        val frTransaction = supportFragmentManager.beginTransaction()

        when(intToFragment){
            EXTRA_FLAG_DASHBOARD -> {
                supportActionBar?.let {
                    it.subtitle = "Dashboard"
                    frTransaction.replace(R.id.frame_nav_container, DashboardFragment())
                }
            }
            EXTRA_FLAG_ACTIVITY -> {
                supportActionBar?.let {
                    it.subtitle = "Activity"
                    frTransaction.replace(R.id.frame_nav_container,
                        ActivityFragment()
                    )
                }
            }
            EXTRA_FLAG_REQUEST -> {
                supportActionBar?.let {
                    it.subtitle = "Request"
                    frTransaction.replace(R.id.frame_nav_container, RequestFragment())
                }
            }
            EXTRA_FLAG_APPROVAL -> {
                supportActionBar?.let {
                    it.subtitle = "Approval"
                    frTransaction.replace(R.id.frame_nav_container, ApprovalFragment())
                }
            }
            EXTRA_FLAG_CONFIRMATION -> {
                supportActionBar?.let {
                    it.subtitle = "Confirmation"
                    frTransaction.replace(R.id.frame_nav_container, ConfirmationFragment())
                }
            }
            EXTRA_FLAG_ABSENT -> {
                supportActionBar?.let {
                    it.subtitle = "Absent"
                    frTransaction.replace(R.id.frame_nav_container, AbsentFragment())
                }
            }
            EXTRA_FLAG_NOTIFICATION -> {
                supportActionBar?.let {
                    it.subtitle = "Notification"
                    frTransaction.replace(R.id.frame_nav_container, NotificationFragment())
                }
            }
            EXTRA_FLAG_TEAM -> {
                supportActionBar?.let {
                    it.subtitle = "Team"
                    frTransaction.replace(R.id.frame_nav_container,
                        TeamFragment()
                    )
                }
            }
            EXTRA_FLAG_SETTING -> {
                supportActionBar?.let {
                    it.subtitle = "Setting"
                    frTransaction.replace(R.id.frame_nav_container, SettingFragment())
                }
            }
            EXTRA_FLAG_LOGOUT -> {
                keyDialogActive = DIALOG_LOG_OUT
               MessageUtils.alertDialogOkCancel("Are you sure want to exit Apps ?", ConstantObject.vAlertDialogConfirmation, this, this)
            }
        }
        frTransaction.commit()
        menuBinding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun getLogoutDevice(){
        MessageUtils.toastMessage(this@MenuActivity, "Please Wait ...",ConstantObject.vToastInfo)
        apiRepo.getLogout(preference.getUn(), this, object : ApiRepo.ApiCallback<JSONObject>{
            override fun onDataLoaded(data: JSONObject?) {
                data?.let {
                    val responseLogout = it.getString(ConstantObject.vResponseStatus)
                    if(responseLogout.contains(ConstantObject.vValueResponseSuccess)){
                        preference.clearPreference()
                        val startMain = Intent(Intent.ACTION_MAIN)
                        startMain.addCategory(Intent.CATEGORY_HOME)
                        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(startMain)
                    }
                }
            }

            override fun onDataError(error: String?) {
                MessageUtils.toastMessage(this@MenuActivity, ""+error.toString(),ConstantObject.vToastError)
            }
        })
    }

    override fun onNegativeClick(o: Any) {}

    override fun onPositiveClick(o: Any) {
        when(keyDialogActive){
            DIALOG_LOG_OUT -> getLogoutDevice()
        }
    }

    companion object{
        const val EXTRA_FLAG_DASHBOARD = 1
        const val EXTRA_FLAG_ACTIVITY = 2
        const val EXTRA_FLAG_REQUEST = 3
        const val EXTRA_FLAG_APPROVAL = 4
        const val EXTRA_FLAG_CONFIRMATION = 5
        const val EXTRA_FLAG_ABSENT = 6
        const val EXTRA_FLAG_NOTIFICATION = 7
        const val EXTRA_FLAG_TEAM= 8
        const val EXTRA_FLAG_SETTING = 9
        const val EXTRA_FLAG_LOGOUT = 10
        const val EXTRA_CALLER_ACTIVITY_FLAG = "hris_activity"
        const val DIALOG_LOG_OUT = 11
    }
}
