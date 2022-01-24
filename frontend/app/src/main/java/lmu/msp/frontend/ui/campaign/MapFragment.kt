package lmu.msp.frontend.ui.campaign

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import lmu.msp.frontend.R
import lmu.msp.frontend.models.websocket.DrawMessage
import lmu.msp.frontend.viewmodels.WebSocketDataViewModel
import java.lang.Exception

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
        val drawColor = view.findViewById<Button>(R.id.color)
        val background = view.findViewById<Button>(R.id.background)
        delete.setOnClickListener {
            myCanvasView.resetCanvasDrawing()
            viewModel.sendDrawMessageClear()
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
                        it.color,
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
                    it.color,
                    it.currentX,
                    it.eventX,
                    it.currentY,
                    it.eventY

                )
            )
        }
        drawColor.setOnClickListener(View.OnClickListener {
            val popupMenu = PopupMenu(view.context, drawColor)

            //To display the icons in the popup menu the following try catch method is required
            try {
                val fields = popupMenu.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field[popupMenu]
                        val classPopupHelper = Class.forName(
                            menuPopupHelper
                                .javaClass.name
                        )
                        val setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            popupMenu.menuInflater.inflate(R.menu.popup_color_picker, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.ibBlack -> myCanvasView.changeColor(Color.BLACK)
                    R.id.ibBlue -> myCanvasView.changeColor(Color.BLUE)
                    R.id.ibGreen -> myCanvasView.changeColor(Color.GREEN)
                    R.id.ibRed -> myCanvasView.changeColor(Color.RED)
                    R.id.ibYellow -> myCanvasView.changeColor(Color.YELLOW)
                }
                true
            }
            popupMenu.show()
        })
        return view
    }

    private inner class MyCanvasView(context: Context) : View(context) {
        private var extraCanvas: Canvas? = null
        private var path = Path()
        private var allStrokes = ArrayList<Stroke>()
        private val undonePaths = ArrayList<Path>()
        private var standardStrokeWidth = 12f
        private val drawColor = Color.BLACK


        private var paint = Paint().apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = standardStrokeWidth
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
            createPaintObject(paint.color, paint.strokeWidth)
            invalidate()
        }

        private fun emitToServer() {
            val drawObject = DrawObject(
                paint.color,
                currentX / width,
                motionTouchEventX / width,
                currentY / height,
                motionTouchEventY / height
            )


            viewModel.sendDrawMessage(
                DrawMessage(
                    drawObject.color,
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
            changeColor(drawObject.color)
            path.moveTo(mx, my)
            path.lineTo(x, y)
            allStrokes.add(Stroke(path, paint))
            createPaintObject(paint.color, paint.strokeWidth)
            println("color"+ paint.color)
            // path = Path()
            invalidate()
        }

        fun resetCanvasDrawing() {
            path.reset()
            allStrokes.clear()
            invalidate()
        }

        fun changeColor(color: Int) {
            paint.color = color
        }
        private fun createPaintObject(color: Int, strokeWidth: Float) {
            paint = Paint()
            paint.isAntiAlias = true
            paint.color = color
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeWidth = strokeWidth
        }

    }

    class DrawObject(
        val color: Int,
        val currentX: Float,
        val eventX: Float,
        val currentY: Float,
        val eventY: Float
    )

    class Stroke(val path: Path, val paint: Paint)

}