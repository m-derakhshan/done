package m.derakhshan.done.tasks

import m.derakhshan.done.database.models.TasksModel

interface TaskClickListener {
    fun onTaskClick(taskItem: TasksModel)
}