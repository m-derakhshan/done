package m.derakhshan.done.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import m.derakhshan.done.database.models.TaskStatus
import m.derakhshan.done.database.models.TasksModel


@Dao
interface TasksDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(task: TasksModel)

    @Query("SELECT * FROM Tasks WHERE date <= :date and status = :status  order by  id ASC")
    fun getTodayTask(
        date: Map<String, String>,
        status: TaskStatus = TaskStatus.IN_PROGRESS
    ): LiveData<List<TasksModel>>

    @Query("SELECT * FROM Tasks WHERE id = :id")
    fun getTask(
        id: Long
    ): TasksModel

    @Query("UPDATE Tasks SET taskName=:taskName WHERE id = :id")
    fun updateTaskTitle(taskName: String, id: Long)

    @Query("SELECT * FROM Tasks order by status DESC , id ASC")
    fun getAllTasks(): LiveData<List<TasksModel>>

    @Query("UPDATE Tasks SET status = :status WHERE id = :id")
    fun updateStatus(status: TaskStatus, id: Long)

    @Delete
    fun delete(task: TasksModel)


}