package m.derakhshan.done.database.convertor

import androidx.room.TypeConverter
import m.derakhshan.done.database.models.TaskStatus


class EnumConverters {
    @TypeConverter
    fun toTaskStatus(status: Int): TaskStatus =
        when (status) {
            0 -> TaskStatus.DONE
            else -> TaskStatus.IN_PROGRESS
        }

    @TypeConverter
    fun fromTaskStatus(status: TaskStatus): Int =
        when (status) {
            TaskStatus.DONE -> 0
            else -> 1
        }

}