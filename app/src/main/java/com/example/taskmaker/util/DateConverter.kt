package com.example.taskmaker.util

import java.text.SimpleDateFormat
import java.util.*

object DateConverter {
    fun convertMillisToString(timeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
        return sdf.format(calendar.time)
    }
}