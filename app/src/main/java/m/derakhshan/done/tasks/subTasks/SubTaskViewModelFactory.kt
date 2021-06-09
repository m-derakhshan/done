package m.derakhshan.done.tasks.subTasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import m.derakhshan.done.database.TasksDatabase


class SubTaskViewModelFactory(private val taskID: Int, val database: TasksDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Int::class.java, TasksDatabase::class.java)
            .newInstance(taskID, database)
    }
}