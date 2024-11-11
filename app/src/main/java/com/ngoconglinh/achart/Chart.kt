package com.ngoconglinh.achart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat


class Chart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val circleLeftSize = 20f
    private val textHeight = 30f
    private var maxDay = 31
    private var column = 7
    private var isFormatNumber = true

    private val colorList = listOf(
        R.color.color_part5,
        R.color.color_part4,
        R.color.color_part3,
        R.color.color_part2,
        R.color.color_part1,
    )

    private val colorListNoData = listOf(
        R.color.color_part1_dis,
        R.color.color_part2_dis,
        R.color.color_part3_dis,
        R.color.color_part4_dis,
        R.color.color_part5_dis,
    )

    private var currentColorList = listOf<Int>()

    private val circlePointY = mutableListOf<Float>()
    private val dayPointX = mutableListOf<Float>()

    private var listChartPoint = mutableListOf<ChartPoint>()

    init {
        paint.color = Color.BLACK

        val ta = context.obtainStyledAttributes(attrs, R.styleable.Chart)
        maxDay = ta.getInt(R.styleable.Chart_maxDay, 31)
        column = ta.getInt(R.styleable.Chart_column, 7)
        isFormatNumber = ta.getBoolean(R.styleable.Chart_isFormatNumber, true)
        currentColorList = colorList

        ta.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        circlePointY.clear()
        dayPointX.clear()
        canvas.let {

            //draw 5 circle left
            val per = (height.toFloat() - (textHeight * 2)) / 5
            for (i in 0 .. 4) {
                paint.color = context.resources.getColor(currentColorList[i], null)
                val y = (per * i) + (circleLeftSize)
                circlePointY.add(y)
                it.drawCircle(circleLeftSize, y, circleLeftSize, paint)
            }

            val startAreaDrawWith = (circleLeftSize * 4)
            val areaDrawWith = width - startAreaDrawWith - circleLeftSize

            val keyDays = mutableListOf<Int>()
            val perDay = maxDay / (column - 1)

            for (i in 0..(column - 2)) {
                keyDays.add(i * perDay + 1)
            }
            keyDays.add(maxDay)

            val perAreaDrawWith = areaDrawWith / maxDay
            for (i in 1..maxDay) {
                if (i in keyDays) {
                    //draw 7 line vertical
                    paint.color = Color.parseColor("#646464")
                    paint.strokeWidth = 2f
                    it.drawLine((perAreaDrawWith * (i - 1)) + startAreaDrawWith, 0f, (perAreaDrawWith * (i - 1)) + startAreaDrawWith, height.toFloat() - (textHeight * 2) - circleLeftSize, paint)

                    //draw 7 text
                    paint.color = com.ngoconglinh.achart.Color.generateTransparentColor(
                            Color.BLACK, 0.8
                        )
                    paint.textSize = textHeight
                    paint.typeface = ResourcesCompat.getFont(context, R.font.nunito_bold)

                    val text = if (isFormatNumber) String.format("%02d", i) else i.toString()
                    val bonusMargin = if (isFormatNumber) - 20f else {
                        when(i) {
                            1,2,3,4,5,6,7,8,9 -> - 7f
                            else -> - 20f
                        }
                    }
                    it.drawText(text, (perAreaDrawWith * (i - 1)) + startAreaDrawWith + bonusMargin, height.toFloat() - textHeight, paint)
                }
                dayPointX.add((perAreaDrawWith * (i - 1)) + startAreaDrawWith,)
            }

            if (listChartPoint.isNotEmpty() && listChartPoint.size > 1) {
                for ( i in 0 .. listChartPoint.size - 2) {
                    val chartPoint = listChartPoint[i]
                    val chartPointNext = listChartPoint[i + 1]
                    val startPointX = dayPointX[chartPoint.column.coerceIn(0 until maxDay)].toInt()
                    val startPointY = circlePointY[chartPoint.row.coerceIn(0..4)].toInt()
                    val pointStart = Point(startPointX, startPointY)
                    val endPointX = dayPointX[chartPointNext.column.coerceIn(0 until maxDay)].toInt()
                    val endPointY = circlePointY[chartPointNext.row.coerceIn(0..4)].toInt()
                    val pointEnd = Point(endPointX, endPointY)
                    it.drawChartLine(i == 0, pointStart, pointEnd)
                }
            } else if (listChartPoint.size == 1) {
                val chartPoint = listChartPoint[0]
                val startPointX = dayPointX[chartPoint.column.coerceIn(0 until maxDay)].toInt()
                val startPointY = circlePointY[chartPoint.row.coerceIn(0..4)].toInt()
                val pointStart = Point(startPointX, startPointY)
                it.drawChartLine(true, pointStart, pointStart)
            }
        }
    }

    private fun Canvas.drawChartLine(isStartPoint: Boolean = false, startPoint: Point, endPoint: Point) {
        paint.color = Color.parseColor("#FE556F")
        paint.strokeWidth = 3f
        if (isStartPoint) {
            drawCircle(startPoint.x.toFloat(), startPoint.y.toFloat(), 7f, paint)
        }
        drawLine(startPoint.x.toFloat(), startPoint.y.toFloat(), endPoint.x.toFloat(), endPoint.y.toFloat(), paint)
        drawCircle(endPoint.x.toFloat(), endPoint.y.toFloat(), 7f, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    }


    /**
     * Input by ChartPoint model
     */
    fun addChartPoint(chartPoint: MutableList<ChartPoint>) {
        chartPoint.sortBy { it.column }
        listChartPoint = chartPoint
        invalidate()
    }

    /**
     * is use color or not
     */
    fun setColorEnable(isHaveColor: Boolean) {
        currentColorList = if (isHaveColor) colorList else colorListNoData
        invalidate()
    }

    var mMaxDay get() = maxDay
        set(value) {
            maxDay = value
            invalidate()
        }

    data class ChartPoint(val column: Int, val row: Int)

}
