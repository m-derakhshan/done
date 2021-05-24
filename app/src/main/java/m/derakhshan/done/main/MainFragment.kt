package m.derakhshan.done.main

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import dagger.hilt.android.AndroidEntryPoint
import de.co.derakhshan.imagepickerlib.ImagePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import m.derakhshan.done.Arrange
import m.derakhshan.done.R
import m.derakhshan.done.Utils
import m.derakhshan.done.database.TasksDatabase
import m.derakhshan.done.database.models.TaskStatus
import m.derakhshan.done.database.models.TasksModel
import m.derakhshan.done.databinding.FragmentMainBinding
import m.derakhshan.done.main.today_tasks.RecyclerItemTouchHelper
import m.derakhshan.done.main.today_tasks.TodayTaskRecyclerAdapter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@AndroidEntryPoint
class MainFragment : Fragment(),
    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    companion object {
        var motionProgress = 0F
        var todayTaskCount = MutableLiveData<Int>()
        var isAddTaskOpen = false
        var setAddTaskVisibility = MutableLiveData<Boolean>()
    }


    //-------------------------(Injection Part)-----------------------//
    @Inject
    lateinit var database: TasksDatabase

    @Inject
    lateinit var arrange: Arrange

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var adapter: TodayTaskRecyclerAdapter

    @Inject
    lateinit var utils: Utils


    //-------------------------(Global variables)-----------------------//
    private lateinit var binding: FragmentMainBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //-------------------------(init variables)-----------------------//
        val taskDate = ArrayList<PersianCalendar>()
        taskDate.add(PersianCalendar())

        val swipeLeft: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(swipeLeft).attachToRecyclerView(binding.taskLayout.tasksRecyclerView)
        val calendarFactory = object : LightThemeFactory() {
            override val typefacePath: String
                get() = "vazir.ttf"
        }
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        //-------------------------(Set Observers)-----------------------//
        viewModel.todayTasks.observe(viewLifecycleOwner, {
            it?.let { data ->
                binding.taskLayout.emptyTask.visibility = if (data.isNotEmpty())
                    View.GONE
                else View.VISIBLE
                adapter.submitList(data)
                todayTaskCount.value = data.size
            }
        })
        viewModel.initClock().observe(viewLifecycleOwner, {
            it?.let { time ->
                binding.clockLayout.clock.setClock(
                    second = time.second,
                    minute = time.minute,
                    hour = time.hour
                )
                viewModel.greeting(time)
            }
        })
        viewModel.greeting.observe(viewLifecycleOwner, {
            it?.let { msg ->
                binding.greeting.text = msg
            }
        })
        setAddTaskVisibility.observe(viewLifecycleOwner, {
            it?.let { visible ->
                if (visible) {
                    binding.addTaskHolder.visibility = View.VISIBLE
                    binding.task.requestFocus()
                    inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                } else binding.addTaskHolder.visibility = View.GONE
            }
        })

        //-------------------------(Set Binding Functions)-----------------------//
        binding.userName.setText(utils.userNameFamily)
        if (!utils.userImage.isNullOrEmpty())
            binding.picture.setImageURI(Uri.parse(utils.userImage))
        binding.picture.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .maxResultSize(1080, 1080)
                .start(startForImage)
        }
        binding.taskLayout.tasksRecyclerView.apply {
            this.adapter = this@MainFragment.adapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.clockLayout.addTask.setOnClickListener {
            isAddTaskOpen = true
            setAddTaskVisibility.value = true
            // refresh the task date and set it equals to today date
            taskDate[0] = PersianCalendar()
        }


        binding.add.setOnClickListener {
            val taskName = binding.task.text.toString()
            if (taskName.isNotEmpty()) {
                binding.addAnimation.progress = 0.5F
                binding.addAnimation.resumeAnimation()
                binding.addAnimation.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {}
                    override fun onAnimationCancel(p0: Animator?) {}
                    override fun onAnimationRepeat(p0: Animator?) {}
                    override fun onAnimationEnd(p0: Animator?) {
                        binding.addAnimation.progress = 0F
                    }
                })
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        for (task in taskDate) {

                            val time =
                                Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString() + ":" +
                                        Calendar.getInstance().get(Calendar.MINUTE)
                                            .toString() + ":" +
                                        Calendar.getInstance().get(Calendar.SECOND).toString()
                            database.tasksDAO.add(
                                TasksModel(
                                    taskName = taskName,
                                    date = HashMap<String, String>().apply {
                                        this[task.shortDateString] = task.weekDayName
                                    },
                                    status = TaskStatus.IN_PROGRESS,
                                    time = time,
                                    id = arrange.generateID(task.shortDateString, time)
                                )
                            )

                        }

                    }
                }
                binding.task.text.clear()
            }
        }

        val multipleDays = MultipleDaysPickCallback { days ->
            taskDate.clear()
            for (day in days)
                taskDate.add(day.toPersian())
        }

        binding.time.setOnClickListener {
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
            PrimeDatePicker.dialogWith(PersianCalendar())
                .pickMultipleDays(multipleDays)
                .initiallyPickedMultipleDays(taskDate)
                .applyTheme(calendarFactory)
                .minPossibleDate(PersianCalendar())
                .build()
                .show(parentFragmentManager, "date")
        }

        binding.editName.setOnClickListener {
            binding.editName.alpha = 0.5F
            binding.editName.isEnabled = false
            binding.userName.isEnabled = true
            binding.userName.requestFocus()
            inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
        binding.userName.setOnFocusChangeListener { _, changed ->
            if (changed) {
                binding.editName.alpha = 1F
                binding.userName.isEnabled = false
                binding.editName.isEnabled = true
            }
        }
        binding.userName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(newName: Editable?) {
                utils.userNameFamily = newName.toString()
            }
        })


    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is TodayTaskRecyclerAdapter.ViewHolder) {
            val deletedItem: TasksModel = adapter.getItemModel(position)

            if (direction == ItemTouchHelper.LEFT) {
                viewModel.deleteTask(adapter.getItemModel(position))
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

    override fun onResume() {
        super.onResume()
        binding.mainRoot.progress = if (motionProgress > 0.5F) 1F else 0F
    }

    override fun onPause() {
        super.onPause()
        motionProgress = binding.mainRoot.progress
        if (isAddTaskOpen) {
            setAddTaskVisibility.value = false
            isAddTaskOpen = false
        }
    }

    private val startForImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    binding.picture.setImageURI(uri)
                    utils.userImage = uri.toString()
                }
            }
        }


}