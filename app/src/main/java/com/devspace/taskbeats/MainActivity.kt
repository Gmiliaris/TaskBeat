package com.devspace.taskbeats

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaskBeatDataBase::class.java,
            "database-task-beat"
        ).build()
    }

    private val categoryDao by lazy {
        db.getCategoryDao()
    }

    private val taskDao by lazy {
        db.getTaskDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvCategory = findViewById<RecyclerView>(R.id.rv_categories)
        val rvTask = findViewById<RecyclerView>(R.id.rv_tasks)

        val taskAdapter = TaskListAdapter()
        val categoryAdapter = CategoryListAdapter()

        lifecycleScope.launch {
            getCategoriesFromDataBase(categoryAdapter)
            getTasksFromDataBase(taskAdapter)
        }

        categoryAdapter.setOnClickListener { selected ->
            /*val categoryTemp = categories.map { item ->
                when {
                    item.name == selected.name && !item.isSelected -> item.copy(isSelected = true)
                    item.name == selected.name && item.isSelected -> item.copy(isSelected = false)
                    else -> item
                }
            }*/

            /* val taskTemp =
                if (selected.name != "ALL") {
                    tasks.filter { it.category == selected.name }
                } else {
                    tasks
                }
            taskAdapter.submitList(taskTemp)

            categoryAdapter.submitList(categoryTemp)*/
        }

        rvCategory.adapter = categoryAdapter
        rvTask.adapter = taskAdapter
    }

    private suspend fun getCategoriesFromDataBase(adapter: CategoryListAdapter) {
        val categoriesFromDb: List<CategoryEntity> = withContext(Dispatchers.IO) {
            categoryDao.getAllCategories()
        }
        val categoriesUiData = categoriesFromDb.map {
            CategoryUiData(
                name = it.name,
                isSelected = it.isSelected
            )
        }
        adapter.submitList(categoriesUiData)
    }

    private suspend fun getTasksFromDataBase(adapter: TaskListAdapter) {
        val tasksFromDb: List<TaskEntity> = withContext(Dispatchers.IO) {
            taskDao.getAll()
        }

        val tasksUiData = tasksFromDb.map {
            TaskUiData(
                it.name,
                it.category
            )
        }
        adapter.submitList(tasksUiData)
    }
}
