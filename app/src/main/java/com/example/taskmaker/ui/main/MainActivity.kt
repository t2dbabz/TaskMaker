package com.example.taskmaker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaker.R
import com.example.taskmaker.ui.ViewModelFactory
import com.example.taskmaker.ui.add.AddTaskActivity
import com.example.taskmaker.ui.detail.DetailTaskActivity
import com.example.taskmaker.ui.settings.SettingsActivity
import com.example.taskmaker.util.TasksFilter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private  val adapter by lazy {
        TasksAdapter {task ->
            viewModel.updateTask(task)
        }
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        recyclerView = findViewById(R.id.task_list_recycler_view)
        floatingActionButton = findViewById(R.id.add_task_fab)

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        viewModel.tasks.observe(this) { pagingData ->
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)

            adapter.submitList(pagingData)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {

            R.id.action_filter_task -> {
                showFilterTaskPopUpMenu()
                true
            }

            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)

        }

    }

    private fun showFilterTaskPopUpMenu() {
        val view = findViewById<View>(R.id.action_filter_task) ?: return
        PopupMenu(this, view).run {
            inflate(R.menu.filter_task_menu)

            setOnMenuItemClickListener{item: MenuItem ->
                when(item.itemId) {
                    R.id.active -> viewModel.updateFilter(TasksFilter.ACTIVE_TASKS)
                    R.id.completed -> viewModel.updateFilter(TasksFilter.COMPLETED_TASKS)
                    else -> viewModel.updateFilter(TasksFilter.ALL_TASKS)
                }

                true
            }

            show()
        }
    }


}