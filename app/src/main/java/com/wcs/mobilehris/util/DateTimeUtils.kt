@file:Suppress("DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)

package com.wcs.mobilehris.util

import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object DateTimeUtils {

    fun getCurrentTime(): String { return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()) }

    fun getCurrentDate(): String { return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    fun getChangeTimeFormat(selectedTime : String) : String{
        return try{
            val timeSource = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)
            val finalTimeSource = timeSource.parse(selectedTime.trim())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            timeFormat.format(finalTimeSource)
        }catch (e : Exception){""}
    }
    fun getChangeDateFormat(dateTime: String, dateFormatType : Int): String? {
        return try {
            val source = when(dateFormatType){
                ConstantObject.dateTimeFormat_1 -> SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
                ConstantObject.dateTimeFormat_2 -> SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                else -> SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            }

            val dateSource = source.parse(dateTime.trim())
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
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
            val longDiff : Long = timeStop.hours.toLong() - timeStart.hours.toLong()

            return longDiff.toInt()
        }catch (e : java.lang.Exception){0}
    }

    fun getDifferentDate(startDate : String, endDate : String) : Int{
        return try{
            val sdf = SimpleDateFormat("yyy-MM-dd", Locale.getDefault())
            val date1 = sdf.parse(startDate)
            val date2 = sdf.parse(endDate)
            val diff: Long = date2.time - date1.time
            val diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            diffDays.toInt()
        }catch (e : Exception){ 0 }
    }

    fun getAdvancedDate(tenorDate : Int) : String?{
        return try {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, tenorDate)
            val formatedDate = SimpleDateFormat("yyy-MM-dd", Locale.getDefault())
            formatedDate.format(calendar.time).toString()
        }catch (e : java.lang.Exception){ "err $e" }
    }
}