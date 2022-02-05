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

/**
 *
 * (demo) view model to show how an AndroidViewModel works and how the websocket sends recieved data
 * to the view model
 *
 * @param application
 */
class WebSocketDataViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * semaphores because websocket calls a async => if observer changes something at the same moment as
     * the websocket listener racecondition => protect write operations on the lists with semaphores
     */
    private val drawSemaphore = Semaphore(1)
    private val chatSemaphore = Semaphore(1)

    /**
     * converter from json to obj and obj to json
     */
    private val gson = Gson()

    private val webSocketProvider = WebSocketProvider(application)

    //make val privat so that it is harder to change it by mistake from the outside
    private val campaignId = MutableLiveData<Long>(0L)

    private var webSocket: WebSocket? = null

    /**
     * observable data
     */
    private val chatMessages = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
    private val chatMessagesNew = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
    private val drawCanvasClear = MutableLiveData(false)
    private val drawMessages = MutableLiveData<MutableList<DrawMessage>>(mutableListOf())
    private val drawMessagesNew = MutableLiveData<MutableList<DrawMessage>>(mutableListOf())
    private val drawImage = MutableLiveData<DrawImage>()


    private val groupMessages = MutableLiveData<MutableList<GroupMessage>>(mutableListOf())

    fun getSemaphore() = drawSemaphore


    fun getDrawCanvasClear() = drawCanvasClear

    /**
     * getters to reduce the information about the class type (e.g. from the outside only live data is seen => you cant modify the data only read it)
     *
     */
    fun getCampaignId(): LiveData<Long> = campaignId
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

    /**
     * use this method to connect to the websocket
     * otherwise this viewmodel will not work
     *
     * @param campaignId
     */
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

    /**
     * implementation of the websocket callback
     * connection between websocket listener and viewmodel
     *
     */
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