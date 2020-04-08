package com.wcs.mobilehris.util

import android.content.Context
import android.content.SharedPreferences

class Preference(private var _context : Context){
    private val sharedPreferences: SharedPreferences =
        _context.getSharedPreferences(_context.packageName + "_pref", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor
    private val keyUsername = "username"
    private val keYProfileName = "name"
    private val keyPhoneProfile = "phone"
    private val keyEmailProfile = "email"
    private val keyApiToken = "token"
    private val keyFirebaseToken = "firebase_token"

    init {
        editor = sharedPreferences.edit()
    }

    fun saveUn(un: String, name : String, phone : String, email : String, unToken : String) {
        editor.putString(keyUsername, un)
        editor.putString(keYProfileName, name)
        editor.putString(keyPhoneProfile, phone)
        editor.putString(keyEmailProfile, email)
        editor.putString(keyApiToken, unToken)
        editor.apply()
    }

    fun saveFirebaseToken(fToken : String){
        editor.putString(keyFirebaseToken, fToken)
        editor.apply()
    }

    fun getUn(): String { return sharedPreferences.getString(keyUsername, "").toString() }
    fun getName(): String { return sharedPreferences.getString(keYProfileName, "").toString() }
    fun getPhone(): String { return sharedPreferences.getString(keyPhoneProfile, "").toString() }
    fun getEmail(): String { return sharedPreferences.getString(keyEmailProfile, "").toString() }
    fun getApiToken(): String { return sharedPreferences.getString(keyApiToken, "").toString() }
    fun getFirebaseToken(): String { return sharedPreferences.getString(keyFirebaseToken, "").toString() }

    fun clearPreference() {
        editor.clear()
        editor.apply()
    }
}