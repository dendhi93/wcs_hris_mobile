package com.wcs.mobilehris.feature.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityProfileBinding


class ProfileActivity : AppCompatActivity() {
    private lateinit var profileBinding : ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        profileBinding.viewModel = ProfileViewModel(this)
        supportActionBar?.hide()
        profileBinding.viewModel?.loadProfile()
//        supportActionBar?.let {
//            it.setDisplayHomeAsUpEnabled(true)
//            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
//        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> finish()
//            else -> return super.onOptionsItemSelected(item)
//        }
//        return true
//    }
}
