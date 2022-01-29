package lmu.msp.frontend.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lmu.msp.frontend.api.model.Message
import lmu.msp.frontend.helpers.liveDataListAddElement
import lmu.msp.frontend.helpers.liveDataListAddElementList
import lmu.msp.frontend.helpers.liveDataListClear
import lmu.msp.frontend.helpers.liveDataRemoveElement

class MessagesViewModel: ViewModel() {

    var lst = MutableLiveData<MutableList<Message>>()

    fun add(messages: List<Message>){
        liveDataListAddElementList(messages, lst)
    }

    fun add(message: Message) {
        liveDataListAddElement(message, lst)
    }

    fun clearList() {
        liveDataListClear(lst)
    }

}