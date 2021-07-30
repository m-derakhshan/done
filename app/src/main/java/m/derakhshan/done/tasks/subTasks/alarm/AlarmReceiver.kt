package m.derakhshan.done.tasks.subTasks.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import io.karn.notify.Notify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import m.derakhshan.done.Constants
import m.derakhshan.done.database.TasksDatabase
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var database: TasksDatabase

    override fun onReceive(context: Context, intent: Intent) {

        buildNotification(
            context = context,
            msg = intent.getStringExtra(Constants.EXTRA_MSG_ALARM),
            title = intent.getStringExtra(Constants.EXTRA_TITLE_ALARM),
            taskID = intent.getStringExtra(Constants.EXTRA_TASK_ALARM_ID)
        )

    }


    private fun buildNotification(context: Context, msg: String?, title: String?, taskID: String?) {
        Notify.with(context).content {
            this.title = title
            this.text = msg
        }.show()

        CoroutineScope(Dispatchers.Default).launch {
            taskID?.let {
                database.tasksDAO.updateReminder("", taskID.toLong())
            }
        }
    }
}