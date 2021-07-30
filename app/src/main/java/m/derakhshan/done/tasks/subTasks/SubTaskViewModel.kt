package m.derakhshan.done.tasks.subTasks


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aminography.primecalendar.persian.PersianCalendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import m.derakhshan.done.Arrange
import m.derakhshan.done.database.TasksDatabase
import m.derakhshan.done.database.models.SubTasksModel
import m.derakhshan.done.database.models.TasksModel
import m.derakhshan.done.tasks.subTasks.alarm.AlarmService


class SubTaskViewModel(
    private val taskID: Long,
    val database: TasksDatabase,
    private val alarmService: AlarmService
) : ViewModel() {

    private lateinit var task: TasksModel


    val title = MutableLiveData<String>()
    val dayOfWeek = MutableLiveData<String>()
    val fullDate = MutableLiveData<String>()
    val subTasks = MutableLiveData<List<SubTasksModel>>()
    val reminder = MutableLiveData<String>()

    private lateinit var persianDateArray: List<String>

    init {
        viewModelScope.launch(Dispatchers.Default) {

            task = database.tasksDAO.getTask(taskID)

            reminder.postValue(task.reminder ?: "")
            title.postValue(task.taskName)
            persianDateArray = task.date.keys.first().toString().split("/")

            dayOfWeek.postValue(task.date.values.first())
            fullDate.postValue(Arrange().persianConverter(task.date.keys.first().toString()))

            subTasks.postValue(database.subTasksDAO.getSubTasks(taskID))
        }
    }


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

    fun deleteReminder() = viewModelScope.launch(Dispatchers.Default) {
        reminder.postValue("")
        database.tasksDAO.updateReminder("", taskID)
        alarmService.cancelAlarm(reqCode = taskID, msg = task.taskName, title = "یادآوری انجام کار")
    }


    fun setAlarm(hour: String, minute: String) {

        val myDate = PersianCalendar()
        myDate.timeInMillis = System.currentTimeMillis()
        myDate.clear()
        myDate.set(
            year = persianDateArray[0].toInt(),
            month = persianDateArray[1].toInt() - 1,
            dayOfMonth = persianDateArray[2].toInt(),
            hourOfDay = hour.toInt(),
            minute = minute.toInt()
        )

        alarmService.setAlarm(
            timeInMillisecond = myDate.timeInMillis,
            title = "یادآوری انجام کار",
            msg = task.taskName,
            reqCode = taskID
        )
        viewModelScope.launch(Dispatchers.Default) {
            reminder.postValue("$hour:$minute")
            database.tasksDAO.updateReminder("$hour:$minute", taskID)
        }

    }
}