package com.wcs.mobilehris.feature.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityProfileBinding
import com.wcs.mobilehris.feature.team.fragment.TeamFragment
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.Preference


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ProfileActivity : AppCompatActivity() {
    private lateinit var profileBinding : ActivityProfileBinding
    private lateinit var intentFrom : String
    private var intentTeamName : String? = null
    private var intentTeamPhone : String? = null
    private var intentTeamEmail : String? = null
    private lateinit var preference : Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        profileBinding.viewModel = ProfileViewModel(this)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        preference = Preference(this)
        intentFrom = intent.getStringExtra(ConstantObject.extra_intent)
        when(intentFrom.trim()){
            ConstantObject.extra_fromIntentProfile -> {
                profileBinding
                    .viewModel?.loadProfile(preference.getName(),
                    preference.getPhone(), preference.getEmail())
            }
            else -> {
                intentTeamName = intent.getStringExtra(TeamFragment.extraTeamName)
                intentTeamPhone = intent.getStringExtra(TeamFragment.extraTeamPhone)
                intentTeamEmail = intent.getStringExtra(TeamFragment.extraTeamEmail)
                when{
                    !intentTeamName.isNullOrEmpty() &&
                            !intentTeamEmail.isNullOrEmpty() &&
                            !intentTeamPhone.isNullOrEmpty() -> profileBinding
                        .viewModel?.loadProfile(intentTeamName.toString().trim(),
                        intentTeamPhone.toString().trim(), intentTeamEmail.toString().trim())
                }

            }
        }

    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> finish()
//            else -> return super.onOptionsItemSelected(item)
//        }
//        return true
//    }
}
