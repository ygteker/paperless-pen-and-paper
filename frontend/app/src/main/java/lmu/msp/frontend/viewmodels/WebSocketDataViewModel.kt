package lmu.msp.frontend.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import lmu.msp.frontend.helpers.websockets.CampaignWebSocketListener
import lmu.msp.frontend.helpers.websockets.WebSocketCallback
import lmu.msp.frontend.helpers.websockets.WebSocketProvider
import lmu.msp.frontend.models.websocket.ChatMessage
import lmu.msp.frontend.models.websocket.DrawMessage
import okhttp3.WebSocket

class WebSocketDataViewModel(application: Application) : AndroidViewModel(application) {

    private val webSocketProvider = WebSocketProvider(application)

    //make val privat so that it is harder to change it by mistake from the outside
    private val campaignId = MutableLiveData<Long>(0L)

    private var webSocket: WebSocket? = null

    private val chatMessages = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())

    private val drawMessages = MutableLiveData<MutableList<DrawMessage>>(mutableListOf())

    fun getCampaignId(): LiveData<Long> = campaignId

    fun getChatMessages(): LiveData<MutableList<ChatMessage>> = chatMessages
    fun getDrawMessages(): LiveData<MutableList<DrawMessage>> = drawMessages


    fun startWebSocket(campaignId: Long) {
        this.campaignId.value = campaignId
        webSocket?.cancel()
        webSocket =
            webSocketProvider.start(campaignId, CampaignWebSocketListener(ImpWebSocketCallback()))
    }

    private inner class ImpWebSocketCallback : WebSocketCallback {

        override fun receiveChatMessages(chatMessages: List<ChatMessage>) {
            val data = this@WebSocketDataViewModel.chatMessages.value ?: mutableListOf()
            data.addAll(chatMessages)
            this@WebSocketDataViewModel.chatMessages.postValue(data)
        }

        override fun receiveDrawMessages(drawMessages: List<DrawMessage>) {
            val data = this@WebSocketDataViewModel.drawMessages.value ?: mutableListOf()
            data.addAll(drawMessages)
            this@WebSocketDataViewModel.drawMessages.postValue(data)

        }

        override fun receiveDrawMessageReset() {
            this@WebSocketDataViewModel.drawMessages.postValue(mutableListOf())
        }

    }

}