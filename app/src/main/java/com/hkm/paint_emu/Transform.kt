package com.hkm.paint_emu

import android.graphics.Point
import android.graphics.PointF
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

abstract class Transform {
    abstract fun rotate(angle: Float) : Polygon
    abstract fun translate(x: Int, y: Int) : Polygon
    abstract fun reflect() : Polygon
    abstract fun reflectX() : Polygon
    abstract fun reflectY() : Polygon
    abstract fun scale(x: Float, y: Float) : Polygon

    protected fun rot(point: Point, angle: Float): PointF {
        val xPrime = (point.x * cosD(angle)) - (point.y * sinD(angle))
        val yPrime = (point.x * sinD(angle)) + (point.y * cosD(angle))

        return PointF(xPrime.toFloat(), yPrime.toFloat())
    }

    private fun cosD(angle: Float): Double {
        return cos(angle * PI /180)
    }

    private fun sinD(angle: Float) : Double {
        return sin(angle * PI /180)
    }
}