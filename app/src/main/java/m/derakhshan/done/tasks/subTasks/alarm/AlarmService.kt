package m.derakhshan.done.tasks.subTasks.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import m.derakhshan.done.Constants

class AlarmService(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    private fun getIntent() = Intent(context, AlarmReceiver::class.java)
    private fun getPendingIntent(intent: Intent, reqCode: Long) = PendingIntent.getBroadcast(
        context,
        reqCode.toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    fun cancelAlarm(title: String, msg: String, reqCode: Long) {
        alarmManager?.cancel(
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_ALARM
                    putExtra(Constants.EXTRA_TITLE_ALARM, title)
                    putExtra(Constants.EXTRA_MSG_ALARM, msg)
                    putExtra(Constants.EXTRA_TASK_ALARM_ID, reqCode)
                },
                reqCode = reqCode
            )
        )
    }

    fun setAlarm(timeInMillisecond: Long, title: String, msg: String, reqCode: Long) {
        alarmManager?.let {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillisecond,
                    getPendingIntent(
                        getIntent().apply {
                            action = Constants.ACTION_SET_ALARM
                            putExtra(Constants.EXTRA_TITLE_ALARM, title)
                            putExtra(Constants.EXTRA_MSG_ALARM, msg)
                            putExtra(Constants.EXTRA_TASK_ALARM_ID, reqCode)
                        },
                        reqCode = reqCode
                    )
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillisecond,
                    getPendingIntent(
                        getIntent().apply {
                            action = Constants.ACTION_SET_ALARM
                            putExtra(Constants.EXTRA_TITLE_ALARM, title)
                            putExtra(Constants.EXTRA_MSG_ALARM, msg)
                            putExtra(Constants.EXTRA_TASK_ALARM_ID, reqCode)
                        },
                        reqCode = reqCode
                    )
                )
            }
        }

    }
}