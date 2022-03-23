package com.example.taskmaker.ui.detail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.taskmaker.R
import com.example.taskmaker.model.Task
import com.example.taskmaker.notification.TaskReminder
import com.example.taskmaker.ui.ViewModelFactory
import com.example.taskmaker.ui.main.MainViewModel
import com.example.taskmaker.ui.main.TaskTitleView
import com.example.taskmaker.ui.main.TaskTitleView.Companion.DONE
import com.example.taskmaker.util.DateConverter
import com.example.taskmaker.util.TASK_ID
import java.text.DateFormat
import java.time.Month
import java.util.*

class DetailTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var viewModel: DetailTaskViewModel

    private lateinit var taskTitleView: TaskTitleView
    private lateinit var priorityImageView: ImageView
    private lateinit var dueDateTextView: TextView
    private var selectedTask: Task? = null
    private val calendar by lazy { Calendar.getInstance() }
    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeViews()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailTaskViewModel::class.java]

         val taskId = intent.extras?.getInt(TASK_ID)
        if (taskId != null) {
            viewModel.setTaskId(taskId)
        }

        viewModel.task.observe(this) { task ->

            if (task != null) {
                selectedTask =  task

                taskTitleView.text = task.taskTitle
                if (task.isCompleted) {
                    taskTitleView.state = DONE
                }

                dueDateTextView.text = getString(
                    R.string.task_detail_due_date,
                    DateConverter.convertMillisToString(task.dueDateMillis)
                )

                priorityImageView.setImageResource(if (task.isPriority) R.drawable.ic_priority_yes else R.drawable.ic_priority_no)
            }
        }


    }

    private fun initializeViews() {
        taskTitleView = findViewById(R.id.task_detail_title)
        priorityImageView = findViewById(R.id.detail_task_priority)
        dueDateTextView = findViewById(R.id.task_detail_due_date)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_screen_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

       return when(item.itemId) {
            R.id.action_set_alarm -> {

                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                    DatePickerDialog(this, this, year, month,day)
                datePickerDialog.show()
                true
            }

            R.id.action_delete_task -> {
                viewModel.deleteTask()
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, this, hour, minute, true)
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute

        val taskReminderTime = calendar.apply {
            set(Calendar.YEAR, myYear )
            set(Calendar.MONTH, myMonth )
            set(Calendar.DAY_OF_MONTH, myDay )
            set(Calendar.HOUR, myHour)
            set(Calendar.MINUTE, myMinute)
        }.timeInMillis

        setTaskReminder(this, selectedTask, taskReminderTime)
    }

    private fun setTaskReminder(context: Context, task: Task?, reminderTime: Long ) {
        val taskReminder = TaskReminder()
        if (task != null) {
            taskReminder.setTaskReminder(context, task, reminderTime)
            Toast.makeText(
                this,
                getString(R.string.reminder_set),
                Toast.LENGTH_LONG
            ).show()
        }

    }

}