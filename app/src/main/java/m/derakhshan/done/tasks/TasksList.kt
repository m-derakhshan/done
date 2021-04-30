package m.derakhshan.done.tasks

import m.derakhshan.done.database.models.TasksModel


abstract class TasksList {
    companion object {
        const val dateType = 0
        const val taskType = 1
    }

    abstract fun getType(): Int
    abstract val id: String
    override fun equals(other: Any?): Boolean {
        return (other as TasksList).getType() == this.getType() &&
                (other).id == this.id
    }
}

class TaskItem : TasksList() {
    lateinit var task: TasksModel
    override val id: String
        get() = task.id.toString()

    override fun getType(): Int {
        return taskType
    }
}

class DateItem : TasksList() {
    lateinit var date: Map<String, String>
    override val id: String
        get() = date.hashCode().toString()

    override fun getType(): Int {
        return dateType
    }
}