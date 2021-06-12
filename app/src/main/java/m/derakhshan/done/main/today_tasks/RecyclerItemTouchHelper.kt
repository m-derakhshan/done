package m.derakhshan.done.main.today_tasks


import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import m.derakhshan.done.R


class RecyclerItemTouchHelper(
    dragDirs: Int,
    swipeDirs: Int,
    private val listener: RecyclerItemTouchHelperListener
) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    private var direction: Float = 0F


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            try {
                val foregroundView: View = viewHolder.itemView.findViewById(R.id.view_foreground)
                getDefaultUIUtil().onSelected(foregroundView)
            } catch (e: Exception) {
                Log.i("Log", "error in ItemTouchHelper Line 37")
            }
        }
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val foregroundView: View? = viewHolder?.itemView?.findViewById(R.id.view_foreground)
        this.direction = dX

        getDefaultUIUtil().onDrawOver(
            c, recyclerView, foregroundView, dX, dY,
            actionState, isCurrentlyActive
        )
    }


    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {

        val foregroundView: View? = viewHolder.itemView.findViewById(R.id.view_foreground)
        getDefaultUIUtil().clearView(foregroundView)

    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val backgroundLayout: View =
            viewHolder.itemView.findViewById(R.id.view_background)
        val mainLayout: View? =
            viewHolder.itemView.findViewById(R.id.frame_root)
        val deleteAnimation: LottieAnimationView =
            viewHolder.itemView.findViewById(R.id.done_animation)

        val deleteLayout: View =
            viewHolder.itemView.findViewById(R.id.delete)

        val editLayout: View =
            viewHolder.itemView.findViewById(R.id.edit)

        if (dX > 0) {
            //-------------------------(here is for opening delete and edit menu)-----------------------//
            backgroundLayout.background.setTint(
                ContextCompat.getColor(
                    viewHolder.itemView.context,
                    R.color.orange
                )
            )
            deleteLayout.visibility = View.VISIBLE
            editLayout.visibility = View.GONE
            deleteAnimation.visibility = View.GONE
        } else {
            //-------------------------(here is for mark task as done)-----------------------//
            deleteAnimation.visibility = View.VISIBLE
            deleteLayout.visibility = View.GONE
            editLayout.visibility = View.GONE


            backgroundLayout.background.setTint(
                ContextCompat.getColor(
                    viewHolder.itemView.context,
                    R.color.light_green
                )
            )

            deleteAnimation.progress = if (mainLayout!!.alpha < 1F) (1F + dX / 800) else -dX / 1300

        }


        val foregroundView: View? = viewHolder.itemView.findViewById(R.id.view_foreground)
        getDefaultUIUtil().onDraw(
            c, recyclerView, foregroundView, dX / 3, dY,
            actionState, isCurrentlyActive
        )
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }


    interface RecyclerItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int)
    }
}

