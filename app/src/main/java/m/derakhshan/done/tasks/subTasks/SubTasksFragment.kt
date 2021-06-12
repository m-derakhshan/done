package m.derakhshan.done.tasks.subTasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import m.derakhshan.done.R
import m.derakhshan.done.database.TasksDatabase
import m.derakhshan.done.databinding.FragmentSubTasksBinding
import javax.inject.Inject

@AndroidEntryPoint
class SubTasksFragment : Fragment() {

    private lateinit var binding: FragmentSubTasksBinding
    private lateinit var viewModel: SubTaskViewModel

    @Inject
    lateinit var database: TasksDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_tasks, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //-------------------------(init variables)-----------------------//

        val taskID = arguments?.getLong("id")!!
        val factory = SubTaskViewModelFactory(taskID = taskID, database = database)
        viewModel = ViewModelProvider(this, factory).get(SubTaskViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.setDay().observe(viewLifecycleOwner, {
            binding.topDates.taskDate = it.toInt()
            binding.topDates.invalidate()
        })
        viewModel.subTasks.observe(viewLifecycleOwner, {
            it?.let { tasksList ->
                var subTasks = ""
                for (task in tasksList)
                    subTasks = subTasks + task.subTask + "\n"
                binding.subTasks.setText(subTasks.trim())
            }
        })

    }

    override fun onPause() {
        viewModel.endOfFragmentLifeCycle(
            taskName = binding.taskSubject.text.toString(),
            subTasks = binding.subTasks.text.toString()
        )
        super.onPause()
    }


}