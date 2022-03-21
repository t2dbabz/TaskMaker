package com.example.taskmaker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaker.R
import com.example.taskmaker.ui.ViewModelFactory
import com.example.taskmaker.ui.add.AddTaskActivity
import com.example.taskmaker.ui.detail.DetailTaskActivity
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


}