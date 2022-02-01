package lmu.msp.backend.socket.model

/**
 * message type DRAW_PATH
 * used for the live drawing
 *
 * @property color
 * @property currentX
 * @property eventX
 * @property currentY
 * @property eventY
 */
data class DrawMessage(val color:Int, val currentX: Float, val eventX: Float, val currentY: Float, val eventY: Float)