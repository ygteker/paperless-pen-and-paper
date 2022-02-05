package lmu.msp.frontend.ui.campaign

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.FragmentMapBinding
import lmu.msp.frontend.models.websocket.DrawMessage
import lmu.msp.frontend.viewmodels.WebSocketDataViewModel
import java.io.ByteArrayOutputStream

const val DRAW_BLACK = "#000000"
const val DRAW_BLUE = "#1976d2"
const val DRAW_GREEN = "#388e3c"
const val DRAW_RED = "#e53935"
const val DRAW_YELLOW = "#ffeb3b"
/**
 * Fragment which provides the functionality to draw on a synchronized canvas with a shared
 * background image
 */
class MapFragment : Fragment(R.layout.fragment_map) {

    lateinit var viewModel: WebSocketDataViewModel
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root
        val relativeLayout = binding.canvasLayout
        val myCanvasView = MyCanvasView(view.context)
        relativeLayout.addView(myCanvasView)
        val delete = binding.delete
        val drawColor = binding.color
        val background = binding.background

        //delete canvas button
        delete.setOnClickListener {
            myCanvasView.resetCanvasDrawing()
            viewModel.sendDrawMessageClear()
        }

        //background image button
        background.setOnClickListener {
            pickBackgroundPicture()
        }

        //color pick button
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
                    R.id.ibBlack -> myCanvasView.changeCurrentColor(DRAW_BLACK)
                    R.id.ibBlue -> myCanvasView.changeCurrentColor(DRAW_BLUE)
                    R.id.ibGreen -> myCanvasView.changeCurrentColor(DRAW_GREEN)
                    R.id.ibRed -> myCanvasView.changeCurrentColor(DRAW_RED)
                    R.id.ibYellow -> myCanvasView.changeCurrentColor(DRAW_YELLOW)
                }
                true
            }
            popupMenu.show()
        })

        //logic to observe incoming draw messages
        viewModel = ViewModelProvider(requireActivity()).get(WebSocketDataViewModel::class.java)

        //receive message to clear canvas
        viewModel.getDrawCanvasClear().observe(viewLifecycleOwner, {
            if (it) {
                myCanvasView.resetCanvasDrawing()
                viewModel.getDrawCanvasClear().postValue(false)
            }
        })
        //receive message to draw paths
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
        //receive message to change background image
        viewModel.getDrawImage().observe(viewLifecycleOwner, {
            if (it.imageBase64.isNotEmpty()) {
                val byteArray = it.getByteArray()
                if (byteArray.isNotEmpty()) {
                    val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    binding.canvasBg.setImageBitmap(Bitmap.createBitmap(bmp))
                }
            }
        })

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
            drawMessages.clear()
            viewModel.getSemaphore().release()
        })

        return view
    }

    /**
     * Starts the ImagePicker for the background image
     */
    private fun pickBackgroundPicture() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .galleryOnly()
            .start()
    }

    /**
     * Executed after picking a background image, replaces the current image with the new one and
     * sends it via websockets to synchronize the image
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                binding.canvasBg.setImageURI(data?.data)
                val bitmap = (binding.canvasBg.drawable as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

                viewModel.sendImage(stream.toByteArray())
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * The custom canvas to draw on and the logic behind sending and receiving draw data
     */
    private inner class MyCanvasView(context: Context) : View(context) {
        private var extraCanvas: Canvas? = null
        private var path = Path()
        private var allStrokes = ArrayList<Stroke>()
        private var standardStrokeWidth = 12f
        private var currentColor = Color.BLACK
        private var motionTouchEventX = 0f
        private var motionTouchEventY = 0f
        private var currentX = 0f
        private var currentY = 0f
        private val touchTolerance = 30


        private var paint = Paint().apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = standardStrokeWidth
            color = currentColor
        }

        override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
            super.onSizeChanged(width, height, oldWidth, oldHeight)
            extraCanvas = Canvas()
        }

        /**
         * Redraws the canvas with all saved Strokes everytime invalidate() is used
         */
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            for (s: Stroke in allStrokes) {
                canvas.drawPath(s.path, s.paint)
            }
            canvas.drawPath(path, paint)
        }

        /**
         * Registers a touch event and executes a method based on the touch action
         */
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

        /**
         * Resets the paths and variables when the user starts to touch the canvas
         */
        private fun touchStart() {
            path.reset()
            path.moveTo(motionTouchEventX, motionTouchEventY)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            emitToServer()
            invalidate()
        }

        /**
         * Calculates the lines of the path and adds it to the current path, while the user is
         * moving his finger on the canvas.
         */
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

        /**
         * Saves the path and the paint of the drawn lines when the user stops to touch the canvas
         */
        private fun touchUp() {
            path.lineTo(currentX, currentY)
            allStrokes.add(Stroke(path, paint))
            emitToServer()
            extraCanvas?.drawPath(path, paint)
            path = Path()
            createPaintObject(paint.color, paint.strokeWidth)
            invalidate()
        }

        /**
         * Transforms the the drawn lines into a drawObject and sends it to the websocket
         */
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

        /**
         * Transforms a drawObject (which usually comes from the websocket) and add it to the
         * allStrokes list
         */
        fun drawFromServer(drawObject: DrawObject) {
            val mx = drawObject.currentX * width
            val x = drawObject.eventX * width
            val my = drawObject.currentY * height
            val y = drawObject.eventY * height

            val path = Path()
            changePaintColor(drawObject.color)
            path.moveTo(mx, my)
            path.lineTo(x, y)
            allStrokes.add(Stroke(path, paint))
            createPaintObject(currentColor, paint.strokeWidth)
            invalidate()
        }

        /**
         * Resets all saved strokes
         */
        fun resetCanvasDrawing() {
            path.reset()
            allStrokes.clear()
            invalidate()
        }

        /**
         * Changes the color of the paint
         */
        fun changePaintColor(color: Int) {
            paint.color = color
        }

        /**
         * Changes the current color of the user
         */
        fun changeCurrentColor(color: String) {
            currentColor = Color.parseColor(color)
            paint.color = Color.parseColor(color)
        }

        /**
         * Creates a new Paint object with the current values of the user
         */
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