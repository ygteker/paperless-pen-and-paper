package lmu.msp.frontend.viewmodels

import android.app.Application
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
import java.util.concurrent.Semaphore

class WebSocketDataViewModel(application: Application) : AndroidViewModel(application) {

    private val drawSemaphore = Semaphore(1)
    private val chatSemaphore = Semaphore(1)
    private val gson = Gson()

    private val webSocketProvider = WebSocketProvider(application)

    //make val privat so that it is harder to change it by mistake from the outside
    private val campaignId = MutableLiveData<Long>(0L)

    private var webSocket: WebSocket? = null

    private val chatMessages = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
    private val chatMessagesNew = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())

    private val drawCanvasClear = MutableLiveData(false)
    private val drawMessages = MutableLiveData<MutableList<DrawMessage>>(mutableListOf())
    private val drawMessagesNew = MutableLiveData<MutableList<DrawMessage>>(mutableListOf())
    private val drawImage = MutableLiveData<DrawImage>()


    private val groupMessages = MutableLiveData<MutableList<GroupMessage>>(mutableListOf())


    fun getCampaignId(): LiveData<Long> = campaignId

    fun getSemaphore() = drawSemaphore


    fun getDrawCanvasClear() = drawCanvasClear

    fun getChatMessages(): LiveData<MutableList<ChatMessage>> = chatMessages
    fun getChatMessagesNew(): LiveData<MutableList<ChatMessage>> = chatMessagesNew
    fun getDrawMessages(): LiveData<MutableList<DrawMessage>> = drawMessages
    fun getDrawMessagesNew(): LiveData<MutableList<DrawMessage>> = drawMessagesNew
    fun getDrawImage(): LiveData<DrawImage> = drawImage
    fun getGroupMessages(): LiveData<MutableList<GroupMessage>> = groupMessages

    fun sendDrawMessageClear() {
        webSocket?.send(gson.toJson(BasicMessage(MessageType.DRAW_RESET)))
        liveDataListClear(drawMessages)
    }

    fun sendDrawMessage(drawMessage: DrawMessage) {
        liveDataListAddElement(drawMessage, drawMessages)
        webSocket?.send(
            gson.toJson(
                BasicMessage(
                    MessageType.DRAW_PATH,
                    drawMessage = listOf(drawMessage)
                )
            )
        )
    }

    fun startWebSocket(campaignId: Long) {
        this.campaignId.value = campaignId
        webSocket?.cancel()
        webSocket =
            webSocketProvider.start(campaignId, CampaignWebSocketListener(ImpWebSocketCallback()))
    }

    fun sendChatMessage(chatMessage: ChatMessage) {
//        liveDataListAddElement(chatMessage, chatMessages)
        webSocket?.send(gson.toJson(BasicMessage(MessageType.CHAT_MESSAGE, listOf(chatMessage))))
    }

    fun sendGroupMessage(groupMessage: GroupMessage) {
//        liveDataListAddElement(groupMessage, groupMessages)
        webSocket?.send(
            gson.toJson(
                BasicMessage(
                    MessageType.GROUP_MESSAGE,
                    groupMessage = listOf(groupMessage)
                )
            )
        )
    }


    fun sendImage(byteArray: ByteArray) {
        webSocket?.send(
            gson.toJson(
                BasicMessage(
                    MessageType.DRAW_IMAGE,
                    drawImage = DrawImage.create(byteArray)
                )
            )
        )

    }

    private inner class ImpWebSocketCallback : WebSocketCallback {

        override fun receiveChatMessages(chatMessages: List<ChatMessage>) {
            liveDataListAddElementList(chatMessages, this@WebSocketDataViewModel.chatMessages)
//            chatSemaphore.acquire()
//            Log.i("TAG", "acquired")
//            liveDataListAddElementList(chatMessages, chatMessagesNew)
//            chatSemaphore.release()
        }

        override fun receiveGroupMessages(groupMessage: List<GroupMessage>) {
            liveDataListAddElementList(groupMessage, this@WebSocketDataViewModel.groupMessages)
        }

        override fun receiveDrawMessages(drawMessages: List<DrawMessage>) {
            liveDataListAddElementList(drawMessages, this@WebSocketDataViewModel.drawMessages)
            drawSemaphore.acquire()
            liveDataListAddElementList(drawMessages, drawMessagesNew)
            drawSemaphore.release()
        }

        override fun receiveDrawMessageReset() {
            liveDataListClear(drawMessages)
            drawCanvasClear.postValue(true)
        }

        override fun receivedDrawImage(drawImage: DrawImage) {
            this@WebSocketDataViewModel.drawImage.postValue(drawImage)
        }

    }

}