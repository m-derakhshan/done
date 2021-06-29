package m.derakhshan.done.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import m.derakhshan.done.Arrange
import m.derakhshan.done.R

class DrawDate(context: Context?, attrs: AttributeSet?) : View(context, attrs) {


    private var selectedDateColor = Color.GREEN
    private var normalDateColor = Color.GRAY
    var taskDate: Int = 3
        get() = field + 5


    private var size = 15F
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textAppearance = Paint(Paint.ANTI_ALIAS_FLAG)
    private val arrange = Arrange()

    init {
        context?.let {
            val typedArray =
                context.theme.obtainStyledAttributes(attrs, R.styleable.subTaskDate, 0, 0)

            selectedDateColor =
                typedArray.getColor(R.styleable.subTaskDate_selectedDateColor, Color.GREEN)
            normalDateColor =
                typedArray.getColor(R.styleable.subTaskDate_normalDateColor, Color.GRAY)

            textAppearance.apply {
                textSize =
                    typedArray.getDimension(R.styleable.subTaskDate_dateTextSize, 60F)
                color =
                    typedArray.getColor(R.styleable.subTaskDate_textColor, Color.WHITE)
                textAlign = Paint.Align.CENTER
            }
            taskDate = typedArray.getInt(R.styleable.subTaskDate_taskDate, 3)

        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var xPosition: Float
        var yPosition: Float

        size = width / 22F

        for (i in 0..9) {
            xPosition = (size * (if (i == 0) 1 else (i * 2) + 1)) * 1.1F
            yPosition = size
            paint.color = if (i == 5) selectedDateColor else normalDateColor
            canvas?.drawCircle(xPosition, yPosition, size, paint)
            canvas?.drawText(
                arrange.persianConverter((taskDate - i).toString()),
                xPosition - ((textAppearance.descent() + textAppearance.ascent()) / 15),
                yPosition - ((textAppearance.descent() + textAppearance.ascent()) / 2),
                textAppearance
            )

        }

    }


}