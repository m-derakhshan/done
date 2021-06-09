package m.derakhshan.done.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primecalendar.persian.PersianCalendar
import m.derakhshan.done.R
import m.derakhshan.done.databinding.FragmentSubTasksBinding

class SubTasksFragment : Fragment() {

    private lateinit var binding: FragmentSubTasksBinding

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

        binding.lifecycleOwner = this
        binding.calendar.calendarType = CalendarType.PERSIAN
    }
}