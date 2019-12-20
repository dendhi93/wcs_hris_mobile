package com.wcs.mobilehris.util

import android.content.Context
import android.content.SharedPreferences

class Preference(private var _context : Context){
    private val sharedPreferences: SharedPreferences =
        _context.getSharedPreferences(_context.packageName + "_pref", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor
    private val TAG = "Preferences"
    private val KEY_USERNAME = "usernama"

    init {
        editor = sharedPreferences.edit()
    }

    fun saveUn(un: String) {
        editor.putString(KEY_USERNAME, un)
        editor.apply()
    }
    fun getUn(): String {
        return sharedPreferences.getString(KEY_USERNAME, "").toString()
    }

    fun clearPreference() {
        editor.clear()
        editor.apply()
    }
}