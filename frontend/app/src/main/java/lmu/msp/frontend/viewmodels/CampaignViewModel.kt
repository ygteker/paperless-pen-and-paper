package lmu.msp.frontend.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lmu.msp.frontend.models.websocket.ChatMessage

class CampaignViewModel: ViewModel() {
    var chatList = MutableLiveData<ArrayList<ChatMessage>>()
    var newChatList = arrayListOf<ChatMessage>()

    fun add(message: ChatMessage) {
        newChatList.add(message)
        chatList.value = newChatList
    }

    fun remove(message: ChatMessage) {
        newChatList.remove(message)
        chatList.value = newChatList
    }

    fun getMessages(accessToken: String) {
        //TODO handle ws connection
    }
}