package com.wcs.mobilehris.util

import android.content.Context
import android.content.SharedPreferences

class Preference(private var _context : Context){
    private val sharedPreferences: SharedPreferences =
        _context.getSharedPreferences(_context.packageName + "_pref", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor
    private val TAG = "Preferences"
    private val KEY_USERNAME = "username"
    private val KEY_NAMEPROFILE = "name"
    private val KEY_PHONEPROFILE = "phone"
    private val KEY_EMAILPROFILE = "email"
    private val KEY_TOKEN = "token"

    init {
        editor = sharedPreferences.edit()
    }

    fun saveUn(un: String, name : String, phone : String, email : String, unToken : String) {
        editor.putString(KEY_USERNAME, un)
        editor.putString(KEY_NAMEPROFILE, name)
        editor.putString(KEY_PHONEPROFILE, phone)
        editor.putString(KEY_EMAILPROFILE, email)
        editor.putString(KEY_TOKEN, unToken)
        editor.apply()
    }

    fun getUn(): String { return sharedPreferences.getString(KEY_USERNAME, "").toString() }
    fun getName(): String { return sharedPreferences.getString(KEY_NAMEPROFILE, "").toString() }
    fun getPhone(): String { return sharedPreferences.getString(KEY_PHONEPROFILE, "").toString() }
    fun getEmail(): String { return sharedPreferences.getString(KEY_EMAILPROFILE, "").toString() }
    fun getToken(): String { return sharedPreferences.getString(KEY_EMAILPROFILE, "").toString() }

    fun clearPreference() {
        editor.clear()
        editor.apply()
    }
}