package m.derakhshan.done.main.today_tasks


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
import m.derakhshan.done.database.models.TasksModel
import m.derakhshan.done.databinding.TasksItemModelBinding


class TodayTaskRecyclerAdapter(private val today: PersianCalendar) :
    ListAdapter<TasksModel, TodayTaskRecyclerAdapter.ViewHolder>
        (object : DiffUtil.ItemCallback<TasksModel>() {
        override fun areItemsTheSame(oldItem: TasksModel, newItem: TasksModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TasksModel, newItem: TasksModel): Boolean {
            return (oldItem.id == newItem.id) && (oldItem.status == newItem.status)
        }
    }
    ) {


    fun getItemModel(position: Int): TasksModel = getItem(position)





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val taskBinding = DataBindingUtil.inflate<TasksItemModelBinding>(
            inflater,
            R.layout.tasks_item_model,
            parent,
            false
        )
        return ViewHolder(taskBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(
        private val myView: TasksItemModelBinding
    ) :
        RecyclerView.ViewHolder(myView.root) {

        fun bind(model: TasksModel) {

            myView.taskName.text = model.taskName

            if (model.status == TaskStatus.DONE) {
                myView.frameRoot.alpha = 0.3F
                myView.doneAnimation.progress = 1F
            } else {
                myView.frameRoot.alpha = 1F
                myView.doneAnimation.progress = 0F
            }

            myView.taskName.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (model.date.keys.first() < today.shortDateString && model.status == TaskStatus.IN_PROGRESS) R.color.red else R.color.black
                )
            )
        }


    }
}