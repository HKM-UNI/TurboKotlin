package com.hkm.paint_emu

import android.graphics.Point
import androidx.core.graphics.toPoint

class Polygon(points: Array<Point>) : Transform() {
    private var points = arrayListOf<Point>()

    init {
        for (p in points) {
            this.points.add(p)
        }
    }

    fun getPoints() = points

    private fun perform(operation: (Point) -> Unit) : Polygon {
        val copy = points.toTypedArray().onEach { operation.invoke(it) }
        return Polygon(copy as Array<Point>)
    }

    override fun rotate(angle: Float): Polygon {
        return perform { p ->
            run {
                val rotated = rot(p, angle).toPoint()
                p.set(rotated.x, rotated.y)
            }
        }
    }

    override fun translate(x: Int, y: Int): Polygon {
        return perform { p -> p.set(p.x + x, p.y + y) }
    }

    override fun reflectX(): Polygon {
        return perform { p -> p.set(p.x, p.y * -1) }
    }

    override fun reflectY(): Polygon {
        return perform { p -> p.set(p.x * -1, p.y) }
    }

    override fun reflect(): Polygon {
        return perform { p -> p.set(p.x * -1, p.y * -1) }
    }

    override fun scale(x: Float, y: Float): Polygon {
        return perform { p ->
            p.set((p.x * x).toInt(), (p.y * y).toInt())
        }
    }

    override fun toString(): String {
        return points.joinToString { ", " }
    }
}