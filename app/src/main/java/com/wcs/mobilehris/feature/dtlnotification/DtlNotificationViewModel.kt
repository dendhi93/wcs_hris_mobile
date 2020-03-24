package com.wcs.mobilehris.feature.dtlnotification

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.feature.menu.MenuActivity

class DtlNotificationViewModel(private val context : Context) :ViewModel(){
    val stCreateDate = ObservableField("")
    val stTitleContent = ObservableField("")
    val stDtlContent = ObservableField("")

    fun loadDtl(createDate : String, title :String, content : String){
        stCreateDate.set(createDate.trim())
        stTitleContent.set(title.trim())
        stDtlContent.set(content.trim())
    }

    fun onBackDtlNotification(){
        val intent = Intent(context, MenuActivity::class.java)
        intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_NOTIFICATION)
        context.startActivity(intent)
    }

}