package com.example.timesculptor.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import com.example.timesculptor.R
import java.util.Calendar
import java.util.Locale

object AppUtil {

    private const val A_DAY = (86400 * 1000).toLong()
    fun parsePackageName(pckManager: PackageManager, data: String): String {
        val applicationInformation: ApplicationInfo? =
            try {
                pckManager.getApplicationInfo(data, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
                Log.i("session name","$e")
            null
        }
        return if (applicationInformation != null) {
            pckManager.getApplicationLabel(applicationInformation).toString()
        } else {
            data
        }
    }

    fun getPackageIcon(context: Context, packageName: String): Drawable {
        val manager = context.packageManager
        try {
            return manager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return context.resources.getDrawable(R.drawable.ic_android_black_24dp)
    }

    fun formatMilliSeconds(milliSeconds: Long): String {
        val second = milliSeconds / 1000L
        return if (second < 60) {
            String.format("%ss", second)
        } else if (second < 60 * 60) {
            String.format("%sm %ss", second / 60, second % 60)
        } else {
            String.format(
                "%sh %sm %ss",
                second / 3600,
                second % 3600 / 60,
                second % 3600 % 60
            )
        }
    }

    fun isSystemApp(manager: PackageManager, packageName: String): Boolean {
        var isSystemApp = false
        try {
            val applicationInfo = manager.getApplicationInfo(packageName, 0)
            isSystemApp = (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                    && (applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return isSystemApp
    }

    fun isInstalled(packageManager: PackageManager, packageName: String): Boolean {
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return applicationInfo != null
    }

    fun openable(packageManager: PackageManager, packageName: String?): Boolean {
        return packageManager.getLaunchIntentForPackage(packageName!!) != null
    }

    fun getAppUid(packageManager: PackageManager, packageName: String): Int {
        val applicationInfo: ApplicationInfo
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            return applicationInfo.uid
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    fun getTimeRange(sort: SortEnum?): LongArray {
        val range: LongArray = when (sort) {
            SortEnum.TODAY -> getTodayRange()
            SortEnum.YESTERDAY -> getYesterday()
            SortEnum.THIS_WEEK -> getThisWeek()
            SortEnum.THIS_MONTH -> getThisMonth()
            SortEnum.THIS_YEAR -> getThisYear()

            else -> getTodayRange()
        }
        Log.d("**********", range[0].toString() + " ~ " + range[1])
        return range
    }

    private fun getTodayRange(): LongArray {
        val timeNow = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return longArrayOf(cal.timeInMillis, timeNow)
    }

     fun getTillNow(latest:Long): LongArray {
        val timeNow = System.currentTimeMillis()
        return longArrayOf(latest, timeNow)
    }

    fun getYesterdayTimestamp(): Long {
        val timeNow = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeNow - A_DAY
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.timeInMillis
    }

    private fun getYesterday(): LongArray {
        val timeNow = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeNow - A_DAY
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        val start = cal.timeInMillis
        val end = if (start + A_DAY > timeNow) timeNow else start + A_DAY
        return longArrayOf(start, end)
    }

     fun getThisWeek(): LongArray {
        val timeNow = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        val start = cal.timeInMillis
        val end = if (start + A_DAY > timeNow) timeNow else start + A_DAY
        return longArrayOf(start, end)
    }

    private fun getThisMonth(): LongArray {
        val timeNow = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_MONTH] = 1
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return longArrayOf(cal.timeInMillis, timeNow)
    }

    private fun getThisYear(): LongArray {
        val timeNow = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = Calendar.getInstance()[Calendar.YEAR]
        cal[Calendar.MONTH] = Calendar.JANUARY
        cal[Calendar.DAY_OF_MONTH] = 1
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return longArrayOf(cal.timeInMillis, timeNow)
    }

    fun humanReadableByteCount(bytes: Long): String {
        val unit = 1024
        if (bytes < unit) return "$bytes B"
        val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
        val pre = "KMGTPE"[exp - 1].toString() + ""
        return String.format(
            Locale.getDefault(),
            "%.1f %sB",
            bytes / Math.pow(unit.toDouble(), exp.toDouble()),
            pre
        )
    }
    fun Long.toHoursMinutesSeconds(): String {
        val totalSeconds = this / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        val timeString = when {
            hours > 0 -> String.format("%02dh %02dm %02ds", hours, minutes, seconds)
            minutes > 0 -> String.format("%02dm %02ds", minutes, seconds)
            else -> String.format("%02ds", seconds)
        }
        return timeString
    }


    fun Long.toHoursMinutes(): String {
        val totalSeconds = this / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        val timeString = when {
            hours > 0 -> String.format("%02dh %02dm", hours, minutes)
            minutes > 0 -> String.format("%02dm", minutes)
            else -> String.format("%02ds", seconds)
        }
        return timeString
    }

    fun Long.toMinutesSeconds(): String {
        val totalSeconds = this / 1000
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d : %02d", minutes, seconds)
    }

    fun Int.toMonthAbbreviation(): String {
        return when (this) {
            0 -> "JAN"
            1 -> "FEB"
            2 -> "MAR"
            3 -> "APR"
            4 -> "MAY"
            5 -> "JUN"
            6 -> "JUL"
            7 -> "AUG"
            8 -> "SEP"
            9 -> "OCT"
            10 -> "NOV"
            11 -> "DEC"
            else -> throw IllegalArgumentException("Invalid month value: $this. Must be between 0 and 11.")
        }
    }





}