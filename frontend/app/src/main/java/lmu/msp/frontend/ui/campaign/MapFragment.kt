package lmu.msp.frontend.ui.campaign

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import lmu.msp.frontend.R

class MapFragment : Fragment(R.layout.fragment_map) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        var relativeLayout = view.findViewById<RelativeLayout>(R.id.rect)
        relativeLayout.addView( MyCanvasView(view.context))

            return view
    }

}

private class MyCanvasView(context: Context) : View(context) {
    private var extraCanvas: Canvas? = null
    private var path = Path()
    private val paths = ArrayList<Path>()
    private val undonePaths = ArrayList<Path>()
    private var strokeWidth = 12f
    private val drawColor = ResourcesCompat.getColor(resources, R.color.black, null)


    private val paint = Paint().apply {
        color = drawColor

        isAntiAlias = true

        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 12f
    }

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    private var currentX = 0f
    private var currentY = 0f

    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop


    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        extraCanvas = Canvas()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (Path in paths) {
            canvas.drawPath(Path, paint)
        }
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }
    private fun touchStart() {
        undonePaths.clear()
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
        invalidate()
    }

    private fun touchMove() {
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
        }
        invalidate()
    }

    private fun touchUp() {
        path.lineTo(currentX, currentY)
        extraCanvas?.drawPath(path, paint)
        paths.add(path)
        path = Path()

    }

    /*fun undoCanvasDrawing() {
        if (paths.size > 0) {
            undonePaths.add(paths.removeAt(paths.size - 1))
            invalidate()
        } else {
            Log.d("UNDO_ERROR", "Something went wrong with UNDO action")
        }
    }
    fun redoCanvasDrawing() {
        if (undonePaths.size > 0) {
            paths.add(undonePaths.removeAt(undonePaths.size - 1))
            invalidate()
        } else {
            Log.d("REDO_ERROR", "Something went wrong with REDO action")
        }
    }
    fun resetCanvasDrawing() {
        path.reset() // Avoiding saving redo from Path()
        paths.clear()
        invalidate()
    }*/
}