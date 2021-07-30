package m.derakhshan.done.tasks.subTasks


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import m.derakhshan.done.Arrange
import m.derakhshan.done.R
import m.derakhshan.done.Utils
import m.derakhshan.done.database.TasksDatabase
import m.derakhshan.done.databinding.FragmentSubTasksBinding
import m.derakhshan.done.tasks.subTasks.alarm.AlarmService
import javax.inject.Inject


@AndroidEntryPoint
class SubTasksFragment : Fragment() {


    private lateinit var binding: FragmentSubTasksBinding
    private lateinit var viewModel: SubTaskViewModel
    private var hasReminder = false

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
        val alarmService = AlarmService(requireContext())

        val taskID = arguments?.getLong("id")!!
        val factory = SubTaskViewModelFactory(
            taskID = taskID,
            database = database,
            alarmService = alarmService
        )
        viewModel = ViewModelProvider(this, factory).get(SubTaskViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.subTasks.observe(viewLifecycleOwner, {
            it?.let { tasksList ->
                var subTasks = ""
                for (task in tasksList)
                    subTasks = subTasks + task.subTask + "\n"
                binding.subTasks.setText(subTasks.trim())
            }
        })

        binding.addReminder.setOnClickListener {
            if (hasReminder)
                hasReminder = false.also {
                    viewModel.deleteReminder()
                    binding.reminderIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            else
                showDialog()
        }

        viewModel.reminder.observe(viewLifecycleOwner, {
            it?.let { reminder ->
                if (reminder.isEmpty())
                    binding.reminder.text = "یادآوری برای این کار تنظیم نشده است.".also {
                        hasReminder = false
                        binding.reminderIcon.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                else
                    binding.reminder.text = Arrange().persianConcatenate(
                        first = "یادآوری برای: ",
                        end = reminder
                    ).also {
                        hasReminder = true
                        binding.reminderIcon.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    }
            }
        })
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.set_alarm_dialog)

        val hour = dialog.findViewById(R.id.alarm_hour) as NumberPicker
        hour.apply {
            this.minValue = 0
            this.maxValue = 23
        }
        val minute = dialog.findViewById(R.id.alarm_minute) as NumberPicker
        minute.apply {
            this.minValue = 0
            this.maxValue = 59
        }
        val setReminder = dialog.findViewById(R.id.set_reminder) as Button

        setReminder.setOnClickListener {
            viewModel.setAlarm(
                hour = hour.value.toString(),
                minute = minute.value.toString()
            )
            Utils(requireContext()).showSnackBar(
                color = ContextCompat.getColor(requireContext(), R.color.black),
                msg = "یادآور با موفقیت اضافه شد!",
                snackView = binding.root
            ).show()
            dialog.dismiss()
            binding.reminderIcon.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }
        dialog.show()
    }

    override fun onPause() {
        viewModel.endOfFragmentLifeCycle(
            taskName = binding.taskSubject.text.toString(),
            subTasks = binding.subTasks.text.toString()
        )
        super.onPause()
    }


}