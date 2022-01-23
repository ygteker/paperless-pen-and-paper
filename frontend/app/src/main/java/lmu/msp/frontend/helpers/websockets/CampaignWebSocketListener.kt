package lmu.msp.frontend.helpers.websockets

import android.util.Log
import com.google.gson.Gson
import lmu.msp.frontend.models.websocket.BasicMessage
import lmu.msp.frontend.models.websocket.MessageType
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class CampaignWebSocketListener(private val webSocketCallback: WebSocketCallback) :
    WebSocketListener() {
    companion object {
        private const val TAG = "CampaignWebSocketListener"
    }

    val gson = Gson()

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d(TAG, "onClosed: $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.d(TAG, "onFailure: ${response?.code}")
        t.printStackTrace()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        val basicMessage = gson.fromJson(text, BasicMessage::class.java)

        when (basicMessage.messageType) {
            MessageType.CONNECT -> {
                webSocketCallback.receiveChatMessages(basicMessage.chatMessage!!)
                webSocketCallback.receiveDrawMessages(basicMessage.drawMessage!!)
                webSocketCallback.receivedDrawImage(basicMessage.drawImage!!)
            }
            MessageType.DISCONNECT -> TODO()
            MessageType.CHAT_MESSAGE -> webSocketCallback.receiveChatMessages(basicMessage.chatMessage!!)
            MessageType.DRAW_PATH -> webSocketCallback.receiveDrawMessages(basicMessage.drawMessage!!)
            MessageType.DRAW_IMAGE -> webSocketCallback.receivedDrawImage(basicMessage.drawImage!!)
            MessageType.DRAW_RESET -> webSocketCallback.receiveDrawMessageReset()
        }

        Log.i(TAG, text)
    }
}