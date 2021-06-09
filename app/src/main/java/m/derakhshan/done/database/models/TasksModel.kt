package m.derakhshan.done.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Tasks")
data class TasksModel(
    @PrimaryKey
    @ColumnInfo
    val id: Int,

    @ColumnInfo
    val taskName: String,

    @ColumnInfo
    val date: Map<String, String>,

    @ColumnInfo
    val time: String?,

    @ColumnInfo
    val status: TaskStatus,
)


enum class TaskStatus {
    DONE,
    IN_PROGRESS
}

