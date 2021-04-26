package m.derakhshan.done.canvas


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import m.derakhshan.done.R

class AnalogClock(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var backColor = Color.WHITE
    private var linesDefaultColor = Color.GRAY
    private var linesColor = Color.GRAY

    private var secondColor = Color.GRAY
    private var minuteColor = Color.BLUE
    private var hourColor = Color.BLUE

    private val lineWidth = 8F
    private val lineHeight = 35
    private var size = 200
    private var radius = 50F

    private var second = 0
    private var minute = 0
    private var hour = 0


    init {
        context?.let {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs, R.styleable.AnalogClock,
                0, 0
            )
            backColor =
                typedArray.getColor(R.styleable.AnalogClock_backgroundColor, Color.WHITE)
            linesDefaultColor =
                typedArray.getColor(R.styleable.AnalogClock_linesDefaultColor, Color.GRAY)
            linesColor =
                typedArray.getColor(R.styleable.AnalogClock_linesColor, Color.GRAY)
            secondColor =
                typedArray.getColor(R.styleable.AnalogClock_secondsColor, Color.GRAY)
            minuteColor =
                typedArray.getColor(R.styleable.AnalogClock_minuteColor, Color.BLUE)
            hourColor =
                typedArray.getColor(R.styleable.AnalogClock_hourColor, Color.BLUE)
            size =
                typedArray.getDimension(R.styleable.AnalogClock_size, 200F).toInt()
            radius =
                size / 2F
        }

    }


    private fun drawCircle(canvas: Canvas?) {
        canvas?.let {

            paint.color = Color.GRAY
            paint.style = Paint.Style.FILL
            paint.alpha = 30
            canvas.drawCircle(width / 2F + 10, height / 2F + 10, radius / 1.1F , paint)

            paint.color = backColor
            paint.style = Paint.Style.FILL
            canvas.drawCircle(width / 2F, height / 2F, radius / 1.1F, paint)


        }
    }

    private fun drawDot(canvas: Canvas?) {

        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(width / 2F, height / 2F, radius / 14, paint)

        paint.style = Paint.Style.STROKE
        paint.color = Color.GRAY
        paint.strokeWidth = lineWidth / 2

        canvas?.drawCircle(width / 2F , height / 2F, radius / 14, paint)
    }

    private fun drawLines(canvas: Canvas?) {
        canvas?.let {

            paint.style = Paint.Style.FILL

            for (i in 0..59) {
                canvas.save()
                canvas.rotate(i * 6F, width / 2F, height / 2F)

                paint.color = if (i <= second)
                    linesColor
                else
                    linesDefaultColor

                val left = width / 2F
                val top = (height / 2F) - radius - lineHeight
                val right = left + lineWidth / 1.7F
                val bottom = top + lineHeight
                canvas.drawRect(left, top, right, bottom, paint)

                canvas.restore()

            }


        }
    }

    private fun drawSecond(canvas: Canvas?, second: Int) {

        canvas?.let {
            val rad = radius / 1.1F
            paint.color = secondColor
            paint.style = Paint.Style.FILL
            canvas.save()

            canvas.rotate(second * 6F, width / 2F, height / 2F)
            val left = width / 2F
            val top = height / 2F - rad + 10
            val right = left + lineWidth / 1.6F
            val bottom = height / 2F - rad / 15F
            canvas.drawRect(left, top, right, bottom, paint)

            canvas.restore()
        }
    }

    private fun drawMinute(canvas: Canvas?, minute: Int) {

        canvas?.let {
            val rad = radius / 1.1F
            paint.color = minuteColor
            paint.style = Paint.Style.FILL
            canvas.save()

            canvas.rotate(minute * 6F, width / 2F, height / 2F)
            val left = width / 2F
            val top = height / 2F - rad + 10
            val right = left + lineWidth / 1.2F
            val bottom = height / 2F - rad / 15F
            canvas.drawRect(left, top, right, bottom, paint)

            canvas.restore()
        }
    }

    private fun drawHour(canvas: Canvas?, hour: Int, min: Int) {

        canvas?.let {
            val rad = radius / 1.1F
            paint.color = hourColor
            paint.style = Paint.Style.FILL
            canvas.save()

            canvas.rotate(hour % 12 * 30F + min * 0.5F, width / 2F, height / 2F)
            val left = width / 2F
            val top = height / 2F - rad + 50
            val right = left + lineWidth / 1.2F
            val bottom = height / 2F - rad / 15F
            canvas.drawRect(left, top, right, bottom, paint)

            canvas.restore()
        }
    }


    fun setClock(second: Int, minute: Int, hour: Int) {
        this.second = second
        this.minute = minute
        this.hour = hour
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCircle(canvas)
        drawLines(canvas)
        drawHour(canvas, hour,minute)
        drawMinute(canvas, minute)
        drawSecond(canvas, second)
        drawDot(canvas)
    }

}