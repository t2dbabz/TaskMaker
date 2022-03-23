package com.example.taskmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.preference.PreferenceManager
import java.util.*

class TaskMakerApplication(): Application() {

    override fun onCreate() {
        super.onCreate()

        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferences.getString(
            getString(R.string.pref_key_theme),
            getString(R.string.pref_theme_auto)
        )?.apply {
            when(this) {
                "on" -> setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "off" -> setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                else ->setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
             }
        }
    }
}