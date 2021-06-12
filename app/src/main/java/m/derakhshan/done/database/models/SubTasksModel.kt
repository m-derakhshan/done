package m.derakhshan.done.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SubTasks")
data class SubTasksModel(

    @PrimaryKey
    @ColumnInfo
    val id: Long,

    @ColumnInfo
    val taskID: Long,

    @ColumnInfo
    val subTask: String
)