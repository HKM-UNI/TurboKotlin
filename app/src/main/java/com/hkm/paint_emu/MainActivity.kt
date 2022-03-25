package com.hkm.paint_emu

import android.content.res.Resources
import android.graphics.*
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val imgWidth = 326.toPx()
    private val imgHeight = 204.toPx()
    private val centeredOffset = Point(imgWidth /2, imgHeight/2)

    private var drawer = Drawer()
    private var defPainter = Paint()
    private val painterDashed = Paint().apply {
        color = Color.BLACK
        strokeWidth = 3.0f
        pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
    }

    private lateinit var bmpTranslateX : Bitmap
    private lateinit var bmpTranslateY : Bitmap
    private lateinit var bmpRotation : Bitmap
    private lateinit var bmpMirrorH : Bitmap
    private lateinit var bmpMirrorV : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Primitivas Gráficas"

        defPainter.apply {
            color = Color.BLACK
            strokeWidth = 5.0f
        }

        drawPlanes()
        drawTranslationX()
        drawTranslationY()
        drawRotation()
        drawReflectionY()
        drawReflectionX()
    }

    private fun drawPlanes() {
        val planeTemplate = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(planeTemplate)
        setDrawer(canvas, painterDashed, offset = Point(0,0))

        val topCenter = Point(imgWidth/2, 0)
        val bottomCenter = Point(imgWidth/2, imgHeight)
        drawer.drawSegment(topCenter, bottomCenter)

        val leftMidCenter = Point(0, imgHeight/2)
        val rightMidCenter = Point(imgWidth, imgHeight/2)
        drawer.drawSegment(leftMidCenter, rightMidCenter)

        // Init each bitmap with the plane template
        bmpTranslateX = Bitmap.createBitmap(planeTemplate)
        bmpTranslateY = Bitmap.createBitmap(planeTemplate)
        bmpRotation = Bitmap.createBitmap(planeTemplate)
        bmpMirrorH = Bitmap.createBitmap(planeTemplate)
        bmpMirrorV = Bitmap.createBitmap(planeTemplate)

        // Setting up the same plane for each image view
        findViewById<ImageView>(R.id.imageV).setImageBitmap(bmpTranslateX)
        findViewById<ImageView>(R.id.imageV2).setImageBitmap(bmpTranslateY)
        findViewById<ImageView>(R.id.imageV3).setImageBitmap(bmpRotation)
        findViewById<ImageView>(R.id.imageV4).setImageBitmap(bmpMirrorH)
        findViewById<ImageView>(R.id.imageV5).setImageBitmap(bmpMirrorV)
    }

    private fun drawTranslationX() {
        val squareX = Polygon (arrayOf(
            Point(-300, 100),
            Point(-100, 100),
            Point(-100, -100),
            Point(-300, -100))
        )
        setDrawer(Canvas(bmpTranslateX))

        drawer.draw(squareX)
        drawer.draw(squareX.translate(400, 0))
    }

    private fun drawTranslationY() {
        val squareY = Polygon(arrayOf(
            Point(-50, -100),
            Point(50, -100),
            Point(50, -200),
            Point(-50, -200))
        )
        setDrawer(Canvas(bmpTranslateY))

        drawer.draw(squareY)
        drawer.draw(squareY.translate(0, 300))
    }

    private fun drawRotation() {
        val triangle = Polygon(arrayOf(
            Point(-300, 100),
            Point(-200, -100),
            Point(-100, 100))
        )
        setDrawer(Canvas(bmpRotation))

        drawer.draw(triangle)
        /* Since the rotation is about the origin,
         * the resulting polygon needs to be moved to a more feasible location */
        drawer.draw(triangle.rotate(90f).translate(200, 200))
    }

    private fun drawReflectionY() {
        val triangle = Polygon(arrayOf(
            Point(-300, 100),
            Point(-300, -100),
            Point(-100, 100))
        )
        setDrawer(Canvas(bmpMirrorH))

        drawer.draw(triangle)
        drawer.draw(triangle.reflectY())
    }

    private fun drawReflectionX() {
        val triangle = Polygon(arrayOf(
            Point(50, -200),
            Point(50, -100),
            Point(-50, -100))
        )
        setDrawer(Canvas(bmpMirrorV))

        drawer.draw(triangle)
        drawer.draw(triangle.reflectX())
    }

    private fun setDrawer(
        canvas: Canvas,
        painter: Paint = defPainter,
        offset: Point = centeredOffset
    ) {
        drawer = Drawer(canvas, painter, offset)
    }

    private fun Int.toPx() : Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}