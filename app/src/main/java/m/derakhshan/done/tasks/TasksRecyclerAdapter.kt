package m.derakhshan.done.tasks


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.persian.PersianCalendar
import m.derakhshan.done.R
import m.derakhshan.done.database.models.TaskStatus
import m.derakhshan.done.databinding.DateItemModelBinding
import m.derakhshan.done.databinding.TasksItemModelBinding
import m.derakhshan.done.tasks.TasksList.Companion.taskType


class TasksRecyclerAdapter(private val today: PersianCalendar) :
    ListAdapter<TasksList, TasksRecyclerAdapter.ViewHolder>
        (object : DiffUtil.ItemCallback<TasksList>() {
        override fun areItemsTheSame(oldItem: TasksList, newItem: TasksList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TasksList, newItem: TasksList): Boolean {
            return if ((oldItem is TaskItem) && (newItem is TaskItem))
                (oldItem.task.id == newItem.task.id)
                        && (oldItem.task.status == newItem.task.status)
                        && (oldItem.task.taskName == newItem.task.taskName)
            else newItem == oldItem
        }
    }
    ) {

    lateinit var clickListener: TaskClickListener
    fun getItemModel(position: Int): TaskItem = (getItem(position) as TaskItem)

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getType()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == taskType) {
            val binding = DataBindingUtil.inflate<TasksItemModelBinding>(
                inflater,
                R.layout.tasks_item_model,
                parent,
                false
            )
            ViewHolder(taskView = binding, dateView = null)
        } else {
            val binding = DataBindingUtil.inflate<DateItemModelBinding>(
                inflater,
                R.layout.date_item_model,
                parent,
                false
            )
            ViewHolder(taskView = null, dateView = binding)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItem(position).getType() == taskType)
            holder.bind(getItem(position) as TaskItem)
        else
            holder.bind(getItem(position) as DateItem)
    }


    inner class ViewHolder(
        private val taskView: TasksItemModelBinding?,
        private val dateView: DateItemModelBinding?
    ) :
        RecyclerView.ViewHolder(taskView?.root ?: dateView!!.root) {
        fun bind(taskModel: TaskItem) {

            taskView?.let { myView ->
                val model = taskModel.task

                //-------------------------(adding click listener for going to subTasks)-----------------------//
                myView.frameRoot.setOnClickListener {
                    clickListener.onTaskClick(model)
                }

                myView.taskName.text = model.taskName
                myView.frameRoot.alpha = if (model.status == TaskStatus.DONE) 0.3F else 1F

                myView.taskName.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        if (model.date.keys.first() < today.shortDateString && model.status == TaskStatus.IN_PROGRESS) R.color.red else R.color.black
                    )
                )
            }
        }

        fun bind(dateModel: DateItem) {
            dateView?.let { myView ->
                myView.dateName.text = dateModel.date.keys.first()
                myView.weekName.text = dateModel.date.values.first()
            }
        }
    }
}