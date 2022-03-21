package com.example.taskmaker.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebSettings
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.taskmaker.R
import com.example.taskmaker.model.Task
import com.example.taskmaker.ui.ViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText

class AddTaskActivity : AppCompatActivity() {
    private lateinit var viewModel: AddTaskViewModel
    private lateinit var taskTitle: TextInputEditText
    private lateinit var prioritySwitch: SwitchMaterial
    private lateinit var dueDateTextView: TextView
    private  var dueDateInMillis: Long? = null
    var isPriorityChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[AddTaskViewModel::class.java]

        initializeViews()




    }

    private fun initializeViews() {
        taskTitle = findViewById(R.id.task_title_editText)
        prioritySwitch = findViewById(R.id.priority_switch)
        dueDateTextView = findViewById(R.id.due_date_textView)

        prioritySwitch.setOnCheckedChangeListener { _, isChecked ->
            isPriorityChecked = isChecked
        }
    }

    private fun addNewTask() {
        if (!taskTitle.text.isNullOrBlank()){
            val title = taskTitle.text.toString().trim()

           val dueDate = if (dueDateInMillis != null ) dueDateInMillis else System.currentTimeMillis()

            val task = Task(taskTitle = title, isPriority = isPriorityChecked, dueDateMillis = dueDate!!)

            viewModel.addTask(task)

            finish()
        } else {
            Toast.makeText(this, "Task title is Empty", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_task_screen_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_save_task -> {
                addNewTask()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }
}