package m.derakhshan.done.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import m.derakhshan.done.R
import kotlin.math.min

class MenuIcon(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var dotColor = ArrayList<Int>()

    private var size = 50F
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        context?.let {
            val typedArray =
                context.theme.obtainStyledAttributes(attrs, R.styleable.MenuIcon, 0, 0)
            dotColor.add(typedArray.getColor(R.styleable.MenuIcon_firstColor, Color.CYAN))
            dotColor.add(typedArray.getColor(R.styleable.MenuIcon_secondColor, Color.CYAN))
            dotColor.add(typedArray.getColor(R.styleable.MenuIcon_thirdColor, Color.CYAN))
            dotColor.add(typedArray.getColor(R.styleable.MenuIcon_fourthColor, Color.CYAN))

        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        size = min(width, height) / 5F

        paint.color = dotColor[0]
        canvas?.drawCircle(size, size, size, paint)

        paint.color = dotColor[1]
        canvas?.drawCircle(width - size, size, size, paint)

        paint.color = dotColor[2]
        canvas?.drawCircle(size, height - size, size, paint)

        paint.color = dotColor[3]
        canvas?.drawCircle(width - size, height - size, size, paint)

    }



}