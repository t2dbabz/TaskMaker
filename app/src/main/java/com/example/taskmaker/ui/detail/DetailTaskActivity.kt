package com.example.taskmaker.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.taskmaker.R
import com.example.taskmaker.ui.ViewModelFactory
import com.example.taskmaker.ui.main.MainViewModel
import com.example.taskmaker.ui.main.TaskTitleView
import com.example.taskmaker.ui.main.TaskTitleView.Companion.DONE
import com.example.taskmaker.util.DateConverter
import com.example.taskmaker.util.TASK_ID

class DetailTaskActivity : AppCompatActivity() {
    private lateinit var viewModel: DetailTaskViewModel

    private lateinit var taskTitleView: TaskTitleView
    private lateinit var priorityImageView: ImageView
    private lateinit var dueDateTextView: TextView

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

}