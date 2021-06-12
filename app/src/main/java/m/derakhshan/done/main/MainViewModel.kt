package m.derakhshan.done.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aminography.primecalendar.persian.PersianCalendar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import m.derakhshan.done.database.TasksDatabase
import m.derakhshan.done.database.models.TaskStatus
import m.derakhshan.done.database.models.TasksModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class MainViewModel @Inject constructor(private val database: TasksDatabase) : ViewModel() {


    private val job = Job()
    private val scope = Dispatchers.Default + job
    private val timeData = MutableLiveData<TimeModel>()
    private var checkGreeting = true
    private val today = PersianCalendar()
    val greeting = MutableLiveData<String>()

    val todayTasks: LiveData<List<TasksModel>>
        get() = database.tasksDAO.getTodayTask(HashMap<String, String>().apply {
            this[today.shortDateString] = today.weekDayName
        })

    val allTasks: LiveData<List<TasksModel>>
        get() = database.tasksDAO.getAllTasks()

    fun deleteTask(task: TasksModel) {
        viewModelScope.launch(Dispatchers.Default) {
            database.tasksDAO.delete(task = task)
        }
    }

    fun updateStatus(task: TasksModel) {
        viewModelScope.launch(Dispatchers.Default) {
            database.tasksDAO.updateStatus(
                id = task.id,
                status = when (task.status) {
                    TaskStatus.IN_PROGRESS -> TaskStatus.DONE
                    else -> TaskStatus.IN_PROGRESS
                }
            )
        }
    }


    fun restoreTask(task: TasksModel) {
        viewModelScope.launch(Dispatchers.Default) {
            database.tasksDAO.add(task = task)
        }
    }

    fun initClock(): LiveData<TimeModel> {
        CoroutineScope(scope).launch {
            withContext(Dispatchers.Default, block = {
                while (true) {
                    val time = Calendar.getInstance()
                    timeData.postValue(
                        TimeModel(
                            second = time.get(Calendar.SECOND),
                            minute = time.get(Calendar.MINUTE),
                            hour = time.get(Calendar.HOUR_OF_DAY)
                        )
                    )
                    delay(1000)
                }
            })
        }
        return timeData
    }

    fun greeting(time: TimeModel) {
        if (!checkGreeting)
            return
        greeting.value =
            when {
                time.hour < 5 -> "نیمه شب بخیر"
                time.hour < 12 -> "صبح بخیر"
                time.hour < 16 -> "ظهر بخیر"
                time.hour < 20 -> "عصر بخیر"
                else -> "شب بخیر"
            }
        this.checkGreeting = false
    }

    fun clearSubTasksOfDeletedTask(items: ArrayList<Long>) {
        viewModelScope.launch(Dispatchers.Default) {
            for (item in items)
                database.subTasksDAO.deleteAllSubTasks(item)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}