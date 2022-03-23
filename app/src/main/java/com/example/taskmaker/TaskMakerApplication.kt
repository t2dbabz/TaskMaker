package com.example.taskmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.preference.PreferenceManager
import androidx.work.*
import com.example.taskmaker.worker.DailyReminderWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TaskMakerApplication(): Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

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

        delayedInit()

    }

    private fun delayedInit() {
        applicationScope.launch {
            setupDailyTaskReminder()
        }
    }

    private fun setupDailyTaskReminder() {

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)


        workManager.enqueueUniquePeriodicWork(
            DailyReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}