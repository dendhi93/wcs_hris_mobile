@file:Suppress("DEPRECATION")

package com.wcs.mobilehris.util

import android.util.Log
import java.sql.Time
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

    fun getDifferentHours(startTime : String, endTime : String) : Int{
        return try{
            val startTimeHour = startTime.split(":")[0].toInt()
            val startTimeMinutes = startTime.split(":")[1].toInt()
            val startTimeSeconds = startTime.split(":")[2].toInt()

            val endTimeHour = endTime.split(":")[0].toInt()
            val endTimeMinutes = endTime.split(":")[1].toInt()
            val endTimeSeconds = endTime.split(":")[2].toInt()

            val timeStart = Time(startTimeHour, startTimeMinutes, startTimeSeconds)
            val timeStop = Time(endTimeHour, endTimeMinutes, endTimeSeconds)
            Log.d("###", "timeStart " + timeStart.hours + " ~ timeStop " + timeStop.hours)
            val longDiff : Long = timeStop.hours.toLong() - timeStart.hours.toLong()

            return longDiff.toInt()
        }catch (e : java.lang.Exception){0}
    }
}