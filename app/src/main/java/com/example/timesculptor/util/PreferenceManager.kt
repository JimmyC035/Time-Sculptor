package com.example.timesculptor.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager {
    fun putBoolean(key: String?, value: Boolean) {
        mShare!!.edit().putBoolean(key, value).apply()
    }

    fun putInt(key: String?, value: Int) {
        mShare!!.edit().putInt(key, value).apply()
    }

    fun getBoolean(key: String?): Boolean {
        return mShare!!.getBoolean(key, false)
    }

    fun getInt(key: String?): Int {
        return mShare!!.getInt(key, 0)
    }

    fun getUninstallSettings(key: String?): Boolean {
        return mShare!!.getBoolean(key, true)
    }

    fun getSystemSettings(key: String?): Boolean {
        return mShare!!.getBoolean(key, true)
    }

    fun putString(key: String?, value: String?) {
        mShare!!.edit().putString(key, value).apply()
    }

    fun getString(key: String?): String? {
        return mShare!!.getString(key, "")
    }


    companion object {
        const val PREF_SETTINGS_HIDE_SYSTEM_APPS = "hide_system_apps"
        const val PREF_SETTINGS_HIDE_UNINSTALL_APPS = "hide_uninstall_apps"
        const val PREF_LIST_SORT = "sort_list"
//        const val FCM_ID = "fcm_id"
        private const val PREF_NAME = "preference_application"
        private var mManager: PreferenceManager? = null
        private var mShare: SharedPreferences? = null
        fun init(context: Context) {
            mManager = PreferenceManager()
            mShare = context.applicationContext.getSharedPreferences(PREF_NAME, 0)
        }

        fun getInstance(): PreferenceManager? {
            return mManager
        }
    }
}