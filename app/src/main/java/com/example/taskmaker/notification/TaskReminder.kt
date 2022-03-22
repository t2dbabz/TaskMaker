package com.example.taskmaker.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.taskmaker.R
import com.example.taskmaker.model.Task
import com.example.taskmaker.ui.detail.DetailTaskActivity
import com.example.taskmaker.util.DateConverter
import com.example.taskmaker.util.TASK_DUE_DATE
import com.example.taskmaker.util.TASK_ID
import com.example.taskmaker.util.TASK_TITLE

class TaskReminder(): BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("TASK REMINDER", "Task Reminder onReceive called")
        val bundle = intent?.extras

        val taskId = bundle?.getInt(TASK_ID)
        val taskTitle = bundle?.getString(TASK_TITLE)
        val taskDueDateInMillis = bundle?.getLong(TASK_DUE_DATE)

        createNotificationChannel(context)

        if (taskTitle != null && taskId != null && taskDueDateInMillis != null) {
            notifyTaskReminderNotification(context, taskId, taskTitle, taskDueDateInMillis)
        }
    }


    fun setTaskReminder(context: Context, task: Task, reminderTime: Long) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,TaskReminder::class.java)
        val extras = Bundle()
        extras.putInt(TASK_ID, task.id)
        extras.putString(TASK_TITLE, task.taskTitle)
        extras.putLong(TASK_DUE_DATE, task.dueDateMillis)
        intent.putExtras(extras)

        val pendingIntent = PendingIntent.getBroadcast(context, TASK_REMINDER_ID, intent, 0)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent )

        Log.d("TASK REMINDER", "alarm set")
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

    private fun notifyTaskReminderNotification(context: Context, taskId: Int, taskTitle: String, taskDueDate: Long) {

        val intent = Intent(context, DetailTaskActivity::class.java)
        intent.putExtra(TASK_ID, taskId)

        val dueDate = DateConverter.convertMillisToString(taskDueDate)

        val contentPendingIntent = PendingIntent.getActivity(
            context,
            TASK_REMINDER_NOTIF_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(taskTitle)
                .setContentText(context.getString(R.string.task_detail_due_date, dueDate))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)

            notify(NOTIFICATION_ID, build.build())

        }

    }

    companion object {
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "1000"
        const val NOTIFICATION_CHANNEL_NAME = "Task Reminder Channel"
        const val TASK_REMINDER_ID = 121
        const val TASK_REMINDER_NOTIF_REQUEST_CODE = 21

    }
}