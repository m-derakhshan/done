package m.derakhshan.done.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import m.derakhshan.done.database.models.TaskStatus
import m.derakhshan.done.database.models.TasksModel


@Dao
interface TasksDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(task: TasksModel)

    @Query("SELECT * FROM Tasks WHERE status = :status order by  id ASC")
    fun getTodayTask(status: TaskStatus): LiveData<List<TasksModel>>

    @Query("SELECT * FROM Tasks order by status DESC, id ASC")
    fun getAllTasks(): LiveData<List<TasksModel>>

    @Query("UPDATE Tasks SET status = :status WHERE id = :id")
    fun updateStatus(status: TaskStatus, id: Int)

    @Delete
    fun delete(task: TasksModel)

    @Query("DELETE from Tasks")
    fun deleteAll()


}