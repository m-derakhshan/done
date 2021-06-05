package m.derakhshan.done.tasks

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import m.derakhshan.done.R
import m.derakhshan.done.Utils
import m.derakhshan.done.database.TasksDatabase
import m.derakhshan.done.database.models.TasksModel
import m.derakhshan.done.databinding.FragmentTasksBinding
import m.derakhshan.done.main.MainViewModel
import m.derakhshan.done.main.today_tasks.RecyclerItemTouchHelper
import javax.inject.Inject


@AndroidEntryPoint
class TasksFragment : Fragment(),
    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {


    //-------------------------(Injection)-----------------------//
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var adapter: TasksRecyclerAdapter

    @Inject
    lateinit var utils: Utils

    @Inject
    lateinit var database: TasksDatabase


    //-------------------------(Global variables)-----------------------//
    private lateinit var binding: FragmentTasksBinding


    companion object {
        var showAnim: Boolean = false
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //-------------------------(init variables)-----------------------//
        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.tasksRecyclerView)


        //-------------------------(Set Binding Functions)-----------------------//
        binding.tasksRecyclerView.adapter = adapter
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        //-------------------------(set observers)-----------------------//
        viewModel.allTasks.observe(viewLifecycleOwner, { list ->
            list?.let { data: List<TasksModel> ->
                if (data.isEmpty()) {
                    if (showAnim) {
                        binding.allDoneAnimation.visibility = View.VISIBLE
                        binding.allDoneAnimation.playAnimation()
                        showAnim = false
                    }
                } else {
                    binding.allDoneAnimation.visibility = View.GONE
                    showAnim = true
                }
                val tasksItem = ArrayList<TasksList>()
                //-------------------------(group by key which is date in fact)-----------------------//
                val tasksList: Map<String, List<TasksModel>> =
                    data.groupBy { it.date.keys.first() }
                for (i in tasksList.keys) {
                    //-------------------------(adding date in list first)-----------------------//
                    tasksItem.add(DateItem().apply {
                        this.date = HashMap<String, String>().apply {
                            this[i] = tasksList[i]?.get(0)?.date?.values?.first() ?: ""
                        }
                    })
                    for (j in tasksList[i]!!)
                        tasksItem.add(TaskItem().apply {
                            this.task = j
                        })
                }

                adapter.submitList(tasksItem)

            }
        })


    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is TasksRecyclerAdapter.ViewHolder) {
            val deletedItem: TasksModel = adapter.getItemModel(position).task

            if (direction == ItemTouchHelper.RIGHT) {
                viewModel.deleteTask(adapter.getItemModel(position).task)
                utils.vibratePhone()
                val snackbar = utils.showSnackBar(
                    ContextCompat.getColor(requireContext(), R.color.snackBarBlack),
                    msg = "کار با موفقیت حذف شد.",
                    snackView = binding.root
                )

                snackbar.setAction("بازگردانی!") {
                    viewModel.restoreTask(deletedItem)
                }
                snackbar.setActionTextColor(Color.YELLOW)
                ViewCompat.setLayoutDirection(snackbar.view, ViewCompat.LAYOUT_DIRECTION_RTL)
                snackbar.show()
            } else {

                viewModel.updateStatus(deletedItem)
            }
        }
    }

}