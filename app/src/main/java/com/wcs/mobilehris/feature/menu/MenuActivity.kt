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
import com.wcs.mobilehris.databinding.ActivityMenuBinding
import com.wcs.mobilehris.feature.absent.AbsentFragment
import com.wcs.mobilehris.feature.profile.ProfileActivity
import com.wcs.mobilehris.feature.activity.ActivityFragment
import com.wcs.mobilehris.feature.approval.ApprovalFragment
import com.wcs.mobilehris.feature.confirmation.ConfirmationFragment
import com.wcs.mobilehris.feature.notification.NotificationFragment
import com.wcs.mobilehris.feature.request.RequestFragment
import com.wcs.mobilehris.feature.setting.SettingFragment
import com.wcs.mobilehris.feature.status.StatusFragment
import com.wcs.mobilehris.feature.team.TeamFragment
import com.wcs.mobilehris.utils.ConstantObject
import com.wcs.mobilehris.utils.MessageUtils
import com.wcs.mobilehris.utils.Preference
import com.wcs.mobilehris.utilsinterface.DialogInterface


@Suppress("DEPRECATION")
class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DialogInterface {
    private lateinit var menuBinding : ActivityMenuBinding
    private lateinit var tvHeaderMenu : TextView
    private lateinit var lnHeader : LinearLayout
    private lateinit var preference: Preference
    private var keyDialogActive = 0

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
        tvHeaderMenu.text = preference.getUn().trim()
        lnHeader.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        loadFragment()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        var vFragment : Fragment? = null
        when(menuItem.itemId){
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
            R.id.nav_status -> {
                vFragment = StatusFragment()
                supportActionBar?.let { it.subtitle = "Status" }
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
                vFragment = TeamFragment()
                supportActionBar?.let { it.subtitle = "Team" }
            }
            R.id.nav_setting -> {
                vFragment = SettingFragment()
                supportActionBar?.let { it.subtitle = "Setting" }
            }
            R.id.nav_logout -> {
                keyDialogActive = DIALOG_LOG_OUT
                MessageUtils.alertDialogOkCancel("Are you sure want to exit Apps ?", "Exit Apps", MenuActivity@this, this)
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
            EXTRA_FLAG_ACTIVITY -> {
                supportActionBar?.let {
                    it.subtitle = "Activity"
                    frTransaction.replace(R.id.frame_nav_container, ActivityFragment())
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
                    frTransaction.replace(R.id.frame_nav_container, RequestFragment())
                }
            }
            EXTRA_FLAG_CONFIRMATION -> {
                supportActionBar?.let {
                    it.subtitle = "Confirmation"
                    frTransaction.replace(R.id.frame_nav_container, ConfirmationFragment())
                }
            }
            EXTRA_FLAG_STATUS -> {
                supportActionBar?.let {
                    it.subtitle = "Status"
                    frTransaction.replace(R.id.frame_nav_container, StatusFragment())
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
                    frTransaction.replace(R.id.frame_nav_container, TeamFragment())
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
               MessageUtils.alertDialogOkCancel("Are you sure want to exit Apps ?", "Exit Apps", MenuActivity@this, this)
            }
        }
        frTransaction.commit()
        menuBinding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onNegativeClick(o: Any) {}

    override fun onPositiveClick(o: Any) {
        when(keyDialogActive){
            DIALOG_LOG_OUT -> {
                preference.clearPreference()
                finish()
            }
        }
    }


    companion object{
        const val EXTRA_FLAG_ACTIVITY = 1
        const val EXTRA_FLAG_REQUEST = 2
        const val EXTRA_FLAG_APPROVAL = 3
        const val EXTRA_FLAG_CONFIRMATION = 4
        const val EXTRA_FLAG_STATUS = 5
        const val EXTRA_FLAG_ABSENT = 6
        const val EXTRA_FLAG_NOTIFICATION = 7
        const val EXTRA_FLAG_TEAM= 8
        const val EXTRA_FLAG_SETTING = 9
        const val EXTRA_FLAG_LOGOUT = 10
        const val EXTRA_CALLER_ACTIVITY_FLAG = "hris_activity"
        const val DIALOG_LOG_OUT = 11
    }


}
