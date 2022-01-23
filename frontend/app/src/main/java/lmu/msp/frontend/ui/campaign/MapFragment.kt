package lmu.msp.frontend.ui.campaign

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import lmu.msp.frontend.R
import lmu.msp.frontend.models.websocket.DrawMessage
import lmu.msp.frontend.viewmodels.WebSocketDataViewModel

class MapFragment : Fragment(R.layout.fragment_map) {

    lateinit var viewModel: WebSocketDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val relativeLayout = view.findViewById<RelativeLayout>(R.id.canvas_layout)
        val canvas_bg = view.findViewById<ImageView>(R.id.canvas_bg)

        val myCanvasView = MyCanvasView(view.context)
        relativeLayout.addView(myCanvasView)
        val delete = view.findViewById<Button>(R.id.delete)
        val draw = view.findViewById<Button>(R.id.draw)
        val background = view.findViewById<Button>(R.id.background)
        delete.setOnClickListener {
            myCanvasView.resetCanvasDrawing()
            viewModel.sendDrawMessageClear()
        }
        draw.setOnClickListener {
            myCanvasView.drawFromServer(myCanvasView.testDrawnObject)

        }
        background.setOnClickListener {
            canvas_bg.setImageResource(R.drawable.yawning)

        }

        viewModel = ViewModelProvider(requireActivity()).get(WebSocketDataViewModel::class.java)


        viewModel.getDrawMessagesNew().observe(viewLifecycleOwner, { drawMessages ->
            viewModel.getSemaphore().acquire()

            drawMessages.forEach {
                myCanvasView.drawFromServer(
                    DrawObject(
                        it.currentX,
                        it.eventX,
                        it.currentY,
                        it.eventY
                    )
                )

            }
            Log.i("draw ing", " ${drawMessages.size}")
            drawMessages.clear()
            viewModel.getSemaphore().release()
        })

        viewModel.getDrawCanvasClear().observe(viewLifecycleOwner, {
            if (it) {
                myCanvasView.resetCanvasDrawing()
                viewModel.getDrawCanvasClear().postValue(false)
            }

        })

        viewModel.getDrawMessages().value?.forEach {
            myCanvasView.drawFromServer(
                DrawObject(
                    it.currentX,
                    it.eventX,
                    it.currentY,
                    it.eventY
                )
            )
        }
        return view
    }

    private inner class MyCanvasView(context: Context) : View(context) {
        private var extraCanvas: Canvas? = null
        private var path = Path()
        private var allStrokes = ArrayList<Stroke>()
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
            for (s: Stroke in allStrokes) {
                canvas.drawPath(s.path, s.paint)
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
            emitToServer()
            invalidate()
        }

        private fun touchMove() {
            val dx = Math.abs(motionTouchEventX - currentX)
            val dy = Math.abs(motionTouchEventY - currentY)
            if (dx >= touchTolerance || dy >= touchTolerance) {
                emitToServer()
                path.quadTo(
                    currentX,
                    currentY,
                    (motionTouchEventX + currentX) / 2,
                    (motionTouchEventY + currentY) / 2
                )
                currentX = motionTouchEventX
                currentY = motionTouchEventY
            }
            invalidate()
        }

        private fun touchUp() {
            emitToServer()
            path.lineTo(currentX, currentY)
            allStrokes.add(Stroke(path, paint))
            extraCanvas?.drawPath(path, paint)
            path = Path()
        }

        var testDrawnObject: DrawObject = DrawObject(0f, 0f, 0f, 0f)
        private fun emitToServer() {
            val drawObject = DrawObject(
                currentX / width,
                motionTouchEventX / width,
                currentY / height,
                motionTouchEventY / height
            )

            testDrawnObject = drawObject

            viewModel.sendDrawMessage(
                DrawMessage(
                    drawColor,
                    drawObject.currentX,
                    drawObject.eventX,
                    drawObject.currentY,
                    drawObject.eventY
                )
            )
        }

        fun drawFromServer(drawObject: DrawObject) {
            val mx = drawObject.currentX * width
            val x = drawObject.eventX * width
            val my = drawObject.currentY * height
            val y = drawObject.eventY * height

            val path = Path()
            path.moveTo(mx, my)
            path.lineTo(x, y)
            allStrokes.add(Stroke(path, paint))
            // path = Path()
            invalidate()
        }

        fun resetCanvasDrawing() {
            path.reset()
            allStrokes.clear()
            invalidate()
        }


    }

    class DrawObject(
        val currentX: Float,
        val eventX: Float,
        val currentY: Float,
        val eventY: Float
    )

    class Stroke(val path: Path, val paint: Paint)

}