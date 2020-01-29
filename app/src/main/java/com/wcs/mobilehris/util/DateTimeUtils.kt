package com.wcs.mobilehris.util

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    fun getCurrentTime(): String {
        return SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }

    fun getCurrentDate(): String {
        return SimpleDateFormat("yyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun getChangeDateFormat(dateTime: String?): String? {
        return try {
            val source = SimpleDateFormat("yyy-MM-dd, HH:mm:ss", Locale.getDefault())
            val dateSource = source.parse(dateTime.toString())
            val dateFormat = SimpleDateFormat("dd/MM/yyyy; HH:mm", Locale.getDefault())
            if (dateSource != null) {
                dateFormat.format(dateSource)
            } else { "" }
        } catch (e: Exception) { "" }
    }
}