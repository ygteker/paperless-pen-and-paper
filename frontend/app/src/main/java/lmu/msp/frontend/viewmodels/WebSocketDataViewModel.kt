package lmu.msp.frontend.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import lmu.msp.frontend.helpers.liveDataListAddElement
import lmu.msp.frontend.helpers.liveDataListAddElementList
import lmu.msp.frontend.helpers.liveDataListClear
import lmu.msp.frontend.helpers.websockets.CampaignWebSocketListener
import lmu.msp.frontend.helpers.websockets.WebSocketCallback
import lmu.msp.frontend.helpers.websockets.WebSocketProvider
import lmu.msp.frontend.models.websocket.*
import okhttp3.WebSocket
import java.util.*


class WebSocketDataViewModel(application: Application) : AndroidViewModel(application) {
    private val gson = Gson()

    private val webSocketProvider = WebSocketProvider(application)

    //make val privat so that it is harder to change it by mistake from the outside
    private val campaignId = MutableLiveData<Long>(0L)

    private var webSocket: WebSocket? = null

    private val chatMessages = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
    private val drawMessages = MutableLiveData<MutableList<DrawMessage>>(mutableListOf())
    private val drawImage = MutableLiveData<DrawImage>()


    fun getCampaignId(): LiveData<Long> = campaignId

    fun getChatMessages(): LiveData<MutableList<ChatMessage>> = chatMessages
    fun getDrawMessages(): LiveData<MutableList<DrawMessage>> = drawMessages
    fun getDrawImage(): LiveData<DrawImage> = drawImage

    fun sendDrawMessageClear() {
        webSocket?.send(gson.toJson(BasicMessage(MessageType.DRAW_RESET)))
        liveDataListClear(drawMessages)
    }

    fun sendDrawMessage(drawMessage: DrawMessage) {
        liveDataListAddElement(drawMessage, drawMessages)
        webSocket?.send(gson.toJson(BasicMessage(MessageType.DRAW_PATH, null, listOf(drawMessage))))
    }

    fun startWebSocket(campaignId: Long) {
        this.campaignId.value = campaignId
        webSocket?.cancel()
        webSocket =
            webSocketProvider.start(campaignId, CampaignWebSocketListener(ImpWebSocketCallback()))
    }

    fun sendImage(byteArray: ByteArray) {
        val msg = gson.toJson(
            BasicMessage(MessageType.DRAW_IMAGE, null, null, DrawImage.create(byteArray))
        )
        Log.i("tasdas", msg.length.toString() + "a")
        webSocket?.send(
            msg
        )

    }

    private inner class ImpWebSocketCallback : WebSocketCallback {

        override fun receiveChatMessages(chatMessages: List<ChatMessage>) {
            liveDataListAddElementList(chatMessages, this@WebSocketDataViewModel.chatMessages)
        }

        override fun receiveDrawMessages(drawMessages: List<DrawMessage>) {
            liveDataListAddElementList(drawMessages, this@WebSocketDataViewModel.drawMessages)
        }

        override fun receiveDrawMessageReset() {
            liveDataListClear(drawMessages)
        }

        override fun receivedDrawImage(drawImage: DrawImage) {
            this@WebSocketDataViewModel.drawImage.postValue(drawImage)
        }

    }

}