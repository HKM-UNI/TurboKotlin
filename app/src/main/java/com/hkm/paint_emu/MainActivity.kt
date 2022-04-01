package com.hkm.paint_emu

import android.content.res.Resources
import android.graphics.*
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    // Dimensions for each of the ImageView
    private val imgWidth = 326.toPx()
    private val imgHeight = 204.toPx()
    // Offset to draw in the images
    // It's centered to simulate the origin in a plane
    private val centeredOffset = Point(imgWidth /2, imgHeight/2)

    // Dimensions for activity_rotation, since it will be drawn using the entire screen
    private var screenHeight = 0
    private var screenWidth = 0
    // This offset is the same as the previous one,
    // but it needs the dimensions of the screen,
    // which must be obtained at runtime.
    private lateinit var fullCanvasOffset : Point

    /**
     * Custom class to deal with canvas and paint
     */
    private var drawer = Drawer()

    /**
     * The default painter to draw in the plane
     */
    private var defPainter = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5.0f
    }

    /**
     * A dashed paint implementation
     */
    private val painterDashed = Paint().apply {
        color = Color.BLACK
        strokeWidth = 3.0f
        pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
    }

    // These bitmaps will be the background of each imageView
    private lateinit var bmpTranslateX : Bitmap
    private lateinit var bmpTranslateY : Bitmap
    private lateinit var bmpRotation : Bitmap
    private lateinit var bmpMirrorH : Bitmap
    private lateinit var bmpMirrorV : Bitmap
    private lateinit var bmpScale : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun setActivityMain(view: View) {
        setContentView(R.layout.activity_main)
        initMain()
    }

    fun setActivityRotation(view: View) {
        setContentView(R.layout.activity_rotation)
        setScreenSizes()
        initRotation()
    }

    fun setActivityMenu(view: View) {
        setContentView(R.layout.activity_menu)
        // Restores default painter color
        defPainter.apply { color = Color.BLACK }
    }

    private fun setScreenSizes() {
        var metrics = Resources.getSystem().displayMetrics
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        fullCanvasOffset = Point(screenWidth/2, screenHeight/2)
    }

    private fun initMain() {
        drawPlanes()
        drawTranslationX()
        drawTranslationY()
        drawRotation()
        drawReflectionY()
        drawReflectionX()
        drawScale()
    }

    private fun initRotation() {
        val planeTemplate = getBitmapPlane(screenWidth, screenHeight, offset = Point(0,0))
        drawRotationFullScreen(planeTemplate)
        findViewById<ImageView>(R.id.imageV9).setImageBitmap(planeTemplate)
    }

    private fun drawRotationFullScreen(bitmap: Bitmap) {
        /**
         * A w-shaped polygon
         */
        var polyW = Polygon(arrayOf(
            Point(-800, 600), // A
            Point(-400, -400), // B
            Point(-200, -400), // C
            Point(-60, 195), // D
            Point(60, 195), // E
            Point(200, -400), // F
            Point(400, -400), // G
            Point(800, 600), // H
            Point(600, 600), // I
            Point(380, -80), // J
            Point(256, -80), // K
            Point(160, 315), // L
            Point(-160, 315), // M
            Point(-256, -80), // N
            Point(-380, -80), // O
            Point(-600, 600) // P
        ))
        setDrawer(Canvas(bitmap), offset = fullCanvasOffset)

        // Since the original bitmap is too big for the screen,
        // a couple of transformations are needed
        polyW = polyW.scale(0.5f, 0.5f).reflectX().translate(0, -500)

        drawer.draw(polyW)
        defPainter.apply { color = Color.BLUE }
        drawer.draw(polyW.rotate(45f).translate(-400, 600))
    }

    private fun drawPlanes() {
        val planeTemplate = getBitmapPlane(imgWidth, imgHeight, offset = Point(0,0))
        // Init each bitmap with the plane template
        bmpTranslateX = Bitmap.createBitmap(planeTemplate)
        bmpTranslateY = Bitmap.createBitmap(planeTemplate)
        bmpRotation = Bitmap.createBitmap(planeTemplate)
        bmpMirrorH = Bitmap.createBitmap(planeTemplate)
        bmpMirrorV = Bitmap.createBitmap(planeTemplate)
        bmpScale = Bitmap.createBitmap(planeTemplate)

        // Setting up the same plane for each image view
        findViewById<ImageView>(R.id.imageV).setImageBitmap(bmpTranslateX)
        findViewById<ImageView>(R.id.imageV2).setImageBitmap(bmpTranslateY)
        findViewById<ImageView>(R.id.imageV3).setImageBitmap(bmpRotation)
        findViewById<ImageView>(R.id.imageV4).setImageBitmap(bmpMirrorH)
        findViewById<ImageView>(R.id.imageV5).setImageBitmap(bmpMirrorV)
        findViewById<ImageView>(R.id.imageV6).setImageBitmap(bmpScale)
    }

    private fun getBitmapPlane(
        width: Int,
        height: Int,
        offset: Point
    ) : Bitmap {
        val planeTemplate = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(planeTemplate)
        setDrawer(canvas, painterDashed.apply { color = Color.BLACK }, offset)

        val topCenter = Point(width/2, 0)
        val bottomCenter = Point(width/2, height)
        drawer.drawSegment(topCenter, bottomCenter)

        val leftMidCenter = Point(0, height/2)
        val rightMidCenter = Point(width, height/2)
        drawer.drawSegment(leftMidCenter, rightMidCenter)

        return planeTemplate
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

    private fun drawScale() {
        val squareO = Polygon(arrayOf(
            Point(-50, 50),
            Point(50, 50),
            Point(50, -50),
            Point(-50, -50))
        )
        setDrawer(Canvas(bmpScale))

        drawer.draw(squareO)
        setDrawer(Canvas(bmpScale), painterDashed.apply { color = Color.BLUE })
        drawer.draw(squareO.scale(2f, 2f))
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