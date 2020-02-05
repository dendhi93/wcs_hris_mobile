package com.wcs.mobilehris.feature.profile

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.util.Preference

class ProfileViewModel(private var _context : Context) : ViewModel() {
    val stProfileName = ObservableField<String>("")
    val stProfilePhone = ObservableField<String>("")
    val stProfileMail = ObservableField<String>("")

    fun loadProfile(name : String, phone : String, email : String){
        stProfileName.set(name.trim())
        stProfilePhone.set(phone.trim())
        stProfileMail.set(email.trim())
    }


}