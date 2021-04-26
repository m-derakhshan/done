package m.derakhshan.done.main.today_tasks


import android.graphics.Canvas
import android.view.View
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
            val foregroundView: View = viewHolder.itemView.findViewById(R.id.view_foreground)
            getDefaultUIUtil().onSelected(foregroundView)
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

        if (dX > 0) {

            val deleteLayout: View? =
                viewHolder.itemView.findViewById(R.id.view_background_delete)
            deleteLayout?.visibility = View.GONE

            val doneLayout: View? =
                viewHolder.itemView.findViewById(R.id.view_background_done)
            doneLayout?.visibility = View.VISIBLE

            val doneAnimation: LottieAnimationView =
                viewHolder.itemView.findViewById(R.id.done_animation)

            doneAnimation.progress = dX / 1200

        } else {
            val deleteLayout: View? =
                viewHolder.itemView.findViewById(R.id.view_background_delete)
            deleteLayout?.visibility = View.VISIBLE

            val doneLayout: View? =
                viewHolder.itemView.findViewById(R.id.view_background_done)
            doneLayout?.visibility = View.GONE

            val deleteAnimation: LottieAnimationView? =
                viewHolder.itemView.findViewById(R.id.delete_animation)
            deleteAnimation?.progress = -dX / 5000


        }


        val foregroundView: View? = viewHolder.itemView.findViewById(R.id.view_foreground)
        getDefaultUIUtil().onDraw(
            c, recyclerView, foregroundView, dX, dY,
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

