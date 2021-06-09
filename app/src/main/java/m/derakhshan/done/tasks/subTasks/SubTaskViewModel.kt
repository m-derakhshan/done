package m.derakhshan.done.tasks.subTasks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import m.derakhshan.done.Arrange
import m.derakhshan.done.database.TasksDatabase
import m.derakhshan.done.database.models.SubTasksModel
import m.derakhshan.done.database.models.TasksModel

class SubTaskViewModel(private val taskID: Int, val database: TasksDatabase) : ViewModel() {

    private lateinit var task: TasksModel
    private val day = MutableLiveData<String>()

    val title = MutableLiveData<String>()
    val month = MutableLiveData<String>()
    val year = MutableLiveData<String>()
    val subTasks = MutableLiveData<List<SubTasksModel>>()

    private val _taskUpdated = MutableLiveData<Boolean>()
    val taskUpdated: LiveData<Boolean>
        get() = _taskUpdated


    init {
        viewModelScope.launch(Dispatchers.Default) {
            task = database.tasksDAO.getTask(taskID)
            title.postValue(task.taskName)
            val date = task.date.keys.first().toString().split("/")
            day.postValue(date[2])
            month.postValue(Arrange().getMonthName(date[1].toInt()))
            year.postValue(Arrange().persianConverter(date[0]))
            subTasks.postValue(database.subTasksDAO.getSubTasks(taskID))
        }
    }

    fun setDay(): LiveData<String> = day

    fun endOfFragmentLifeCycle(taskName: String, subTasks: String) {
        viewModelScope.launch(Dispatchers.Default) {

            val task1 = async(Dispatchers.Default) { updateInfo(taskName) }
            val task2 = async(Dispatchers.Default) { updateSubTasks(subTasks) }
            task1.await()
            task2.await()
        }
    }

    private fun updateInfo(taskName: String) {
        database.tasksDAO.updateTaskTitle(id = taskID, taskName = taskName)
    }

    private fun updateSubTasks(subTasks: String) {
        val tasks = ArrayList<SubTasksModel>()
        val info = subTasks.split("\n")
        for (taskIndex in info.indices) {
            if (info[taskIndex].isNotEmpty())
                tasks.add(
                    SubTasksModel(
                        id = taskID + taskIndex,
                        taskID = taskID,
                        subTask = info[taskIndex]
                    )
                )
        }
        database.subTasksDAO.deleteAllSubTasks(taskID)
        if (tasks.isNotEmpty())
            database.subTasksDAO.addSubTask(tasks)
    }
}