package com.wcs.mobilehris.feature.dtlnotification

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class DtlNotificationViewModel :ViewModel(){
    val stCreateDate = ObservableField<String>("")
    val stTitleContent = ObservableField<String>("")
    val stDtlContent = ObservableField<String>("")

    fun loadDtl(createDate : String, title :String, content : String){
        stCreateDate.set(createDate.trim())
        stTitleContent.set(title.trim())
        stDtlContent.set(content.trim())
    }

}