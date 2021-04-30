package m.derakhshan.done


import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class Utils(private val context: Context) {

    fun showSnackBar(color: Int, msg: String, snackView: View): Snackbar {
        val font = Typeface.createFromAsset(context.assets, "vazir.ttf")
        val snackBar = Snackbar.make(snackView, msg, Snackbar.LENGTH_LONG)
        val view = snackBar.view
        view.setBackgroundColor(color)
        view.layoutDirection = View.LAYOUT_DIRECTION_RTL
        val txt = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val action = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        action.typeface = font
        txt.typeface = font
        return snackBar
    }

    fun vibratePhone() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, 1))
        } else {
            vibrator.vibrate(50)
        }
    }


    private val share = context.getSharedPreferences("share", Context.MODE_PRIVATE)
    private val editor = share.edit()


    var userNameFamily: String
        set(value) {
            editor.putString("userNameFamily", value)
            editor.apply()
        }
        get() = share.getString("userNameFamily", "") ?: ""

    var userImage: String?
        set(value) {
            editor.putString("userImage", value)
            editor.apply()
        }
        get() = share.getString("userImage", "")

}
