package com.wcs.mobilehris.feature.dtlnotification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityDetailNotificationBinding
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils

class DetailNotificationActivity : AppCompatActivity() {

    private lateinit var dtlNotificationBinding : ActivityDetailNotificationBinding
    private var intentCreateDate : String? = ""
    private var intentTitle : String? = ""
    private var intentContent : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dtlNotificationBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail_notification)
        dtlNotificationBinding.viewModel = DtlNotificationViewModel()
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
        }
        intentCreateDate = intent?.getStringExtra(INTENT_DATE).toString()
        intentTitle = intent?.getStringExtra(INTENT_TITLE).toString()
        intentContent = intent?.getStringExtra(INTENT_CONTENT).toString()

        when{
            intentCreateDate != "" || intentTitle != "" || intentContent != "" -> dtlNotificationBinding.viewModel?.loadDtl(
                intentCreateDate!!, intentTitle!!, intentContent)
            else -> MessageUtils.toastMessage(this, "Missing Data, please contact your admin", ConstantObject.vToastError)
         }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object{
        const val INTENT_DATE = "extra_date"
        const val INTENT_TITLE = "extra_title"
        const val INTENT_CONTENT = "extra_content"
    }

}
