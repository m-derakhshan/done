package m.derakhshan.done.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import m.derakhshan.done.database.convertor.EnumConverters
import m.derakhshan.done.database.convertor.HashMapConverter
import m.derakhshan.done.database.dao.SubTaskDAO
import m.derakhshan.done.database.dao.TasksDAO
import m.derakhshan.done.database.models.SubTasksModel
import m.derakhshan.done.database.models.TasksModel


@Database(
    entities = [TasksModel::class, SubTasksModel::class],
    version = 5, exportSchema = false
)
@TypeConverters(EnumConverters::class, HashMapConverter::class)
abstract class TasksDatabase : RoomDatabase() {
    abstract val tasksDAO: TasksDAO
    abstract val subTasksDAO: SubTaskDAO
}