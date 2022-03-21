package com.example.taskmaker.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaker.R
import com.example.taskmaker.model.Task
import com.example.taskmaker.ui.detail.DetailTaskActivity
import com.example.taskmaker.ui.main.TaskTitleView.Companion.DONE
import com.example.taskmaker.ui.main.TaskTitleView.Companion.NORMAL
import com.example.taskmaker.util.DateConverter
import com.example.taskmaker.util.TASK_ID

class TasksAdapter(
    private val onCheckBoxClicked: (Task, Boolean) -> Unit
): PagingDataAdapter<Task, TasksAdapter.TaskViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position) as Task
        holder.bind(task)

        when {
            task.isCompleted -> {
                //Completed
                holder.isCompleteCheckBox.isChecked = true
                holder.taskTitle.state = DONE

            }
            else -> {
                //Active
                holder.isCompleteCheckBox.isChecked = false
                holder.taskTitle.state = NORMAL
            }
        }
    }

    inner class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val taskTitle: TaskTitleView = itemView.findViewById(R.id.task_title)
        val isCompleteCheckBox: CheckBox = itemView.findViewById(R.id.checkBox)
        private val priority: ImageView = itemView.findViewById(R.id.priority)
        private val dueDate: TextView = itemView.findViewById(R.id.due_date)

        fun bind(task: Task) {
            taskTitle.text = task.taskTitle
            itemView.setOnClickListener {
                val detailIntent = Intent(itemView.context, DetailTaskActivity::class.java)
                detailIntent.putExtra(TASK_ID, task.id)
                itemView.context.startActivity(detailIntent)
            }
            dueDate.text = DateConverter.convertMillisToString(task.dueDateMillis)

            priority.setImageResource(if (task.isPriority) R.drawable.ic_priority_yes else R.drawable.ic_priority_no)

            isCompleteCheckBox.setOnClickListener {
                onCheckBoxClicked(task, !task.isCompleted)
            }

        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }

    }
}