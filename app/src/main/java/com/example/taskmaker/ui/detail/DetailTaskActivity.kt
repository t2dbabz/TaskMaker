package com.example.taskmaker.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.ViewModelProvider
import com.example.taskmaker.R
import com.example.taskmaker.ui.ViewModelFactory
import com.example.taskmaker.ui.main.MainViewModel

class DetailTaskActivity : AppCompatActivity() {
    private lateinit var viewModel: DetailTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailTaskViewModel::class.java]

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_screen_menu, menu)
        return true
    }




}