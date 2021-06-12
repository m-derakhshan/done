package m.derakhshan.done.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import m.derakhshan.done.database.models.SubTasksModel


@Dao
interface SubTaskDAO {

    @Query("SELECT * FROM SubTasks WHERE taskID=:taskID")
    fun getSubTasks(taskID: Long): List<SubTasksModel>

    @Insert
    fun addSubTask(subTasksModel: ArrayList<SubTasksModel>)

    @Query("DELETE FROM SubTasks WHERE taskID=:tasksID")
    fun deleteAllSubTasks(tasksID: Long)

}