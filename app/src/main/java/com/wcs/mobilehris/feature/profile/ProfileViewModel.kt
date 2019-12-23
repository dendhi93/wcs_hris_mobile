package com.wcs.mobilehris.feature.profile

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.util.Preference

class ProfileViewModel(private var _context : Context) : ViewModel() {
    val stProfileName = ObservableField<String>("")
    val stProfilePhone = ObservableField<String>("")
    val stProfileMail = ObservableField<String>("")
    private val preference = Preference(_context)

    fun loadProfile(){
        stProfileName.set(preference.getUn().trim())
        stProfilePhone.set("0878900679")
        stProfileMail.set("admin-id@id.wilmar-intl.com")
    }
}