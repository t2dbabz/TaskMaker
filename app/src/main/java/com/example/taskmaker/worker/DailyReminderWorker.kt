package com.example.taskmaker.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.taskmaker.R
import com.example.taskmaker.model.Task
import com.example.taskmaker.notification.TaskReminder
import com.example.taskmaker.repository.TaskRepository
import com.example.taskmaker.ui.detail.DetailTaskActivity
import com.example.taskmaker.ui.main.MainActivity
import kotlinx.coroutines.delay

class DailyReminderWorker(context: Context, workerParams: WorkerParameters): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {

        val tasks = TaskRepository.getInstance(applicationContext).getNearestActiveTask()

        delay(1000L)

        if (tasks.isEmpty()) {
          return  Result.retry()
        } else {
            createNotificationChannel(applicationContext)
            showDailyReminderNotification(tasks)
        }




        return Result.success()
    }


    private fun showDailyReminderNotification( tasks: List<Task>) {

        val notificationStyle = NotificationCompat.InboxStyle()
        notificationStyle.setBigContentTitle("Recent Active Tasks")
            .setSummaryText("Daily Reminder")
        if (tasks.isNotEmpty()) {
            tasks.forEach { task ->
                notificationStyle.addLine(task.taskTitle)
            }
        } else {
            return
        }


        val intent = Intent(applicationContext, MainActivity::class.java)


        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            TaskReminder.TASK_REMINDER_NOTIF_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        with(NotificationManagerCompat.from(applicationContext)) {
            val build = NotificationCompat.Builder(applicationContext, TaskReminder.NOTIFICATION_CHANNEL_ID)
                .setStyle(notificationStyle)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)

            notify(NOTIFICATION_ID, build.build())

        }

    }

    private fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val NOTIFICATION_ID = 24
        const val NOTIFICATION_CHANNEL_ID = "daily_reminder_id"
        const val NOTIFICATION_CHANNEL_NAME = "Daily Task Reminder"
        const val WORK_NAME = "daily_reminder_work"
    }
}