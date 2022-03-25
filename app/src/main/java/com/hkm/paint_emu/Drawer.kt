package com.hkm.paint_emu

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import androidx.core.graphics.plus
import androidx.core.graphics.toPointF

class Drawer() {
    private var canvas = Canvas()
    private var paint = Paint()
    private var offset = Point()

    constructor(canvas: Canvas, paint: Paint, offset: Point = Point(0,0)) : this() {
        this.canvas = canvas
        this.paint = paint
        this.offset = offset
    }

    fun drawSegment(a: Point, b: Point) {
        val start = (a + offset).toPointF()
        val end = (b + offset).toPointF()
        canvas.drawLine(start.x, start.y, end.x , end.y, paint)
    }

    fun draw(poly: Polygon) {
        val points = poly.getPoints()
        for (i in 0..points.size-2) {
            drawSegment(points[i], points[i+1])
        }
        // Close poly
        drawSegment(points[0], points[points.size-1])
    }
}